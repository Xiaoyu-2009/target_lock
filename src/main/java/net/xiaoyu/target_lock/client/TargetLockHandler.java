package net.xiaoyu.target_lock.client;

import net.xiaoyu.target_lock.TargetLock;
import net.xiaoyu.target_lock.ModKeyBindings;
import net.xiaoyu.target_lock.Config;
import net.xiaoyu.target_lock.util.EntityUtils;
import net.xiaoyu.target_lock.util.MessageUtils;
import net.xiaoyu.target_lock.util.RotationUtils;
import net.xiaoyu.target_lock.util.LockTargetData;
import net.xiaoyu.target_lock.util.TargetingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = TargetLock.MOD_ID)
public class TargetLockHandler {
    private static final LockTargetData lockData = new LockTargetData();

    public static void init() {
        NeoForge.EVENT_BUS.register(TargetLockHandler.class);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (ModKeyBindings.TARGET_LOCK_KEY.consumeClick() && mc.player != null) {
            if (lockData.isTargetLocked()) {
                lockData.reset();
                MessageUtils.showUnlockMessage(mc);
            } else {
                TargetingUtils.lockNearestEntity(mc, lockData);
            }
        }

        if (lockData.isTargetLocked() && Config.USE_NEAREST_ENTITY_PRIORITY.get() && mc.player != null) {
            TargetingUtils.findAndLockNearestEntity(mc, lockData);
        }

        if (lockData.isTargetLocked() && lockData.getLockedTarget() != null && !lockData.getLockedTarget().isAlive() && 
            Config.SWITCH_TO_NEXT_TARGET_AFTER_KILL.get() && mc.player != null
        ) {
            TargetingUtils.switchToNextNearestTarget(mc, lockData);
        }
    }
    
    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        if (!lockData.isTargetLocked() || lockData.getLockedTarget() == null) {
            return;
        }
        
        Player player = event.getEntity();
        Minecraft mc = Minecraft.getInstance();

        double lockRange = Config.LOCK_RANGE.get();
        double lockRangeSquared = EntityUtils.getLockRangeSquared(lockRange);

        if (lockData.isLockedTargetValid() && lockData.getLockedTarget().distanceToSqr(player) <= lockRangeSquared) {
            if (Config.LOCK_ENTITY_POSITION.get()) {
                if (lockData.getLockedTargetPosition() == null) {
                    lockData.setLockedTargetPosition(lockData.getLockedTarget().getEyePosition());
                }
                RotationUtils.updatePlayerRotationToTarget(player, lockData.getLockedTargetPosition());
            } else {
                RotationUtils.updatePlayerRotationToTarget(player, lockData.getLockedTarget().getEyePosition());
            }

            if (Config.KEEP_ATTACK_RANGE.get()) {
                TargetingUtils.keepAttackRange(player, lockData.getLockedTarget(), event);
            }

            if (Config.AUTO_ATTACK_AT_CRITICAL_POINT.get()) {
                TargetingUtils.performAutoAttack(player, lockData.getLockedTarget(), mc);
            }
        } else {
            TargetingUtils.handleInvalidTarget(mc, lockData);
        }
    }
}