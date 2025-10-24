package net.xiaoyu.target_lock.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.xiaoyu.target_lock.Config;
import net.xiaoyu.target_lock.client.TargetLockHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "setPos(DDD)V", at = @At("HEAD"), cancellable = true)
    private void onSetPosDouble(double x, double y, double z, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;

        if (Config.TARGET_ENTITY_CAN_MOVE != null && !Config.TARGET_ENTITY_CAN_MOVE.get() && 
            TargetLockHandler.isEntityLocked(entity)) {
            ci.cancel();
        }
    }
    
    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void onMove(MoverType moverType, Vec3 movement, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;

        if (Config.TARGET_ENTITY_CAN_MOVE != null && !Config.TARGET_ENTITY_CAN_MOVE.get() && 
            TargetLockHandler.isEntityLocked(entity)) {
            ci.cancel();
        }
    }
}