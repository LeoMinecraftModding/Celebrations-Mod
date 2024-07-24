package team.leomc.celebrations.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.block.entity.*;

public class CBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Celebrations.ID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LanternBlockEntity>> LANTERN = BLOCK_ENTITIES.register("lantern", () -> BlockEntityType.Builder.of(LanternBlockEntity::new, CBlocks.CHINESE_STYLED_BAMBOO_LANTERN.get(), CBlocks.CHINESE_STYLED_PAPER_LANTERN.get(), CBlocks.CHINESE_STYLED_RED_LANTERN.get(), CBlocks.JAPANESE_STYLED_PAPER_LANTERN.get(), CBlocks.JAPANESE_STYLED_RED_LANTERN.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CoupletBlockEntity>> COUPLET = BLOCK_ENTITIES.register("couplet", () -> BlockEntityType.Builder.of(CoupletBlockEntity::new, CBlocks.COUPLET.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HorizontalScrollBlockEntity>> HORIZONTAL_SCROLL = BLOCK_ENTITIES.register("horizontal_scroll", () -> BlockEntityType.Builder.of(HorizontalScrollBlockEntity::new, CBlocks.HORIZONTAL_SCROLL.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FuStickerBlockEntity>> FU_STICKER = BLOCK_ENTITIES.register("fu_sticker", () -> BlockEntityType.Builder.of(FuStickerBlockEntity::new, CBlocks.FU_STICKER.get(), CBlocks.INVERTED_FU_STICKER.get(), CBlocks.GOLDEN_FU_STICKER.get(), CBlocks.INVERTED_GOLDEN_FU_STICKER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FireworkBundleBlockEntity>> FIREWORK_BUNDLE = BLOCK_ENTITIES.register("firework_bundle", () -> BlockEntityType.Builder.of(FireworkBundleBlockEntity::new, CBlocks.FIREWORK_BUNDLE.get()).build(null));
}
