package cn.leolezury.celebrations.block;

import cn.leolezury.celebrations.block.entity.CoupletBlockEntity;
import cn.leolezury.celebrations.block.entity.HorizontalScrollBlockEntity;
import cn.leolezury.celebrations.init.BlockEntityInit;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HorizontalScrollBlock extends WallSignBlock {
    private String descriptionId;

    public HorizontalScrollBlock(Properties properties) {
        super(properties, WoodType.OAK);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HorizontalScrollBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityInit.HORIZONTAL_SCROLL.get(), CoupletBlockEntity::tick);
    }

    @Override
    public @NotNull String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("block", BuiltInRegistries.BLOCK.getKey(this));
        }

        return this.descriptionId;
    }
}
