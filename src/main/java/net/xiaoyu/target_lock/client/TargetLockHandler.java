package net.xiaoyu.target_lock.client;

import net.xiaoyu.target_lock.TargetLock;
import net.xiaoyu.target_lock.ModKeyBindings;
import net.xiaoyu.target_lock.Config;
import net.xiaoyu.target_lock.util.EntityUtils;
import net.xiaoyu.target_lock.util.MessageUtils;
import net.xiaoyu.target_lock.util.RotationUtils;
import net.xiaoyu.target_lock.util.LockTargetData;
import net.xiaoyu.target_lock.network.TargetLockPacket;
import net.xiaoyu.target_lock.network.NetworkHandler;
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
                // 发送解锁包，目标ID为-1表示解锁当前锁定的目标
                NetworkHandler.sendToServer(new TargetLockPacket(-1, false));
            } else {
                lockNearestEntity(mc);
            }
        }

        if (lockData.isTargetLocked() && Config.USE_NEAREST_ENTITY_PRIORITY.get() && mc.player != null) {
            findAndLockNearestEntity(mc);
        }

        if (lockData.isTargetLocked() && lockData.getLockedTarget() != null && !lockData.getLockedTarget().isAlive() &&
            Config.SWITCH_TO_NEXT_TARGET_AFTER_KILL.get() && mc.player != null
        ) {
            switchToNextNearestTarget(mc);
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
        double lockRangeSquared = lockRange * lockRange;

        if (lockData.isLockedTargetValid() && lockData.getLockedTarget().distanceToSqr(player) <= lockRangeSquared) {
            if (Config.LOCK_ENTITY_POSITION.get()) {
                if (lockData.getLockedTargetPosition() == null) {
                    lockData.setLockedTargetPosition(lockData.getLockedTarget().getEyePosition());
                }
                RotationUtils.updatePlayerRotationToTarget(player, lockData.getLockedTargetPosition());
            } else {
                RotationUtils.updatePlayerRotationToTarget(player, lockData.getLockedTarget().getEyePosition());
            }
            return;
        }

        handleInvalidTarget(player, mc);
    }

    private static void handleInvalidTarget(Player player, Minecraft mc) {
        if (!lockData.getLockedTarget().isAlive()) {
            MessageUtils.showTargetKilledMessage(mc);

            if (Config.SWITCH_TO_NEXT_TARGET_AFTER_KILL.get()) {
                switchToNextNearestTarget(mc);
                return;
            } else {
                lockData.reset();
                NetworkHandler.sendToServer(new TargetLockPacket(-1, false));
                return;
            }
        }

        lockData.reset();
        MessageUtils.showTargetLostMessage(mc);
        NetworkHandler.sendToServer(new TargetLockPacket(-1, false));
    }

    private static void lockNearestEntity(Minecraft mc) {
        double lockRange = Config.LOCK_RANGE.get();
        Entity nearestEntity = EntityUtils.findNearestLivingEntity(mc, lockRange);

        if (nearestEntity != null) {
            lockData.setLockedTarget(nearestEntity);
            lockData.setTargetLocked(true);
            lockData.setLockedTargetPosition(null);
            MessageUtils.showLockSuccessMessage(mc, nearestEntity);
            NetworkHandler.sendToServer(new TargetLockPacket(nearestEntity.getId(), true));
        } else {
            MessageUtils.showNoTargetsMessage(mc);
        }
    }

    private static void findAndLockNearestEntity(Minecraft mc) {
        double lockRange = Config.LOCK_RANGE.get();
        Entity nearestEntity = EntityUtils.findNearestLivingEntity(mc, lockRange);

        if (nearestEntity != null && nearestEntity != lockData.getLockedTarget()) {
            lockData.setLockedTarget(nearestEntity);
            lockData.setLockedTargetPosition(null);
            MessageUtils.showSwitchTargetMessage(mc, nearestEntity);
            NetworkHandler.sendToServer(new TargetLockPacket(nearestEntity.getId(), true));
        }
    }

    private static void switchToNextNearestTarget(Minecraft mc) {
        double lockRange = Config.LOCK_RANGE.get();
        Entity nearestEntity = EntityUtils.findNearestLivingEntity(mc, lockRange, lockData.getLockedTarget());

        if (nearestEntity != null) {
            lockData.setLockedTarget(nearestEntity);
            lockData.setTargetLocked(true);
            lockData.setLockedTargetPosition(null);
            MessageUtils.showSwitchToNextTargetMessage(mc, nearestEntity);
            NetworkHandler.sendToServer(new TargetLockPacket(nearestEntity.getId(), true));
        } else {
            lockData.reset();
            MessageUtils.showNoNextTargetMessage(mc);
            NetworkHandler.sendToServer(new TargetLockPacket(-1, false));
        }
    }

    public static boolean isEntityLocked(Entity entity) {
        return lockData.isTargetLocked() && lockData.getLockedTarget() != null &&
        lockData.getLockedTarget().getId() == entity.getId();
    }
}