package team.leomc.celebrations.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.entity.Balloon;
import team.leomc.celebrations.entity.FuSticker;

public class CEntities {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Celebrations.ID);

	public static final DeferredHolder<EntityType<?>, EntityType<Balloon>> BALLOON = ENTITY_TYPES.register("balloon", () -> EntityType.Builder.of(Balloon::new, MobCategory.MISC).sized(0.5f, 0.75f).clientTrackingRange(10).updateInterval(1).build(Celebrations.id("balloon").toString()));
	public static final DeferredHolder<EntityType<?>, EntityType<FuSticker>> FU_STICKER = ENTITY_TYPES.register("fu_sticker", () -> EntityType.Builder.of(FuSticker::new, MobCategory.MISC).sized(0.5f, 0.75f).clientTrackingRange(10).updateInterval(1).build(Celebrations.id("fu_sticker").toString()));
}
