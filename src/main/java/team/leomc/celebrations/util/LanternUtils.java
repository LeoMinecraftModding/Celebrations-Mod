package team.leomc.celebrations.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import team.leomc.celebrations.item.LanternItem;

public class LanternUtils {
	public static ItemStack getVillagerLanternItem(Villager villager) {
		if (villager.getPersistentData().get("CLantern") instanceof CompoundTag tag) {
			return ItemStack.parse(villager.registryAccess(), tag).orElse(ItemStack.EMPTY);
		} else return ItemStack.EMPTY;
	}

	public static Block getVillagerLanternBlock(Villager villager) {
		if (getVillagerLanternItem(villager).getItem() instanceof LanternItem blockItem) {
			return blockItem.getBlock();
		} else return Blocks.AIR;
	}

	public static void setVillagerLantern(Villager villager, ItemStack lantern) {
		Tag tag = lantern.save(villager.registryAccess());
		villager.getPersistentData().put("CLantern", tag);
	}
}
