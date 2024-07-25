package team.leomc.celebrations.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import team.leomc.celebrations.block.FireworkBundleBlock;
import team.leomc.celebrations.registry.CDataComponents;

public class FireworkBundleItem extends BlockItem {
	public FireworkBundleItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Nullable
	@Override
	protected BlockState getPlacementState(BlockPlaceContext context) {
		BlockState state = super.getPlacementState(context);
		if (state != null && state.hasProperty(FireworkBundleBlock.FIREWORK_AMOUNT)) {
			return state.setValue(FireworkBundleBlock.FIREWORK_AMOUNT, context.getItemInHand().getOrDefault(CDataComponents.FIREWORK_AMOUNT.get(), 0));
		}
		return state;
	}
}
