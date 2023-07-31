package cn.leolezury.celebrations.init;

import cn.leolezury.celebrations.Celebrations;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModeTabInit {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Celebrations.MOD_ID);

    public static final RegistryObject<CreativeModeTab> DEFAULT = TABS.register("default", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .title(Component.translatable("itemGroup." + Celebrations.MOD_ID))
            .icon(() -> new ItemStack(ItemInit.CHINESE_STYLED_RED_LANTERN.get()))
            .displayItems((parameters, output) -> {
                output.accept(ItemInit.CHINESE_STYLED_BAMBOO_LANTERN.get());
                output.accept(ItemInit.CHINESE_STYLED_PAPER_LANTERN.get());
                output.accept(ItemInit.CHINESE_STYLED_RED_LANTERN.get());
                output.accept(ItemInit.JAPANESE_STYLED_PAPER_LANTERN.get());
                output.accept(ItemInit.JAPANESE_STYLED_RED_LANTERN.get());
                output.accept(ItemInit.COUPLET.get());
                output.accept(ItemInit.HORIZONTAL_SCROLL.get());
                output.accept(ItemInit.FU_STICKER.get());
                output.accept(ItemInit.INVERTED_FU_STICKER.get());
                output.accept(ItemInit.GOLDEN_FU_STICKER.get());
                output.accept(ItemInit.INVERTED_GOLDEN_FU_STICKER.get());
                output.accept(ItemInit.FIREWORK_BUNDLE.get());
            }).build());
}
