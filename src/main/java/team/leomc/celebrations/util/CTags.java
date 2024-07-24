package team.leomc.celebrations.util;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import team.leomc.celebrations.Celebrations;

public class CTags {
	public static class Items {
		public static final TagKey<Item> IGNITERS = ItemTags.create(Celebrations.id("igniters"));
	}
}
