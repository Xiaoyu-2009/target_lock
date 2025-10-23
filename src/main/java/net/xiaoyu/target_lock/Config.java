package net.xiaoyu.target_lock;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.DoubleValue LOCK_RANGE;
    public static final ModConfigSpec.BooleanValue USE_NEAREST_ENTITY_PRIORITY;
    public static final ModConfigSpec.BooleanValue SWITCH_TO_NEXT_TARGET_AFTER_KILL;
    
    static {
        BUILDER.comment("Target Lock Config").push("target_lock");
        
        LOCK_RANGE = BUILDER
                .comment("The range within which targets can be locked (block)")
                .translation("config.target_lock.lock_range")
                .defineInRange("lockRange", 20.0D, 0.0D, Double.MAX_VALUE);
        
        USE_NEAREST_ENTITY_PRIORITY = BUILDER
                .comment("Whether to always lock onto the nearest entity, even if already locking onto another entity")
                .translation("config.target_lock.use_nearest_entity_priority")
                .define("useNearestEntityPriority", false);
        
        SWITCH_TO_NEXT_TARGET_AFTER_KILL = BUILDER
                .comment("Whether to switch to the next nearest target after the current target is killed")
                .translation("config.target_lock.switch_to_next_target_after_kill")
                .define("switchToNextTargetAfterKill", true);
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}