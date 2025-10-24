package net.xiaoyu.target_lock.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.xiaoyu.target_lock.network.NetworkHandler;
import net.xiaoyu.target_lock.network.TargetLockPacket;

import java.util.HashSet;
import java.util.Set;

public class ServerTargetLockHandler {
    private static final Set<Integer> lockedPlayers = new HashSet<>();

    public static void setLockedTarget(ServerPlayer lockingPlayer, ServerPlayer target) {
        lockedPlayers.add(target.getId());
        NetworkHandler.sendToClient(new TargetLockPacket(target.getId(), true), lockingPlayer);
        NetworkHandler.sendToClient(new TargetLockPacket(target.getId(), true), target);
    }

    public static boolean isEntityLocked(Entity entity) {
        if (!(entity instanceof ServerPlayer)) {
            return false;
        }
        return lockedPlayers.contains(entity.getId());
    }

    public static void resetPlayerLock(ServerPlayer player) {
        lockedPlayers.remove(player.getId());
        NetworkHandler.sendToClient(new TargetLockPacket(player.getId(), false), player);
    }

    public static void handleUnlockPacket(ServerPlayer player, int targetId) {
        for (ServerPlayer target : player.server.getPlayerList().getPlayers()) {
            if (target.getId() == targetId) {
                resetPlayerLock(target);
                break;
            }
        }
    }
}