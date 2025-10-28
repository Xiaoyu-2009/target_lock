package net.xiaoyu.target_lock;

import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import com.mojang.blaze3d.platform.InputConstants;

@EventBusSubscriber(modid = TargetLock.MOD_ID)
public class ModKeyBindings {
    public static final KeyMapping TARGET_LOCK_KEY = new KeyMapping(
        "key.target_lock.lock_target",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        InputConstants.KEY_G,
        "key.categories.gameplay"
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TARGET_LOCK_KEY);
    }
}