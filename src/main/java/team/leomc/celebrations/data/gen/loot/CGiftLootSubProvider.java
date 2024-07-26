package team.leomc.celebrations.data.gen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import team.leomc.celebrations.data.CLootTables;
import team.leomc.celebrations.registry.CItems;

import java.util.function.BiConsumer;

public record CGiftLootSubProvider(HolderLookup.Provider registries) implements LootTableSubProvider {
	public CGiftLootSubProvider(HolderLookup.Provider registries) {
		this.registries = registries;
	}

	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
		consumer.accept(CLootTables.VILLAGER_GIFT,
			LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(4))
					.add(LootItem.lootTableItem(Items.WHEAT_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(50))
					.add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(50))
					.add(LootItem.lootTableItem(Items.MELON_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(50))
					.add(LootItem.lootTableItem(Items.BEETROOT_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))).setWeight(50))
					.add(LootItem.lootTableItem(Items.COOKED_BEEF).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(40))
					.add(LootItem.lootTableItem(Items.COOKED_MUTTON).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(40))
					.add(LootItem.lootTableItem(Items.COOKED_PORKCHOP).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(40))
					.add(LootItem.lootTableItem(Items.COOKED_COD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(50))
					.add(LootItem.lootTableItem(Items.COOKED_SALMON).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))).setWeight(50))
					.add(LootItem.lootTableItem(Items.TROPICAL_FISH).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))).setWeight(20))
					.add(LootItem.lootTableItem(CItems.COUPLET.get()).setWeight(30))
					.add(LootItem.lootTableItem(CItems.HORIZONTAL_SCROLL.get()).setWeight(30))
					.add(LootItem.lootTableItem(CItems.FU_STICKER.get()).setWeight(30))
					.add(LootItem.lootTableItem(CItems.GOLDEN_FU_STICKER.get()).setWeight(30))
					.add(LootItem.lootTableItem(CItems.RED_PAPER.get()).setWeight(35))
					.add(LootItem.lootTableItem(CItems.GOLD_POWDER.get()).setWeight(35)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(3))
					.add(LootItem.lootTableItem(Items.EMERALD).when(LootItemRandomChanceCondition.randomChance(0.5f)).setWeight(50))
					.add(LootItem.lootTableItem(Items.DIAMOND).when(LootItemRandomChanceCondition.randomChance(0.3f)).setWeight(20)))
				.withPool(LootPool.lootPool()
					.setRolls(ConstantValue.exactly(1))
					.add(LootItem.lootTableItem(Items.EMERALD_BLOCK).when(LootItemRandomChanceCondition.randomChance(0.1f)).setWeight(50))
					.add(LootItem.lootTableItem(Items.DIAMOND_BLOCK).when(LootItemRandomChanceCondition.randomChance(0.1f)).setWeight(50))));
	}

	public HolderLookup.Provider registries() {
		return this.registries;
	}
}