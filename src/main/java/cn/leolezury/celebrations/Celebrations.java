package cn.leolezury.celebrations;

import cn.leolezury.celebrations.config.CBConfig;
import cn.leolezury.celebrations.init.BlockEntityInit;
import cn.leolezury.celebrations.init.BlockInit;
import cn.leolezury.celebrations.init.ItemInit;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Celebrations.MOD_ID)
public class Celebrations {
    public static final String MOD_ID = "celebrations";

    public Celebrations() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CBConfig.SPEC);

        BlockInit.BLOCKS.register(modEventBus);
        BlockEntityInit.BLOCK_ENTITIES.register(modEventBus);
        ItemInit.ITEMS.register(modEventBus);
    }
}
