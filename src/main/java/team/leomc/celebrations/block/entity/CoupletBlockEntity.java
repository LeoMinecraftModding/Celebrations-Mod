package team.leomc.celebrations.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import team.leomc.celebrations.registry.CBlockEntities;

public class CoupletBlockEntity extends SignLikeBlockEntity {
	public CoupletBlockEntity(BlockPos pos, BlockState state) {
		super(CBlockEntities.COUPLET.get(), pos, state);
	}
}
