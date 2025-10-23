package net.xiaoyu.target_lock.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class EntityUtils {

    public static Entity findNearestEntity(Minecraft mc, double range, Entity excludeEntity, Predicate<Entity> filter) {
        if (mc.player == null || mc.level == null) return null;

        Entity nearestEntity = null;
        double nearestDistance = Double.MAX_VALUE;
        Vec3 playerPos = mc.player.getEyePosition();
        double rangeSquared = range * range;

        for (Entity entity : mc.level.entitiesForRendering()) {
            if ((excludeEntity != null && entity == excludeEntity) || entity == mc.player || !entity.isAlive()) continue;
            if (filter != null && !filter.test(entity)) continue;

            double distance = entity.distanceToSqr(playerPos);

            if (distance <= rangeSquared) {
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestEntity = entity;
                }
            }
        }

        return nearestEntity;
    }

    public static Entity findNearestLivingEntity(Minecraft mc, double range, Entity excludeEntity) {
        return findNearestEntity(mc, range, excludeEntity, entity -> entity instanceof LivingEntity);
    }

    public static Entity findNearestLivingEntity(Minecraft mc, double range) {
        return findNearestEntity(mc, range, null, entity -> entity instanceof LivingEntity);
    }
}