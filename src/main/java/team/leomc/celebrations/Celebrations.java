package team.leomc.celebrations;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import team.leomc.celebrations.config.CCommonConfig;
import team.leomc.celebrations.registry.*;

@Mod(Celebrations.ID)
public class Celebrations {
	public static final String ID = "celebrations";

	public Celebrations(IEventBus modBus, ModContainer container) {
		container.registerConfig(ModConfig.Type.COMMON, CCommonConfig.SPEC);

		CBlocks.BLOCKS.register(modBus);
		CBlockEntities.BLOCK_ENTITIES.register(modBus);
		CDataComponents.DATA_COMPONENTS.register(modBus);
		CItems.ITEMS.register(modBus);
		CCreativeModeTabs.TABS.register(modBus);
		CRecipeSerializers.RECIPE_SERIALIZERS.register(modBus);
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(ID, path);
	}
}
