package team.leomc.celebrations.item.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import team.leomc.celebrations.item.component.PartyHat;
import team.leomc.celebrations.item.component.PartyHatType;
import team.leomc.celebrations.registry.CDataComponents;
import team.leomc.celebrations.registry.CItems;
import team.leomc.celebrations.registry.CRecipeSerializers;
import team.leomc.celebrations.util.CTags;

import java.util.List;

public class PartyHatRecipe extends CustomRecipe {
	public PartyHatRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput craftingInput, Level level) {
		if (craftingInput.items().stream().anyMatch(stack -> !stack.isEmpty() && !stack.is(CItems.PARTY_HAT.get()) && !stack.is(CTags.Items.PARTY_HAT_PATTERN_INGREDIENTS) && !(stack.getItem() instanceof DyeItem))) {
			return false;
		}
		if (craftingInput.items().stream().filter(stack -> stack.is(CTags.Items.PARTY_HAT_PATTERN_INGREDIENTS)).count() != 1) {
			return false;
		}
		if (craftingInput.items().stream().filter(stack -> stack.getItem() instanceof DyeItem).count() != 1) {
			return false;
		}
		return craftingInput.items().stream().filter(stack -> stack.is(CItems.PARTY_HAT.get())).count() == 1;
	}

	@Override
	public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
		List<ItemStack> hatItems = craftingInput.items().stream().filter(stack -> stack.is(CItems.PARTY_HAT.get())).toList();
		List<ItemStack> patternItems = craftingInput.items().stream().filter(stack -> stack.is(CTags.Items.PARTY_HAT_PATTERN_INGREDIENTS)).toList();
		List<ItemStack> dyeItems = craftingInput.items().stream().filter(stack -> stack.getItem() instanceof DyeItem).toList();
		if (hatItems.size() == 1 && patternItems.size() == 1 && dyeItems.size() == 1) {
			PartyHatType type = null;
			if (patternItems.getFirst().is(CTags.Items.PARTY_HAT_STRIPES_INGREDIENTS)) {
				type = PartyHatType.STRIPES;
			}
			if (patternItems.getFirst().is(CTags.Items.PARTY_HAT_TILT_STRIPES_INGREDIENTS)) {
				type = PartyHatType.TILT_STRIPES;
			}
			if (patternItems.getFirst().is(CTags.Items.PARTY_HAT_DOTS_INGREDIENTS)) {
				type = PartyHatType.DOTS;
			}
			if (type != null && dyeItems.getFirst().getItem() instanceof DyeItem dyeItem) {
				PartyHat hat = new PartyHat(type, dyeItem.getDyeColor());
				ItemStack stack = hatItems.getFirst().copy();
				stack.applyComponentsAndValidate(DataComponentPatch.builder().set(CDataComponents.PART_HAT.get(), hat).build());
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CRecipeSerializers.PARTY_HAT.get();
	}
}
