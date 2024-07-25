package team.leomc.celebrations.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.item.CoupletItem;
import team.leomc.celebrations.item.FireworkBundleItem;
import team.leomc.celebrations.item.LanternItem;

public class CItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Celebrations.ID);
	public static final DeferredItem<LanternItem> CHINESE_STYLED_BAMBOO_LANTERN = ITEMS.register("chinese_styled_bamboo_lantern", () -> new LanternItem(CBlocks.CHINESE_STYLED_BAMBOO_LANTERN.get(), new Item.Properties()));
	public static final DeferredItem<LanternItem> CHINESE_STYLED_PAPER_LANTERN = ITEMS.register("chinese_styled_paper_lantern", () -> new LanternItem(CBlocks.CHINESE_STYLED_PAPER_LANTERN.get(), new Item.Properties()));
	public static final DeferredItem<LanternItem> CHINESE_STYLED_RED_LANTERN = ITEMS.register("chinese_styled_red_lantern", () -> new LanternItem(CBlocks.CHINESE_STYLED_RED_LANTERN.get(), new Item.Properties()));
	public static final DeferredItem<LanternItem> JAPANESE_STYLED_PAPER_LANTERN = ITEMS.register("japanese_styled_paper_lantern", () -> new LanternItem(CBlocks.JAPANESE_STYLED_PAPER_LANTERN.get(), new Item.Properties()));
	public static final DeferredItem<LanternItem> JAPANESE_STYLED_RED_LANTERN = ITEMS.register("japanese_styled_red_lantern", () -> new LanternItem(CBlocks.JAPANESE_STYLED_RED_LANTERN.get(), new Item.Properties()));
	public static final DeferredItem<CoupletItem> COUPLET = ITEMS.register("couplet", () -> new CoupletItem(CBlocks.COUPLET.get(), new Item.Properties()));
	public static final DeferredItem<CoupletItem> HORIZONTAL_SCROLL = ITEMS.register("horizontal_scroll", () -> new CoupletItem(CBlocks.HORIZONTAL_SCROLL.get(), new Item.Properties()));
	public static final DeferredItem<BlockItem> FU_STICKER = ITEMS.register("fu_sticker", () -> new BlockItem(CBlocks.FU_STICKER.get(), new Item.Properties()));
	public static final DeferredItem<BlockItem> INVERTED_FU_STICKER = ITEMS.register("inverted_fu_sticker", () -> new BlockItem(CBlocks.INVERTED_FU_STICKER.get(), new Item.Properties()));
	public static final DeferredItem<BlockItem> GOLDEN_FU_STICKER = ITEMS.register("golden_fu_sticker", () -> new BlockItem(CBlocks.GOLDEN_FU_STICKER.get(), new Item.Properties()));
	public static final DeferredItem<BlockItem> INVERTED_GOLDEN_FU_STICKER = ITEMS.register("inverted_golden_fu_sticker", () -> new BlockItem(CBlocks.INVERTED_GOLDEN_FU_STICKER.get(), new Item.Properties()));
	public static final DeferredItem<FireworkBundleItem> FIREWORK_BUNDLE = ITEMS.register("firework_bundle", () -> new FireworkBundleItem(CBlocks.FIREWORK_BUNDLE.get(), new Item.Properties()));
	public static final DeferredItem<Item> RED_PAPER = ITEMS.register("red_paper", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> GOLD_POWDER = ITEMS.register("gold_powder", () -> new DyeItem(DyeColor.YELLOW, new Item.Properties()));
}
