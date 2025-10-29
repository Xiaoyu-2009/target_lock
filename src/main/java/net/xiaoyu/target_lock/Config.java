package net.xiaoyu.target_lock;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.DoubleValue LOCK_RANGE;
    public static final ModConfigSpec.BooleanValue AUTO_SWITCH_TO_NEXT_TARGET;
    public static final ModConfigSpec.BooleanValue USE_NEAREST_ENTITY_PRIORITY;
    public static final ModConfigSpec.BooleanValue SWITCH_TO_NEXT_TARGET_AFTER_KILL;
    public static final ModConfigSpec.BooleanValue LOCK_ENTITY_POSITION;
    public static final ModConfigSpec.BooleanValue KEEP_ATTACK_RANGE;
    public static final ModConfigSpec.DoubleValue ATTACK_RANGE_ADJUSTMENT_FACTOR;
    public static final ModConfigSpec.BooleanValue AUTO_ATTACK_AT_CRITICAL_POINT;
    public static final ModConfigSpec.BooleanValue AUTO_ATTACK_WITHIN_RANGE;
    public static final ModConfigSpec.BooleanValue AUTO_ATTACK_AT_CRITICAL_POINT_POSITION;
    public static final ModConfigSpec.BooleanValue HIGH_FREQUENCY_ATTACK_AT_CRITICAL_POINT;
    public static final ModConfigSpec.BooleanValue HIGH_FREQUENCY_ATTACK_NEAR_CRITICAL_POINT;
    
    static {
        BUILDER.comment("Target Lock Config").push("target_lock");
        
        LOCK_RANGE = BUILDER
                .comment("The range within which targets can be locked (block)")
                .defineInRange("lockRange", 20.0D, 0.0D, Double.MAX_VALUE);

        AUTO_SWITCH_TO_NEXT_TARGET = BUILDER
                .comment("Whether to automatically switch to the next target entity")
                .define("autoSwitchToNextTarget", true);
        
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
        
        AUTO_ATTACK_AT_CRITICAL_POINT = BUILDER
                .comment("Whether to automatically attack when the target is at critical point")
                .define("autoAttackAtCriticalPoint", false);

        AUTO_ATTACK_WITHIN_RANGE = BUILDER
                .comment("Whether to automatically attack when within attack range")
                .define("autoAttackWithinRange", true);
        
        AUTO_ATTACK_AT_CRITICAL_POINT_POSITION = BUILDER
                .comment("Whether to automatically attack when at critical point position")
                .define("autoAttackAtCriticalPointPosition", true);

        HIGH_FREQUENCY_ATTACK_AT_CRITICAL_POINT = BUILDER
                .comment("Whether to enable high frequency attack when at critical point")
                .define("highFrequencyAttackAtCriticalPoint", false);
        
        HIGH_FREQUENCY_ATTACK_NEAR_CRITICAL_POINT = BUILDER
                .comment("Whether to enable high frequency attack when near critical point")
                .define("highFrequencyAttackNearCriticalPoint", false);
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}