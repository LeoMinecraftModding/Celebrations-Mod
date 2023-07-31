package cn.leolezury.celebrations.event;

import cn.leolezury.celebrations.Celebrations;
import cn.leolezury.celebrations.client.renderer.CoupletRenderer;
import cn.leolezury.celebrations.client.renderer.FuStickerRenderer;
import cn.leolezury.celebrations.client.renderer.HorizontalScrollRenderer;
import cn.leolezury.celebrations.init.BlockEntityInit;
import cn.leolezury.celebrations.message.CoupletUpdateMessage;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Celebrations.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEvents {
    private static int packetsRegistered;
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(BlockEntityInit.COUPLET.get(), CoupletRenderer::new);
        BlockEntityRenderers.register(BlockEntityInit.HORIZONTAL_SCROLL.get(), HorizontalScrollRenderer::new);
        BlockEntityRenderers.register(BlockEntityInit.FU_STICKER.get(), FuStickerRenderer::new);
        Celebrations.NETWORK_WRAPPER.registerMessage(packetsRegistered++, CoupletUpdateMessage.class, CoupletUpdateMessage::write, CoupletUpdateMessage::read, CoupletUpdateMessage.Handler::handle);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CoupletRenderer.LAYER_LOCATION, CoupletRenderer::createCoupletLayer);
        event.registerLayerDefinition(HorizontalScrollRenderer.LAYER_LOCATION, HorizontalScrollRenderer::createScrollLayer);
        event.registerLayerDefinition(FuStickerRenderer.LAYER_LOCATION, FuStickerRenderer::createStickerLayer);
    }
}
