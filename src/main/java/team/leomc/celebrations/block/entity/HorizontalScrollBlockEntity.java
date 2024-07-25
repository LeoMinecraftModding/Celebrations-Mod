package team.leomc.celebrations.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import team.leomc.celebrations.registry.CBlockEntities;

public class HorizontalScrollBlockEntity extends SignLikeBlockEntity {
	public HorizontalScrollBlockEntity(BlockPos pos, BlockState state) {
		super(CBlockEntities.HORIZONTAL_SCROLL.get(), pos, state);
	}

	@Override
	public int getMaxTextLineWidth() {
		return 40;
	}

	@Override
	public int getMaxTextLineNumber() {
		return 1;
	}
}
