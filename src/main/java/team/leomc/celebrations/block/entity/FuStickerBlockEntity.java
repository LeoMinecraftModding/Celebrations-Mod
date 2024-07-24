package team.leomc.celebrations.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import team.leomc.celebrations.registry.CBlockEntities;

public class FuStickerBlockEntity extends BlockEntity {
	public FuStickerBlockEntity(BlockPos pos, BlockState state) {
		super(CBlockEntities.FU_STICKER.get(), pos, state);
	}
}
