package net.xiaoyu.target_lock.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.xiaoyu.target_lock.Config;
import net.xiaoyu.target_lock.util.client.ClientTargetLockHandler;
import net.xiaoyu.target_lock.util.ServerTargetLockHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
    
    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void onTravel(Vec3 vec3, CallbackInfo ci) {
        Player player = (Player) (Object) this;

        if (Config.TARGET_ENTITY_CAN_MOVE != null && !Config.TARGET_ENTITY_CAN_MOVE.get()) {
            if (player.level().isClientSide && ClientTargetLockHandler.isPlayerLocked()) {
                ci.cancel();
            } else if (!player.level().isClientSide && ServerTargetLockHandler.isEntityLocked(player)) {
                ci.cancel();
            }
        }
    }
}