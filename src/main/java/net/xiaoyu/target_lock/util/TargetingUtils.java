package net.xiaoyu.target_lock.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.xiaoyu.target_lock.Config;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;

public class TargetingUtils {

    public static void lockNearestEntity(Minecraft mc, LockTargetData lockData) {
        double lockRange = Config.LOCK_RANGE.get();
        Entity nearestEntity = EntityUtils.findNearestLivingEntity(mc, lockRange);
        
        if (nearestEntity != null) {
            lockData.setLockedTarget(nearestEntity);
            lockData.setTargetLocked(true);
            lockData.setLockedTargetPosition(null);
            MessageUtils.showLockSuccessMessage(mc, nearestEntity);
        } else {
            MessageUtils.showNoTargetsMessage(mc);
        }
    }

    public static void findAndLockNearestEntity(Minecraft mc, LockTargetData lockData) {
        double lockRange = Config.LOCK_RANGE.get();
        Entity nearestEntity = EntityUtils.findNearestLivingEntity(mc, lockRange);
        
        if (nearestEntity != null && nearestEntity != lockData.getLockedTarget()) {
            lockData.setLockedTarget(nearestEntity);
            lockData.setLockedTargetPosition(null);
            MessageUtils.showSwitchTargetMessage(mc, nearestEntity);
        }
    }

    public static void switchToNextNearestTarget(Minecraft mc, LockTargetData lockData) {
        double lockRange = Config.LOCK_RANGE.get();
        Entity nearestEntity = EntityUtils.findNearestLivingEntity(mc, lockRange, lockData.getLockedTarget());
        
        if (nearestEntity != null) {
            lockData.setLockedTarget(nearestEntity);
            lockData.setTargetLocked(true);
            lockData.setLockedTargetPosition(null);
            MessageUtils.showSwitchToNextTargetMessage(mc, nearestEntity);
        } else {
            lockData.reset();
            MessageUtils.showNoNextTargetMessage(mc);
        }
    }

    public static void handleInvalidTarget(Minecraft mc, LockTargetData lockData) {
        if (!lockData.getLockedTarget().isAlive()) {
            MessageUtils.showTargetKilledMessage(mc);

            if (Config.SWITCH_TO_NEXT_TARGET_AFTER_KILL.get() && Config.AUTO_SWITCH_TO_NEXT_TARGET.get()) {
                switchToNextNearestTarget(mc, lockData);
                return;
            } else {
                lockData.reset();
                return;
            }
        }

        lockData.reset();
        MessageUtils.showTargetLostMessage(mc);
    }

    public static void keepAttackRange(Player player, Entity target, MovementInputUpdateEvent event) {
        double distanceDiff = CombatUtils.getDistanceDiff(player, target);
        Vec3 directionToTarget = CombatUtils.getHorizontalDirectionToTarget(player, target);
        Vec3 moveDirection = CombatUtils.getMoveDirection(directionToTarget, distanceDiff);
        
        double adjustmentFactor = Math.abs(distanceDiff) * Config.ATTACK_RANGE_ADJUSTMENT_FACTOR.get();
        
        float[] moveImpulses = CombatUtils.calculateMoveImpulse(moveDirection, player.getYRot());
        
        event.getInput().forwardImpulse = moveImpulses[0] * (float) adjustmentFactor;
        event.getInput().leftImpulse = moveImpulses[1] * (float) adjustmentFactor;
    }

    public static void performAutoAttack(Player player, Entity target, Minecraft mc) {
        double distance = CombatUtils.getDistance(player, target);
        double attackRange = CombatUtils.getAttackRange(player);
        double distanceDiff = distance - attackRange;

        boolean isAtCriticalPoint = distanceDiff <= 0;
        boolean shouldHighFrequencyAttack = isAtCriticalPoint ? 
        Config.HIGH_FREQUENCY_ATTACK_AT_CRITICAL_POINT.get() : 
        Config.HIGH_FREQUENCY_ATTACK_NEAR_CRITICAL_POINT.get();

        if (isAtCriticalPoint) {
            if (!Config.AUTO_ATTACK_WITHIN_RANGE.get()) {
                return;
            }
        } else {
            if (!Config.AUTO_ATTACK_AT_CRITICAL_POINT_POSITION.get()) {
                return;
            }
        }

        if (shouldHighFrequencyAttack) {
            mc.gameMode.attack(player, target);
        } else {
            if (player.getAttackStrengthScale(0.0F) >= 1.0F) {
                mc.gameMode.attack(player, target);
            }
        }
    }
}