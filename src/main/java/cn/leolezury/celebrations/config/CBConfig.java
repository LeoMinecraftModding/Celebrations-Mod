package cn.leolezury.celebrations.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CBConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> CELEBRATION_INTERVAL;
    public static final ForgeConfigSpec.ConfigValue<Integer> CELEBRATION_DURATION;

    public static final ForgeConfigSpec.ConfigValue<Double> LANTERN_EFFECT_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GIVE_NON_ENEMY_BENEFICIAL_EFFECT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GIVE_NON_ENEMY_HARMFUL_EFFECT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GIVE_ENEMY_BENEFICIAL_EFFECT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GIVE_ENEMY_HARMFUL_EFFECT;
    static {
        BUILDER.push("celebration");
        CELEBRATION_INTERVAL = BUILDER.comment("The interval of the villagers' celebrations, in ticks (1 sec = 20 ticks)").define("celebrationInterval", 12000 * 2 * 30);
        CELEBRATION_DURATION = BUILDER.comment("The duration of the villagers' celebrations, in ticks (1 sec = 20 ticks)").define("celebrationDuration", 12000 * 2 * 2);
        BUILDER.pop();
        BUILDER.push("lantern");
        LANTERN_EFFECT_RADIUS = BUILDER.comment("This parameter determines the range of action of the lantern's potion effect").define("effectRange", 10d);
        GIVE_NON_ENEMY_BENEFICIAL_EFFECT = BUILDER.comment("Enable lanterns with potion effects to give beneficial effects to non-enemy mobs.").define("giveNonEnemyBeneficialEffect", true);
        GIVE_NON_ENEMY_HARMFUL_EFFECT = BUILDER.comment("Enable lanterns with potion effects to give harmful effects to non-enemy mobs.").define("giveNonEnemyHarmfulEffect", false);
        GIVE_ENEMY_BENEFICIAL_EFFECT = BUILDER.comment("Enable lanterns with potion effects to give beneficial effects to enemy mobs.").define("giveEnemyBeneficialEffect", false);
        GIVE_ENEMY_HARMFUL_EFFECT = BUILDER.comment("Enable lanterns with potion effects to give harmful effects to enemy mobs.").define("giveEnemyHarmfulEffect", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
