package team.leomc.celebrations.util;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import team.leomc.celebrations.Celebrations;

public class CTags {
	public static class Items {
		public static final TagKey<Item> IGNITERS = create("igniters");
		public static final TagKey<Item> PARTY_HAT_PATTERN_INGREDIENTS = create("party_hat_pattern_ingredients");
		public static final TagKey<Item> PARTY_HAT_STRIPES_INGREDIENTS = create("party_hat_stripes_ingredients");
		public static final TagKey<Item> PARTY_HAT_TILT_STRIPES_INGREDIENTS = create("party_hat_tilt_stripes_ingredients");
		public static final TagKey<Item> PARTY_HAT_DOTS_INGREDIENTS = create("party_hat_dots_ingredients");

		private static TagKey<Item> create(String id) {
			return ItemTags.create(Celebrations.id(id));
		}
	}
}
