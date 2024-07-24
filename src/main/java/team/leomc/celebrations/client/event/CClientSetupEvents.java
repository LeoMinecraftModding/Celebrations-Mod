package team.leomc.celebrations.client.event;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.client.renderer.CoupletRenderer;
import team.leomc.celebrations.client.renderer.FuStickerRenderer;
import team.leomc.celebrations.client.renderer.HorizontalScrollRenderer;
import team.leomc.celebrations.registry.CBlockEntities;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Celebrations.ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class CClientSetupEvents {
	@SubscribeEvent
	private static void onClientSetup(FMLClientSetupEvent event) {
		BlockEntityRenderers.register(CBlockEntities.COUPLET.get(), CoupletRenderer::new);
		BlockEntityRenderers.register(CBlockEntities.HORIZONTAL_SCROLL.get(), HorizontalScrollRenderer::new);
		BlockEntityRenderers.register(CBlockEntities.FU_STICKER.get(), FuStickerRenderer::new);
	}

	@SubscribeEvent
	public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(CoupletRenderer.LAYER_LOCATION, CoupletRenderer::createCoupletLayer);
		event.registerLayerDefinition(HorizontalScrollRenderer.LAYER_LOCATION, HorizontalScrollRenderer::createScrollLayer);
		event.registerLayerDefinition(FuStickerRenderer.LAYER_LOCATION, FuStickerRenderer::createStickerLayer);
	}
}
