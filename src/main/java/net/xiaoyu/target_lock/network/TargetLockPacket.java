package net.xiaoyu.target_lock.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.xiaoyu.target_lock.TargetLock;
import net.xiaoyu.target_lock.util.client.ClientTargetLockHandler;
import net.xiaoyu.target_lock.util.ServerTargetLockHandler;

public record TargetLockPacket(int targetId, boolean isLocked) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TargetLock.MOD_ID, "target_lock_packet");
    public static final Type<TargetLockPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, TargetLockPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        TargetLockPacket::targetId,
        ByteBufCodecs.BOOL,
        TargetLockPacket::isLocked,
        TargetLockPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.flow().isClientbound()) {
                ClientTargetLockHandler.handleTargetLockPacket(this);
            } else {
                if (context.player() instanceof ServerPlayer player) {
                    if (this.isLocked) {
                        if (player.level().getEntity(this.targetId) instanceof ServerPlayer targetPlayer) {
                            ServerTargetLockHandler.setLockedTarget(player, targetPlayer);
                        }
                    } else {
                        ServerTargetLockHandler.handleUnlockPacket(player, this.targetId);
                    }
                }
            }
        });
    }
}