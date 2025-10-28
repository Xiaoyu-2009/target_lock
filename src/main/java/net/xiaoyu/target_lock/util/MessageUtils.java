package net.xiaoyu.target_lock.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class MessageUtils {

    public static void showLockSuccessMessage(Minecraft mc, Entity target) {
        if (mc.player != null) {
            mc.player.displayClientMessage(Component.translatable("message.target_lock.lock_success", target.getName().getString()), true);
        }
    }

    public static void showNoTargetsMessage(Minecraft mc) {
        if (mc.player != null) {
            mc.player.displayClientMessage(Component.translatable("message.target_lock.no_targets"), true);
        }
    }

    public static void showSwitchTargetMessage(Minecraft mc, Entity target) {
        if (mc.player != null) {
            mc.player.displayClientMessage(Component.translatable("message.target_lock.switch_target", target.getName().getString()), true);
        }
    }

    public static void showUnlockMessage(Minecraft mc) {
        if (mc.player != null) {
            mc.player.displayClientMessage(Component.translatable("message.target_lock.unlock"), true);
        }
    }

    public static void showTargetKilledMessage(Minecraft mc) {
        if (mc.player != null) {
            mc.player.displayClientMessage(Component.translatable("message.target_lock.target_killed"), true);
        }
    }

    public static void showTargetLostMessage(Minecraft mc) {
        if (mc.player != null) {
            mc.player.displayClientMessage(Component.translatable("message.target_lock.target_lost"), true);
        }
    }

    public static void showSwitchToNextTargetMessage(Minecraft mc, Entity target) {
        if (mc.player != null) {
            mc.player.displayClientMessage(Component.translatable("message.target_lock.switch_to_next_target", target.getName().getString()), true);
        }
    }

    public static void showNoNextTargetMessage(Minecraft mc) {
        if (mc.player != null) {
            mc.player.displayClientMessage(Component.translatable("message.target_lock.no_next_target"), true);
        }
    }
}