package team.leomc.celebrations.data.gen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CLootProvider extends LootTableProvider {
	public CLootProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
		super(packOutput, Set.of(), List.of(
			new SubProviderEntry(CBlockLootSubProvider::new, LootContextParamSets.BLOCK),
			new SubProviderEntry(CGiftLootSubProvider::new, LootContextParamSets.GIFT)
		), provider);
	}
}
