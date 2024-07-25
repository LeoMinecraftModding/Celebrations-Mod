package team.leomc.celebrations.data.gen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import team.leomc.celebrations.Celebrations;

import java.util.concurrent.CompletableFuture;

public class CBlockTagsProvider extends BlockTagsProvider {
	public CBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
		super(output, future, Celebrations.ID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider lookupProvider) {

	}
}
