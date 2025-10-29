package net.xiaoyu.target_lock.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

public class CombatUtils {

    public static double getDistance(Player player, Entity target) {
        Vec3 playerPos = player.getEyePosition();
        Vec3 targetPos = target.getEyePosition();
        return playerPos.distanceTo(targetPos);
    }

    public static double getAttackRange(Player player) {
        return player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
    }

    public static double getDistanceDiff(Player player, Entity target) {
        double distance = getDistance(player, target);
        double attackRange = getAttackRange(player);
        return distance - attackRange;
    }

    public static Vec3 getHorizontalDirectionToTarget(Player player, Entity target) {
        Vec3 playerPos = player.getEyePosition();
        Vec3 targetPos = target.getEyePosition();
        Vec3 directionToTarget = targetPos.subtract(playerPos);
        return new Vec3(directionToTarget.x, 0, directionToTarget.z).normalize();
    }

    public static Vec3 getMoveDirection(Vec3 directionToTarget, double distanceDiff) {
        return distanceDiff > 0 ? directionToTarget : directionToTarget.reverse();
    }

    public static float[] calculateMoveImpulse(Vec3 moveDirection, float playerYaw) {
        double yawRad = Math.toRadians(playerYaw);
        double sinYaw = Math.sin(yawRad);
        double cosYaw = Math.cos(yawRad);
        
        float forwardImpulse = (float) (moveDirection.x * (-sinYaw) + moveDirection.z * cosYaw);
        float leftImpulse = (float) (moveDirection.x * cosYaw + moveDirection.z * sinYaw);
        
        return new float[]{forwardImpulse, leftImpulse};
    }
}