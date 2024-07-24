package team.leomc.celebrations.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.block.*;

public class CBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Celebrations.ID);

	public static final DeferredBlock<CLanternBlock> CHINESE_STYLED_BAMBOO_LANTERN = BLOCKS.register("chinese_styled_bamboo_lantern", () -> new CLanternBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).sound(SoundType.BAMBOO_WOOD).instabreak().lightLevel((state) -> state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT) ? 15 : 0)) {
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
			VoxelShape shape = Shapes.empty();
			shape = Shapes.or(shape, Shapes.create(0.3125, 0, 0.3125, 0.6875, 0.125, 0.6875));
			shape = Shapes.or(shape, Shapes.create(0.1875, 0.125, 0.1875, 0.8125, 0.875, 0.8125));
			shape = Shapes.or(shape, Shapes.create(0.3125, 0.875, 0.3125, 0.6875, 1, 0.6875));
			shape = Shapes.or(shape, Shapes.create(0.4375, 0.125, 0.4375, 0.5625, 0.5, 0.5625));
			shape = Shapes.or(shape, Shapes.create(0.5, 0.5, 0.46875, 0.5, 0.5625, 0.53125));
			shape = Shapes.or(shape, Shapes.create(0.5, 0.5, 0.46875, 0.5, 0.5625, 0.53125));

			return shape;
		}
	});

	public static final DeferredBlock<CLanternBlock> CHINESE_STYLED_PAPER_LANTERN = BLOCKS.register("chinese_styled_paper_lantern", () -> new CLanternBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).sound(SoundType.GRASS).instabreak().lightLevel((state) -> state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT) ? 15 : 0)) {
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
			VoxelShape shape = Shapes.empty();
			shape = Shapes.or(shape, Shapes.create(0.3125, 0, 0.3125, 0.6875, 0.125, 0.6875));
			shape = Shapes.or(shape, Shapes.create(0.1875, 0.125, 0.1875, 0.8125, 0.875, 0.8125));
			shape = Shapes.or(shape, Shapes.create(0.3125, 0.875, 0.3125, 0.6875, 1, 0.6875));
			shape = Shapes.or(shape, Shapes.create(0.4375, 0.125, 0.4375, 0.5625, 0.5, 0.5625));
			shape = Shapes.or(shape, Shapes.create(0.5, 0.5, 0.46875, 0.5, 0.5625, 0.53125));
			shape = Shapes.or(shape, Shapes.create(0.5, 0.5, 0.46875, 0.5, 0.5625, 0.53125));
			shape = Shapes.or(shape, Shapes.create(0.34375, -0.25, 0.5, 0.65625, 0.125, 0.5));
			shape = Shapes.or(shape, Shapes.create(0.34375, -0.25, 0.5, 0.65625, 0.125, 0.5));

			return shape;
		}
	});

	public static final DeferredBlock<CLanternBlock> CHINESE_STYLED_RED_LANTERN = BLOCKS.register("chinese_styled_red_lantern", () -> new CLanternBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).sound(SoundType.WOOL).instabreak().lightLevel((state) -> state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT) ? 15 : 0)) {
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
			VoxelShape shape = Shapes.empty();
			shape = Shapes.or(shape, Shapes.create(0.3125, 0.125, 0.3125, 0.6875, 0.25, 0.6875));
			shape = Shapes.or(shape, Shapes.create(0.125, 0.25, 0.125, 0.875, 0.8125, 0.875));
			shape = Shapes.or(shape, Shapes.create(0.25, 0.8125, 0.25, 0.75, 0.9375, 0.75));
			shape = Shapes.or(shape, Shapes.create(0.40625, 0.9375, 0.5, 0.59375, 1.0625, 0.5));
			shape = Shapes.or(shape, Shapes.create(0.40625, 0.9375, 0.5, 0.59375, 1.0625, 0.5));
			shape = Shapes.or(shape, Shapes.create(0.34375, -0.25, 0.5, 0.65625, 0.125, 0.5));
			shape = Shapes.or(shape, Shapes.create(0.34375, -0.25, 0.5, 0.65625, 0.125, 0.5));

			return shape;
		}
	});

	public static final DeferredBlock<CLanternBlock> JAPANESE_STYLED_PAPER_LANTERN = BLOCKS.register("japanese_styled_paper_lantern", () -> new CLanternBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).sound(SoundType.GRASS).instabreak().lightLevel((state) -> state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT) ? 15 : 0)) {
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
			VoxelShape shape = Shapes.empty();
			shape = Shapes.or(shape, Shapes.create(0.3125, 0, 0.3125, 0.6875, 0.125, 0.6875));
			shape = Shapes.or(shape, Shapes.create(0.1875, 0.125, 0.1875, 0.8125, 0.875, 0.8125));
			shape = Shapes.or(shape, Shapes.create(0.3125, 0.875, 0.3125, 0.6875, 1, 0.6875));

			return shape;
		}
	});

	public static final DeferredBlock<CLanternBlock> JAPANESE_STYLED_RED_LANTERN = BLOCKS.register("japanese_styled_red_lantern", () -> new CLanternBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).sound(SoundType.WOOL).instabreak().lightLevel((state) -> state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT) ? 15 : 0)) {
		@Override
		public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
			VoxelShape shape = Shapes.empty();
			shape = Shapes.or(shape, Shapes.create(0.3125, 0, 0.3125, 0.6875, 0.125, 0.6875));
			shape = Shapes.or(shape, Shapes.create(0.1875, 0.125, 0.1875, 0.8125, 0.875, 0.8125));
			shape = Shapes.or(shape, Shapes.create(0.3125, 0.875, 0.3125, 0.6875, 1, 0.6875));

			return shape;
		}
	});

	public static final DeferredBlock<CoupletBlock> COUPLET = BLOCKS.register("couplet", () -> new CoupletBlock(BlockBehaviour.Properties.of().noCollission().mapColor(MapColor.COLOR_RED).sound(SoundType.AZALEA_LEAVES).instabreak()));
	public static final DeferredBlock<HorizontalScrollBlock> HORIZONTAL_SCROLL = BLOCKS.register("horizontal_scroll", () -> new HorizontalScrollBlock(BlockBehaviour.Properties.of().noCollission().mapColor(MapColor.COLOR_RED).sound(SoundType.AZALEA_LEAVES).instabreak()));
	public static final DeferredBlock<FuStickerBlock> FU_STICKER = BLOCKS.register("fu_sticker", () -> new FuStickerBlock(BlockBehaviour.Properties.of().noCollission().mapColor(MapColor.COLOR_RED).sound(SoundType.AZALEA_LEAVES).instabreak()));
	public static final DeferredBlock<FuStickerBlock> INVERTED_FU_STICKER = BLOCKS.register("inverted_fu_sticker", () -> new FuStickerBlock(BlockBehaviour.Properties.of().noCollission().mapColor(MapColor.COLOR_RED).sound(SoundType.AZALEA_LEAVES).instabreak()));
	public static final DeferredBlock<FuStickerBlock> GOLDEN_FU_STICKER = BLOCKS.register("golden_fu_sticker", () -> new FuStickerBlock(BlockBehaviour.Properties.of().noCollission().mapColor(MapColor.COLOR_RED).sound(SoundType.AZALEA_LEAVES).instabreak()));
	public static final DeferredBlock<FuStickerBlock> INVERTED_GOLDEN_FU_STICKER = BLOCKS.register("inverted_golden_fu_sticker", () -> new FuStickerBlock(BlockBehaviour.Properties.of().noCollission().mapColor(MapColor.COLOR_RED).sound(SoundType.AZALEA_LEAVES).instabreak()));
	public static final DeferredBlock<FireworkBundleBlock> FIREWORK_BUNDLE = BLOCKS.register("firework_bundle", () -> new FireworkBundleBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT).mapColor(MapColor.COLOR_BROWN)));
}
