package net.xiaoyu.target_lock.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.PacketDistributor;

public class NetworkHandler {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playBidirectional(
            TargetLockPacket.TYPE,
            TargetLockPacket.STREAM_CODEC,
            TargetLockPacket::handle
        );
    }

    public static void sendToServer(TargetLockPacket packet) {
        PacketDistributor.sendToServer(packet);
    }

    public static void sendToClient(TargetLockPacket packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }
}