package team.leomc.celebrations.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CCommonConfig {
	public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	public static final ModConfigSpec SPEC;

	public static final ModConfigSpec.ConfigValue<Integer> CELEBRATION_INTERVAL;
	public static final ModConfigSpec.ConfigValue<Integer> CELEBRATION_DURATION;

	public static final ModConfigSpec.ConfigValue<Double> LANTERN_EFFECT_RADIUS;
	public static final ModConfigSpec.ConfigValue<Boolean> GIVE_NON_ENEMY_BENEFICIAL_EFFECT;
	public static final ModConfigSpec.ConfigValue<Boolean> GIVE_NON_ENEMY_HARMFUL_EFFECT;
	public static final ModConfigSpec.ConfigValue<Boolean> GIVE_ENEMY_BENEFICIAL_EFFECT;
	public static final ModConfigSpec.ConfigValue<Boolean> GIVE_ENEMY_HARMFUL_EFFECT;

	static {
		BUILDER.push("celebration");
		CELEBRATION_INTERVAL = BUILDER.comment("The interval of the villagers' celebrations, in ticks (1 sec = 20 ticks)").define("celebrationInterval", 12000 * 2 * 30);
		CELEBRATION_DURATION = BUILDER.comment("The duration of the villagers' celebrations, in ticks (1 sec = 20 ticks)").define("celebrationDuration", 12000 * 2 * 2);
		BUILDER.pop();
		BUILDER.push("lantern");
		LANTERN_EFFECT_RADIUS = BUILDER.comment("This parameter determines the range of action of the lantern's potion effect").define("effectRange", 10d);
		GIVE_NON_ENEMY_BENEFICIAL_EFFECT = BUILDER.comment("Enables lanterns with potion effects to give beneficial effects to non-enemy mobs").define("giveNonEnemyBeneficialEffect", true);
		GIVE_NON_ENEMY_HARMFUL_EFFECT = BUILDER.comment("Enables lanterns with potion effects to give harmful effects to non-enemy mobs").define("giveNonEnemyHarmfulEffect", false);
		GIVE_ENEMY_BENEFICIAL_EFFECT = BUILDER.comment("Enables lanterns with potion effects to give beneficial effects to enemy mobs").define("giveEnemyBeneficialEffect", false);
		GIVE_ENEMY_HARMFUL_EFFECT = BUILDER.comment("Enables lanterns with potion effects to give harmful effects to enemy mobs").define("giveEnemyHarmfulEffect", true);
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
}
