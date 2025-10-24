package net.xiaoyu.target_lock;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.xiaoyu.target_lock.network.NetworkHandler;

@Mod(TargetLock.MOD_ID)
public class TargetLock {
    public static final String MOD_ID = "target_lock";
    
    public TargetLock(ModContainer container, IEventBus modEventBus) {
        container.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        modEventBus.addListener(NetworkHandler::register);
    }
}