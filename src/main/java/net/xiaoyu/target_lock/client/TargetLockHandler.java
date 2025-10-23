package net.xiaoyu.target_lock.client;

import net.xiaoyu.target_lock.TargetLock;
import net.xiaoyu.target_lock.ModKeyBindings;
import net.xiaoyu.target_lock.Config;
import net.xiaoyu.target_lock.util.EntityUtils;
import net.xiaoyu.target_lock.util.MessageUtils;
import net.xiaoyu.target_lock.util.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = TargetLock.MOD_ID)
public class TargetLockHandler {
    private static Entity lockedTarget = null;
    private static boolean isTargetLocked = false;

    public static void init() {
        NeoForge.EVENT_BUS.register(TargetLockHandler.class);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (ModKeyBindings.TARGET_LOCK_KEY.consumeClick() && mc.player != null) {
            if (isTargetLocked) {
                isTargetLocked = false;
                lockedTarget = null;
                MessageUtils.showUnlockMessage(mc);
            } else {
                lockNearestEntity(mc);
            }
        }

        if (isTargetLocked && Config.USE_NEAREST_ENTITY_PRIORITY.get() && mc.player != null) {
            findAndLockNearestEntity(mc);
        }

        if (isTargetLocked && lockedTarget != null && !lockedTarget.isAlive() && 
            Config.SWITCH_TO_NEXT_TARGET_AFTER_KILL.get() && mc.player != null
        ) {
            switchToNextNearestTarget(mc);
        }
    }
    
    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        if (!isTargetLocked || lockedTarget == null) {
            return;
        }
        
        Player player = event.getEntity();
        Minecraft mc = Minecraft.getInstance();

        double lockRange = Config.LOCK_RANGE.get();
        double lockRangeSquared = lockRange * lockRange;

        if (lockedTarget.isAlive() && lockedTarget.distanceToSqr(player) <= lockRangeSquared) {
            RotationUtils.updatePlayerRotationToTarget(player, lockedTarget.getEyePosition());
            return;
        }

        handleInvalidTarget(player, mc);
    }

    private static void handleInvalidTarget(Player player, Minecraft mc) {
        if (!lockedTarget.isAlive()) {
            MessageUtils.showTargetKilledMessage(mc);

            if (Config.SWITCH_TO_NEXT_TARGET_AFTER_KILL.get()) {
                switchToNextNearestTarget(mc);
                return;
            } else {
                isTargetLocked = false;
                lockedTarget = null;
                return;
            }
        }

        isTargetLocked = false;
        lockedTarget = null;
        MessageUtils.showTargetLostMessage(mc);
    }

    private static void lockNearestEntity(Minecraft mc) {
        double lockRange = Config.LOCK_RANGE.get();
        Entity nearestEntity = EntityUtils.findNearestLivingEntity(mc, lockRange);
        
        if (nearestEntity != null) {
            lockedTarget = nearestEntity;
            isTargetLocked = true;
            MessageUtils.showLockSuccessMessage(mc, nearestEntity);
        } else {
            MessageUtils.showNoTargetsMessage(mc);
        }
    }

    private static void findAndLockNearestEntity(Minecraft mc) {
        double lockRange = Config.LOCK_RANGE.get();
        Entity nearestEntity = EntityUtils.findNearestLivingEntity(mc, lockRange);
        
        if (nearestEntity != null && nearestEntity != lockedTarget) {
            lockedTarget = nearestEntity;
            MessageUtils.showSwitchTargetMessage(mc, nearestEntity);
        }
    }

    private static void switchToNextNearestTarget(Minecraft mc) {
        double lockRange = Config.LOCK_RANGE.get();
        Entity nearestEntity = EntityUtils.findNearestLivingEntity(mc, lockRange, lockedTarget);
        
        if (nearestEntity != null) {
            lockedTarget = nearestEntity;
            isTargetLocked = true;
            MessageUtils.showSwitchToNextTargetMessage(mc, nearestEntity);
        } else {
            isTargetLocked = false;
            lockedTarget = null;
            MessageUtils.showNoNextTargetMessage(mc);
        }
    }
}