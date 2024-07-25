package team.leomc.celebrations.data.gen.model;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.registry.CBlocks;

public class CBlockStateProvider extends BlockStateProvider {
	// render types
	private static final ResourceLocation SOLID = ResourceLocation.withDefaultNamespace("solid");
	private static final ResourceLocation CUTOUT = ResourceLocation.withDefaultNamespace("cutout");
	private static final ResourceLocation CUTOUT_MIPPED = ResourceLocation.withDefaultNamespace("cutout_mipped");
	private static final ResourceLocation TRANSLUCENT = ResourceLocation.withDefaultNamespace("translucent");

	public CBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, Celebrations.ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleExisting(CBlocks.CHINESE_STYLED_BAMBOO_LANTERN.get());
		simpleExisting(CBlocks.CHINESE_STYLED_PAPER_LANTERN.get());
		simpleExisting(CBlocks.CHINESE_STYLED_RED_LANTERN.get());
		simpleExisting(CBlocks.JAPANESE_STYLED_PAPER_LANTERN.get());
		simpleExisting(CBlocks.JAPANESE_STYLED_RED_LANTERN.get());
		particleOnly(CBlocks.COUPLET.get());
		particleOnly(CBlocks.HORIZONTAL_SCROLL.get());
		particleOnly(CBlocks.FU_STICKER.get());
		particleOnly(CBlocks.INVERTED_FU_STICKER.get());
		particleOnly(CBlocks.GOLDEN_FU_STICKER.get());
		particleOnly(CBlocks.INVERTED_GOLDEN_FU_STICKER.get());
		cubeBottomTop(CBlocks.FIREWORK_BUNDLE.get());
	}

	private void cubeBottomTop(Block block) {
		ModelFile modelFile = models().cubeBottomTop(name(block), blockTexture(block).withSuffix("_side"), blockTexture(block).withSuffix("_bottom"), blockTexture(block).withSuffix("_top"));
		simpleBlock(block, modelFile);
	}

	private void simpleExisting(Block block) {
		simpleBlock(block, new ModelFile.UncheckedModelFile(key(block).withPrefix(ModelProvider.BLOCK_FOLDER + "/")));
	}

	private void particleOnly(Block block) {
		particleOnly(block, blockTexture(block));
	}

	private void particleOnly(Block block, ResourceLocation location) {
		simpleBlock(block, models().getBuilder(name(block)).texture("particle", location));
	}

	private ResourceLocation key(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block);
	}

	private String name(Block block) {
		return key(block).getPath();
	}
}
