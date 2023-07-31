package cn.leolezury.celebrations;

import cn.leolezury.celebrations.config.CBConfig;
import cn.leolezury.celebrations.init.BlockEntityInit;
import cn.leolezury.celebrations.init.BlockInit;
import cn.leolezury.celebrations.init.CreativeModeTabInit;
import cn.leolezury.celebrations.init.ItemInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Objects;

@Mod(Celebrations.MOD_ID)
public class Celebrations {
    public static final String MOD_ID = "celebrations";
    public static final SimpleChannel NETWORK_WRAPPER;
    private static final String PROTOCOL_VERSION = Integer.toString(1);

    static {
        NetworkRegistry.ChannelBuilder channel = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "main_channel"));
        channel = channel.clientAcceptedVersions(Integer.toString(1)::equals);
        NETWORK_WRAPPER = channel.serverAcceptedVersions(Integer.toString(1)::equals).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();
    }

    public Celebrations() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CBConfig.SPEC);

        BlockInit.BLOCKS.register(modEventBus);
        BlockEntityInit.BLOCK_ENTITIES.register(modEventBus);
        ItemInit.ITEMS.register(modEventBus);
        CreativeModeTabInit.TABS.register(modEventBus);
    }

    public static <MSG> void sendMessageToServer(MSG message) {
        NETWORK_WRAPPER.sendToServer(message);
    }
}
