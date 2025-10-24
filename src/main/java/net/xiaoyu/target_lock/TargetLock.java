package net.xiaoyu.target_lock;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;

@Mod(TargetLock.MOD_ID)
public class TargetLock {
    public static final String MOD_ID = "target_lock";
    
    public TargetLock(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }
}