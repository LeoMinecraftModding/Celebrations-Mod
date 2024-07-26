package team.leomc.celebrations.data.gen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.util.CTags;

import java.util.concurrent.CompletableFuture;

public class CItemTagsProvider extends ItemTagsProvider {
	public CItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> provider, ExistingFileHelper helper) {
		super(output, future, provider, Celebrations.ID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider lookupProvider) {
		tag(CTags.Items.IGNITERS).add(
			Items.FLINT_AND_STEEL,
			Items.FIRE_CHARGE,
			Items.BLAZE_ROD,
			Items.BLAZE_POWDER
		);
		tag(CTags.Items.PARTY_HAT_PATTERN_INGREDIENTS)
			.addTag(CTags.Items.PARTY_HAT_STRIPES_INGREDIENTS)
			.addTag(CTags.Items.PARTY_HAT_TILT_STRIPES_INGREDIENTS)
			.addTag(CTags.Items.PARTY_HAT_DOTS_INGREDIENTS);
		tag(CTags.Items.PARTY_HAT_STRIPES_INGREDIENTS).add(
			Items.REDSTONE
		);
		tag(CTags.Items.PARTY_HAT_TILT_STRIPES_INGREDIENTS).add(
			Items.FEATHER
		);
		tag(CTags.Items.PARTY_HAT_DOTS_INGREDIENTS).add(
			Items.WHEAT_SEEDS,
			Items.MELON_SEEDS,
			Items.PUMPKIN_SEEDS,
			Items.BEETROOT_SEEDS,
			Items.TORCHFLOWER_SEEDS
		);
	}
}
