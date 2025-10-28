package net.xiaoyu.target_lock;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.DoubleValue LOCK_RANGE;
    public static final ModConfigSpec.BooleanValue USE_NEAREST_ENTITY_PRIORITY;
    public static final ModConfigSpec.BooleanValue SWITCH_TO_NEXT_TARGET_AFTER_KILL;
    public static final ModConfigSpec.BooleanValue LOCK_ENTITY_POSITION;
    public static final ModConfigSpec.BooleanValue KEEP_ATTACK_RANGE;
    public static final ModConfigSpec.DoubleValue ATTACK_RANGE_ADJUSTMENT_FACTOR;
    
    static {
        BUILDER.comment("Target Lock Config").push("target_lock");
        
        LOCK_RANGE = BUILDER
                .comment("The range within which targets can be locked (block)")
                .defineInRange("lockRange", 20.0D, 0.0D, Double.MAX_VALUE);
        
        USE_NEAREST_ENTITY_PRIORITY = BUILDER
                .comment("Whether to always lock onto the nearest entity, even if already locking onto another entity")
                .define("useNearestEntityPriority", false);
        
        SWITCH_TO_NEXT_TARGET_AFTER_KILL = BUILDER
                .comment("Whether to switch to the next nearest target after the current target is kill")
                .define("switchToNextTargetAfterKill", true);
        
        LOCK_ENTITY_POSITION = BUILDER
                .comment("Whether to lock the entity position when targeting")
                .define("lockEntityPosition", false);
        
        KEEP_ATTACK_RANGE = BUILDER
                .comment("Whether to keep the player within attack range of the target")
                .define("keepAttackRange", false);
        
        ATTACK_RANGE_ADJUSTMENT_FACTOR = BUILDER
                .comment("Adjustment strength for maintaining optimal attack distance")
                .defineInRange("attackRangeAdjustmentFactor", 10.0D, 0.0D, Double.MAX_VALUE);
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}