package cn.leolezury.celebrations.item;

import cn.leolezury.celebrations.block.FireworkBundleBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FireworkBundleBlockItem extends BlockItem {
    public FireworkBundleBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if (state != null && state.hasProperty(FireworkBundleBlock.FIREWORK_AMOUNT)) {
            return super.getPlacementState(context).setValue(FireworkBundleBlock.FIREWORK_AMOUNT, context.getItemInHand().getOrCreateTag().getInt("FireworkAmount"));
        }
        return state;
    }
}
