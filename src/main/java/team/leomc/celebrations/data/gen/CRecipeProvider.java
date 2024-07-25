package team.leomc.celebrations.data.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.registry.CDataComponents;
import team.leomc.celebrations.registry.CItems;

import java.util.concurrent.CompletableFuture;

public class CRecipeProvider extends RecipeProvider {
	public CRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CItems.RED_PAPER.get(), 4)
			.requires(Items.PAPER, 4)
			.requires(Items.RED_DYE)
			.unlockedBy("has_paper", has(Items.PAPER))
			.unlockedBy("has_red_dye", has(Items.RED_DYE))
			.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CItems.GOLD_POWDER.get(), 4)
			.requires(Items.GOLD_NUGGET)
			.unlockedBy("has_gold_nugget", has(Items.GOLD_NUGGET))
			.save(recipeOutput);
		ShapedRecipeBuilder
			.shaped(RecipeCategory.MISC, CItems.CHINESE_STYLED_BAMBOO_LANTERN.get())
			.define('#', Items.TORCH)
			.define('X', Items.BAMBOO)
			.define('S', ItemTags.WOODEN_SLABS)
			.pattern("XSX")
			.pattern("X#X")
			.pattern("XSX")
			.unlockedBy("has_torch", has(Items.TORCH))
			.save(recipeOutput);
		ShapedRecipeBuilder
			.shaped(RecipeCategory.MISC, CItems.CHINESE_STYLED_PAPER_LANTERN.get())
			.define('#', Items.TORCH)
			.define('X', Items.PAPER)
			.define('S', Items.STICK)
			.pattern(" S ")
			.pattern("X#X")
			.pattern(" S ")
			.unlockedBy("has_torch", has(Items.TORCH))
			.save(recipeOutput);
		ShapedRecipeBuilder
			.shaped(RecipeCategory.MISC, CItems.CHINESE_STYLED_RED_LANTERN.get())
			.define('#', Items.TORCH)
			.define('G', Items.GOLD_NUGGET)
			.define('S', Items.STRING)
			.define('W', Items.STICK)
			.define('X', CItems.RED_PAPER.get())
			.pattern(" G ")
			.pattern("X#X")
			.pattern("WSW")
			.unlockedBy("has_torch", has(Items.TORCH))
			.save(recipeOutput);
		ShapedRecipeBuilder
			.shaped(RecipeCategory.MISC, CItems.JAPANESE_STYLED_PAPER_LANTERN.get())
			.define('#', Items.TORCH)
			.define('X', Items.PAPER)
			.define('S', ItemTags.WOODEN_SLABS)
			.pattern(" S ")
			.pattern("X#X")
			.pattern(" S ")
			.unlockedBy("has_torch", has(Items.TORCH))
			.save(recipeOutput);
		ShapedRecipeBuilder
			.shaped(RecipeCategory.MISC, CItems.JAPANESE_STYLED_RED_LANTERN.get())
			.define('#', Items.TORCH)
			.define('S', ItemTags.WOODEN_SLABS)
			.define('X', CItems.RED_PAPER.get())
			.pattern(" S ")
			.pattern("X#X")
			.pattern(" S ")
			.unlockedBy("has_torch", has(Items.TORCH))
			.save(recipeOutput);

		ShapedRecipeBuilder
			.shaped(RecipeCategory.MISC, CItems.COUPLET.get())
			.define('X', CItems.RED_PAPER.get())
			.pattern("X")
			.pattern("X")
			.pattern("X")
			.unlockedBy("has_red_paper", has(CItems.RED_PAPER.get()))
			.save(recipeOutput);
		ShapedRecipeBuilder
			.shaped(RecipeCategory.MISC, CItems.HORIZONTAL_SCROLL.get())
			.define('X', CItems.RED_PAPER.get())
			.pattern("XX")
			.unlockedBy("has_red_paper", has(CItems.RED_PAPER.get()))
			.save(recipeOutput);

		ShapedRecipeBuilder
			.shaped(RecipeCategory.MISC, CItems.FU_STICKER.get())
			.define('#', Items.INK_SAC)
			.define('X', CItems.RED_PAPER.get())
			.pattern(" X ")
			.pattern("X#X")
			.pattern(" X ")
			.unlockedBy("has_red_paper", has(CItems.RED_PAPER.get()))
			.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CItems.INVERTED_FU_STICKER.get())
			.requires(CItems.FU_STICKER.get())
			.unlockedBy("has_fu_sticker", has(CItems.FU_STICKER.get()))
			.save(recipeOutput, Celebrations.id(getConversionRecipeName(CItems.INVERTED_FU_STICKER.get(), CItems.FU_STICKER.get())));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CItems.FU_STICKER.get())
			.requires(CItems.INVERTED_FU_STICKER.get())
			.unlockedBy("has_inverted_fu_sticker", has(CItems.INVERTED_FU_STICKER.get()))
			.save(recipeOutput, Celebrations.id(getConversionRecipeName(CItems.FU_STICKER.get(), CItems.INVERTED_FU_STICKER.get())));

		ShapedRecipeBuilder
			.shaped(RecipeCategory.MISC, CItems.GOLDEN_FU_STICKER.get())
			.define('#', CItems.GOLD_POWDER.get())
			.define('X', CItems.RED_PAPER.get())
			.pattern(" X ")
			.pattern("X#X")
			.pattern(" X ")
			.unlockedBy("has_red_paper", has(CItems.RED_PAPER.get()))
			.save(recipeOutput);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CItems.INVERTED_GOLDEN_FU_STICKER.get())
			.requires(CItems.GOLDEN_FU_STICKER.get())
			.unlockedBy("has_golden_fu_sticker", has(CItems.GOLDEN_FU_STICKER.get()))
			.save(recipeOutput, Celebrations.id(getConversionRecipeName(CItems.INVERTED_GOLDEN_FU_STICKER.get(), CItems.GOLDEN_FU_STICKER.get())));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CItems.GOLDEN_FU_STICKER.get())
			.requires(CItems.INVERTED_GOLDEN_FU_STICKER.get())
			.unlockedBy("has_inverted_golden_fu_sticker", has(CItems.INVERTED_GOLDEN_FU_STICKER.get()))
			.save(recipeOutput, Celebrations.id(getConversionRecipeName(CItems.GOLDEN_FU_STICKER.get(), CItems.INVERTED_GOLDEN_FU_STICKER.get())));

		ItemStack stack = CItems.FIREWORK_BUNDLE.get().getDefaultInstance();
		stack.set(CDataComponents.FIREWORK_AMOUNT, 6);
		ShapedRecipeBuilder
			.shaped(RecipeCategory.MISC, stack)
			.define('#', Items.FIREWORK_ROCKET)
			.define('S', ItemTags.WOODEN_SLABS)
			.pattern("###")
			.pattern("###")
			.pattern("SSS")
			.unlockedBy("has_firework_rocket", has(Items.FIREWORK_ROCKET))
			.save(recipeOutput);
	}
}
