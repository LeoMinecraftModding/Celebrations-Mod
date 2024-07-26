package team.leomc.celebrations.client.event;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.client.renderer.CoupletRenderer;
import team.leomc.celebrations.client.renderer.FuStickerRenderer;
import team.leomc.celebrations.client.renderer.HorizontalScrollRenderer;
import team.leomc.celebrations.item.component.PartyHat;
import team.leomc.celebrations.registry.CBlockEntities;
import team.leomc.celebrations.registry.CDataComponents;
import team.leomc.celebrations.registry.CItems;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Celebrations.ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class CClientSetupEvents {
	@SubscribeEvent
	private static void onClientSetup(FMLClientSetupEvent event) {
		BlockEntityRenderers.register(CBlockEntities.COUPLET.get(), CoupletRenderer::new);
		BlockEntityRenderers.register(CBlockEntities.HORIZONTAL_SCROLL.get(), HorizontalScrollRenderer::new);
		BlockEntityRenderers.register(CBlockEntities.FU_STICKER.get(), FuStickerRenderer::new);

		ItemProperties.register(CItems.PARTY_HAT.get(), Celebrations.id("party_hat_type"), (itemStack, clientLevel, livingEntity, i) -> {
			PartyHat partyHat = itemStack.get(CDataComponents.PART_HAT.get());
			if (partyHat != null) {
				return switch (partyHat.type()) {
					case STRIPES -> 0.1f;
					case TILT_STRIPES -> 0.2f;
					case DOTS -> 0.3f;
				};
			}
			return Float.NEGATIVE_INFINITY;
		});
	}

	@SubscribeEvent
	private static void onRegisterItemColorHandlers(RegisterColorHandlersEvent.Item event) {
		event.register((itemStack, i) -> {
			PartyHat hat = itemStack.get(CDataComponents.PART_HAT.get());
			return i == 1 ? (hat == null ? -1 : hat.color().getTextureDiffuseColor()) : -1;
		}, CItems.PARTY_HAT.get());
	}

	@SubscribeEvent
	public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(CoupletRenderer.LAYER_LOCATION, CoupletRenderer::createCoupletLayer);
		event.registerLayerDefinition(HorizontalScrollRenderer.LAYER_LOCATION, HorizontalScrollRenderer::createScrollLayer);
		event.registerLayerDefinition(FuStickerRenderer.LAYER_LOCATION, FuStickerRenderer::createStickerLayer);
	}
}
