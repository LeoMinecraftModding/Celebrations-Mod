package team.leomc.celebrations.data.gen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.registry.CBlocks;

import java.util.Set;
import java.util.stream.Collectors;

public class CBlockLootSubProvider extends BlockLootSubProvider {
	public CBlockLootSubProvider(HolderLookup.Provider lookup) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookup);
	}

	@Override
	protected void generate() {
		dropSelf(CBlocks.CHINESE_STYLED_BAMBOO_LANTERN.get());
		dropSelf(CBlocks.CHINESE_STYLED_PAPER_LANTERN.get());
		dropSelf(CBlocks.CHINESE_STYLED_RED_LANTERN.get());
		dropSelf(CBlocks.JAPANESE_STYLED_PAPER_LANTERN.get());
		dropSelf(CBlocks.JAPANESE_STYLED_RED_LANTERN.get());
		dropSelf(CBlocks.COUPLET.get());
		dropSelf(CBlocks.HORIZONTAL_SCROLL.get());
		dropSelf(CBlocks.FU_STICKER.get());
		dropSelf(CBlocks.INVERTED_FU_STICKER.get());
		dropSelf(CBlocks.GOLDEN_FU_STICKER.get());
		dropSelf(CBlocks.INVERTED_GOLDEN_FU_STICKER.get());
		dropSelf(CBlocks.FIREWORK_BUNDLE.get());
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return BuiltInRegistries.BLOCK.stream().filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(Celebrations.ID)).collect(Collectors.toList());
	}
}
