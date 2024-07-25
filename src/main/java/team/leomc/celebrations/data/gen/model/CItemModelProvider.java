package team.leomc.celebrations.data.gen.model;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.registry.CItems;

public class CItemModelProvider extends ItemModelProvider {
	public CItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, Celebrations.ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		block(CItems.CHINESE_STYLED_BAMBOO_LANTERN.get());
		block(CItems.CHINESE_STYLED_PAPER_LANTERN.get());
		block(CItems.CHINESE_STYLED_RED_LANTERN.get());
		block(CItems.JAPANESE_STYLED_PAPER_LANTERN.get());
		block(CItems.JAPANESE_STYLED_RED_LANTERN.get());
		basicItem(CItems.COUPLET.get());
		basicItem(CItems.HORIZONTAL_SCROLL.get());
		basicItem(CItems.FU_STICKER.get());
		basicItem(CItems.INVERTED_FU_STICKER.get());
		basicItem(CItems.GOLDEN_FU_STICKER.get());
		basicItem(CItems.INVERTED_GOLDEN_FU_STICKER.get());
		block(CItems.FIREWORK_BUNDLE.get());
		basicItem(CItems.RED_PAPER.get());
		basicItem(CItems.GOLD_POWDER.get());
	}

	private void block(Item item) {
		withExistingParent(name(item), modLoc(ModelProvider.BLOCK_FOLDER + "/" + name(item)));
	}

	public ResourceLocation itemTexture(Item item) {
		ResourceLocation name = key(item);
		return texture(name, ModelProvider.ITEM_FOLDER);
	}

	public ResourceLocation texture(ResourceLocation key, String prefix) {
		return ResourceLocation.fromNamespaceAndPath(key.getNamespace(), prefix + "/" + key.getPath());
	}

	private ResourceLocation key(Item item) {
		return BuiltInRegistries.ITEM.getKey(item);
	}

	private String name(Item item) {
		return key(item).getPath();
	}
}
