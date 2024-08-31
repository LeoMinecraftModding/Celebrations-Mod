package team.leomc.celebrations.client.event;

import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.client.model.entity.BalloonModel;
import team.leomc.celebrations.client.renderer.block.CoupletRenderer;
import team.leomc.celebrations.client.renderer.block.FuStickerRenderer;
import team.leomc.celebrations.client.renderer.block.HorizontalScrollRenderer;
import team.leomc.celebrations.client.renderer.entity.BalloonRenderer;
import team.leomc.celebrations.item.component.BalloonData;
import team.leomc.celebrations.item.component.PartyHat;
import team.leomc.celebrations.registry.CBlockEntities;
import team.leomc.celebrations.registry.CDataComponents;
import team.leomc.celebrations.registry.CEntities;
import team.leomc.celebrations.registry.CItems;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Celebrations.ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class CClientSetupEvents {
	@SubscribeEvent
	private static void onClientSetup(FMLClientSetupEvent event) {
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
	private static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CEntities.BALLOON.get(), BalloonRenderer::new);
		event.registerBlockEntityRenderer(CBlockEntities.COUPLET.get(), CoupletRenderer::new);
		event.registerBlockEntityRenderer(CBlockEntities.HORIZONTAL_SCROLL.get(), HorizontalScrollRenderer::new);
		event.registerBlockEntityRenderer(CBlockEntities.FU_STICKER.get(), FuStickerRenderer::new);
	}

	@SubscribeEvent
	private static void onRegisterItemColorHandlers(RegisterColorHandlersEvent.Item event) {
		event.register((itemStack, i) -> {
			PartyHat hat = itemStack.get(CDataComponents.PART_HAT.get());
			return i == 1 ? (hat == null ? -1 : hat.color().getTextureDiffuseColor()) : -1;
		}, CItems.PARTY_HAT.get());
		event.register((itemStack, i) -> {
			BalloonData data = itemStack.get(CDataComponents.BALLOON_DATA.get());
			return i == 1 ? (data == null ? -1 : data.color().getTextureDiffuseColor()) : -1;
		}, CItems.BALLOON.get());
	}

	@SubscribeEvent
	public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(BalloonModel.LAYER_LOCATION, BalloonModel::createBodyLayer);
		event.registerLayerDefinition(CoupletRenderer.LAYER_LOCATION, CoupletRenderer::createCoupletLayer);
		event.registerLayerDefinition(HorizontalScrollRenderer.LAYER_LOCATION, HorizontalScrollRenderer::createScrollLayer);
		event.registerLayerDefinition(FuStickerRenderer.LAYER_LOCATION, FuStickerRenderer::createStickerLayer);
	}
}
