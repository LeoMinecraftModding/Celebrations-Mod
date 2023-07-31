package cn.leolezury.celebrations.init;

import cn.leolezury.celebrations.Celebrations;
import cn.leolezury.celebrations.item.CoupletItem;
import cn.leolezury.celebrations.item.LanternBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Celebrations.MOD_ID);
    public static final RegistryObject<Item> CHINESE_STYLED_BAMBOO_LANTERN = ITEMS.register("chinese_styled_bamboo_lantern", () -> new LanternBlockItem(BlockInit.CHINESE_STYLED_BAMBOO_LANTERN.get(), new Item.Properties()));
    public static final RegistryObject<Item> CHINESE_STYLED_PAPER_LANTERN = ITEMS.register("chinese_styled_paper_lantern", () -> new LanternBlockItem(BlockInit.CHINESE_STYLED_PAPER_LANTERN.get(), new Item.Properties()));
    public static final RegistryObject<Item> CHINESE_STYLED_RED_LANTERN = ITEMS.register("chinese_styled_red_lantern", () -> new LanternBlockItem(BlockInit.CHINESE_STYLED_RED_LANTERN.get(), new Item.Properties()));
    public static final RegistryObject<Item> JAPANESE_STYLED_PAPER_LANTERN = ITEMS.register("japanese_styled_paper_lantern", () -> new LanternBlockItem(BlockInit.JAPANESE_STYLED_PAPER_LANTERN.get(), new Item.Properties()));
    public static final RegistryObject<Item> JAPANESE_STYLED_RED_LANTERN = ITEMS.register("japanese_styled_red_lantern", () -> new LanternBlockItem(BlockInit.JAPANESE_STYLED_RED_LANTERN.get(), new Item.Properties()));
    public static final RegistryObject<Item> COUPLET = ITEMS.register("couplet", () -> new CoupletItem(BlockInit.COUPLET.get(), new Item.Properties()));
    public static final RegistryObject<Item> HORIZONTAL_SCROLL = ITEMS.register("horizontal_scroll", () -> new CoupletItem(BlockInit.HORIZONTAL_SCROLL.get(), new Item.Properties()));
}
