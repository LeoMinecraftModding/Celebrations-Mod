package cn.leolezury.celebrations.util;

import cn.leolezury.celebrations.Celebrations;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CBTags {
    public static class Items {
        public static final TagKey<Item> IGNITERS = ItemTags.create(new ResourceLocation(Celebrations.MOD_ID, "igniters"));
    }
}
