package team.leomc.celebrations.data.gen.lang;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.registry.CBlocks;
import team.leomc.celebrations.registry.CItems;

public class CChineseLanguageProvider extends LanguageProvider {
	public CChineseLanguageProvider(PackOutput output) {
		super(output, Celebrations.ID, "zh_cn");
	}

	@Override
	protected void addTranslations() {
		add("name." + Celebrations.ID, "欢庆");
		add("fml.menu.mods.info.description." + Celebrations.ID, "一个关于庆祝的模组！");
		add(Celebrations.ID + ".configuration.celebration", "庆祝");
		add(Celebrations.ID + ".configuration.celebrationInterval", "庆祝时间间隔");
		add(Celebrations.ID + ".configuration.celebrationDuration", "庆祝时长");
		add(Celebrations.ID + ".configuration.lantern", "灯笼");
		add(Celebrations.ID + ".configuration.lanternEffectRange", "灯笼药水效果范围");
		add(Celebrations.ID + ".configuration.lanternGiveNonEnemyBeneficialEffect", "灯笼给予非敌对生物增益效果");
		add(Celebrations.ID + ".configuration.lanternGiveNonEnemyHarmfulEffect", "灯笼给予非敌对生物负面效果");
		add(Celebrations.ID + ".configuration.lanternGiveEnemyBeneficialEffect", "灯笼给予敌对生物增益效果");
		add(Celebrations.ID + ".configuration.lanternGiveEnemyHarmfulEffect", "灯笼给予敌对生物负面效果");
		add(CBlocks.CHINESE_STYLED_BAMBOO_LANTERN.get(), "中式竹灯笼");
		add(CBlocks.CHINESE_STYLED_PAPER_LANTERN.get(), "中式纸灯笼");
		add(CBlocks.CHINESE_STYLED_RED_LANTERN.get(), "中式红灯笼");
		add(CBlocks.JAPANESE_STYLED_PAPER_LANTERN.get(), "日式纸灯笼");
		add(CBlocks.JAPANESE_STYLED_RED_LANTERN.get(), "日式红灯笼");
		add(CBlocks.COUPLET.get(), "对联");
		add(CBlocks.HORIZONTAL_SCROLL.get(), "横批");
		add(CBlocks.FU_STICKER.get(), "福字");
		add(CBlocks.INVERTED_FU_STICKER.get(), "倒福字");
		add(CBlocks.GOLDEN_FU_STICKER.get(), "金色福字");
		add(CBlocks.INVERTED_GOLDEN_FU_STICKER.get(), "金色倒福字");
		add(CBlocks.FIREWORK_BUNDLE.get(), "烟花桶");
		add(CItems.RED_PAPER.get(), "红纸");
		add(CItems.GOLD_POWDER.get(), "金粉");
		add(CItems.PARTY_HAT.get(), "派对帽");
		add("party_hat_type." + Celebrations.ID + ".stripes", "横纹");
		add("party_hat_type." + Celebrations.ID + ".tilt_stripes", "斜纹");
		add("party_hat_type." + Celebrations.ID + ".dots", "点状纹");
		tooltip("edit_couplet", "编辑对联信息");
		tooltip("edit_horizontal_scroll", "编辑对联横批信息");
		tooltip("gift", "这盏灯笼装有礼物");
		tooltip("gift_sender", "礼物来自：");
		tooltip("lantern_with_potion", "这盏灯笼装有药水");
		message("is_celebrating", "正在庆祝：");
		message("ticks_since_last_celebration", "自从上次庆祝已经过去了（游戏刻）");
		message("ticks_since_celebration_started", "自从开始庆祝已经过去了（游戏刻）");
		message("celebration_started", "庆祝已在当前世界开始");
		message("celebration_terminated", "庆祝已在当前世界被结束");
		message("cannot_ignite_due_to_gift", "这盏灯笼装有礼物，现在无法点燃");
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
