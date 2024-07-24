package team.leomc.celebrations.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.leomc.celebrations.Celebrations;

public class CCreativeModeTabs {
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Celebrations.ID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DEFAULT = TABS.register("default", () -> CreativeModeTab.builder()
		.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
		.title(Component.translatable("name." + Celebrations.ID))
		.icon(() -> new ItemStack(CItems.CHINESE_STYLED_RED_LANTERN.get()))
		.displayItems((params, output) -> BuiltInRegistries.ITEM.stream().filter(i -> BuiltInRegistries.ITEM.getKey(i).getNamespace().equals(Celebrations.ID)).forEach(output::accept))
		.build());
}
