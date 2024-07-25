package team.leomc.celebrations.data.gen.lang;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.registry.CBlocks;
import team.leomc.celebrations.registry.CItems;

public class CEnglishLanguageProvider extends LanguageProvider {
	public CEnglishLanguageProvider(PackOutput output) {
		super(output, Celebrations.ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add("name." + Celebrations.ID, "Celebrations");
		add(CBlocks.CHINESE_STYLED_BAMBOO_LANTERN.get(), "Chinese Styled Bamboo Lantern");
		add(CBlocks.CHINESE_STYLED_PAPER_LANTERN.get(), "Chinese Styled Paper Lantern");
		add(CBlocks.CHINESE_STYLED_RED_LANTERN.get(), "Chinese Styled Red Lantern");
		add(CBlocks.JAPANESE_STYLED_PAPER_LANTERN.get(), "Japanese Styled Paper Lantern");
		add(CBlocks.JAPANESE_STYLED_RED_LANTERN.get(), "Japanese Styled Red Lantern");
		add(CBlocks.COUPLET.get(), "Couplet");
		add(CBlocks.HORIZONTAL_SCROLL.get(), "Horizontal Scroll");
		add(CBlocks.FU_STICKER.get(), "Fu Sticker");
		add(CBlocks.INVERTED_FU_STICKER.get(), "Inverted Fu Sticker");
		add(CBlocks.GOLDEN_FU_STICKER.get(), "Golden Fu Sticker");
		add(CBlocks.INVERTED_GOLDEN_FU_STICKER.get(), "Inverted Golden Fu Sticker");
		add(CBlocks.FIREWORK_BUNDLE.get(), "Firework Bundle");
		add(CItems.RED_PAPER.get(), "Red Paper");
		add(CItems.GOLD_POWDER.get(), "Gold Powder");
		tooltip("edit_couplet", "Edit Couplet Text");
		tooltip("edit_horizontal_scroll", "Edit Horizontal Scroll Text");
		tooltip("gift", "This lantern contains a gift");
		tooltip("gift_sender", "Gift from: ");
		tooltip("lantern_with_potion", "This lantern contains potions");
		message("is_celebrating", "Celebrating: ");
		message("ticks_since_last_celebration", "Ticks since last celebration: ");
		message("ticks_since_celebration_started", "Ticks since celebration started: ");
		message("celebration_started", "Celebration started in current world");
		message("celebration_terminated", "Celebration terminated in current world");
		message("cannot_ignite_due_to_gift", "This lantern contains a gift and cannot be lit now");
	}

	private void tooltip(String path, String translation) {
		add("tooltip", path, translation);
	}

	private void message(String path, String translation) {
		add("message", path, translation);
	}

	private void add(String type, String path, String translation) {
		add(type + "." + Celebrations.ID + "." + path, translation);
	}
}
