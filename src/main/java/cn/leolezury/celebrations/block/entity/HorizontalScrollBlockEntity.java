package cn.leolezury.celebrations.block.entity;

import cn.leolezury.celebrations.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HorizontalScrollBlockEntity extends SignBlockEntity {
    public HorizontalScrollBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.HORIZONTAL_SCROLL.get(), pos, state);
    }
}
