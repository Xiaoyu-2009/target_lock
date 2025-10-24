package net.xiaoyu.target_lock.util.client;

import net.minecraft.client.Minecraft;
import net.xiaoyu.target_lock.network.TargetLockPacket;

public class ClientTargetLockHandler {
    private static boolean isLocked = false;

    public static void handleTargetLockPacket(TargetLockPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        
        if (packet.targetId() == mc.player.getId()) {
            isLocked = packet.isLocked();
        }
    }

    public static boolean isPlayerLocked() {
        return isLocked;
    }
}