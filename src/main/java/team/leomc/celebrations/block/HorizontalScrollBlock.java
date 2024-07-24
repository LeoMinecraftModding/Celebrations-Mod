package team.leomc.celebrations.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import team.leomc.celebrations.block.entity.HorizontalScrollBlockEntity;

public class HorizontalScrollBlock extends WallSignLikeBlock {
	public static final MapCodec<HorizontalScrollBlock> CODEC = simpleCodec(HorizontalScrollBlock::new);

	public HorizontalScrollBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new HorizontalScrollBlockEntity(pos, state);
	}
}
