package cn.leolezury.celebrations.block.entity;

import cn.leolezury.celebrations.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FuStickerBlockEntity extends BlockEntity {
    public FuStickerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.FU_STICKER.get(), pos, state);
    }
}
