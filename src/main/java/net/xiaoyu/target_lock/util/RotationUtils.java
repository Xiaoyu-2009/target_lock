package net.xiaoyu.target_lock.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class RotationUtils {

    public static double[] calculateRotation(Vec3 playerPos, Vec3 targetPos) {
        Vec3 direction = targetPos.subtract(playerPos);
        
        double yaw = Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90.0;
        double pitch = -Math.toDegrees(Math.asin(direction.y / direction.length()));
        
        return new double[]{yaw, pitch};
    }

    public static void updatePlayerRotation(Player player, double yaw, double pitch) {
        player.setYRot((float) yaw);
        player.setXRot((float) pitch);
        
        player.yRotO = player.getYRot();
        player.xRotO = player.getXRot();
    }

    public static void updatePlayerRotationToTarget(Player player, Vec3 targetPos) {
        Vec3 playerPos = player.getEyePosition();
        double[] rotations = calculateRotation(playerPos, targetPos);
        updatePlayerRotation(player, rotations[0], rotations[1]);
    }
}