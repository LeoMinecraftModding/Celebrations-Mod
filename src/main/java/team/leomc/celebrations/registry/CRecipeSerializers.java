package team.leomc.celebrations.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.item.recipe.PartyHatRecipe;

public class CRecipeSerializers {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Celebrations.ID);

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PartyHatRecipe>> PARTY_HAT = RECIPE_SERIALIZERS.register("party_hat", () -> new SimpleCraftingRecipeSerializer<>(PartyHatRecipe::new));
}
