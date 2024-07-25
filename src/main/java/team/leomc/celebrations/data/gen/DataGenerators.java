package team.leomc.celebrations.data.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.data.gen.lang.CChineseLanguageProvider;
import team.leomc.celebrations.data.gen.lang.CEnglishLanguageProvider;
import team.leomc.celebrations.data.gen.loot.CLootProvider;
import team.leomc.celebrations.data.gen.model.CBlockStateProvider;
import team.leomc.celebrations.data.gen.model.CItemModelProvider;
import team.leomc.celebrations.data.gen.tags.CBlockTagsProvider;
import team.leomc.celebrations.data.gen.tags.CItemTagsProvider;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Celebrations.ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		generator.addProvider(event.includeClient(), new CEnglishLanguageProvider(output));
		generator.addProvider(event.includeClient(), new CChineseLanguageProvider(output));

		generator.addProvider(event.includeClient(), new CBlockStateProvider(output, helper));
		generator.addProvider(event.includeClient(), new CItemModelProvider(output, helper));

		CBlockTagsProvider blockTags = new CBlockTagsProvider(output, lookupProvider, helper);
		generator.addProvider(event.includeServer(), blockTags);
		generator.addProvider(event.includeServer(), new CItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), helper));
		generator.addProvider(event.includeServer(), new CLootProvider(output, lookupProvider));
		generator.addProvider(event.includeServer(), new CRecipeProvider(output, lookupProvider));
	}
}
