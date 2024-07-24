package team.leomc.celebrations.item;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;

public class CoupletItem extends SignItem {
	public CoupletItem(Block block, Item.Properties properties) {
		super(properties, block, block, Direction.UP);
	}
}
