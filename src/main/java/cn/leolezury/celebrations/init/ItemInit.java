package cn.leolezury.celebrations.init;

import cn.leolezury.celebrations.Celebrations;
import cn.leolezury.celebrations.item.LanternBlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Celebrations.MOD_ID);

    public static final RegistryObject<Item> CHINESE_STYLED_RED_LANTERN = ITEMS.register("chinese_styled_red_lantern", () -> new LanternBlockItem(BlockInit.CHINESE_STYLED_RED_LANTERN.get(), new Item.Properties()));
}
