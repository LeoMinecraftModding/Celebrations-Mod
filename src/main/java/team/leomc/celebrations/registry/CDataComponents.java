package team.leomc.celebrations.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.util.Lantern;

public class CDataComponents {
	public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Celebrations.ID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Lantern>> LANTERN = DATA_COMPONENTS.register("lantern", () -> DataComponentType.<Lantern>builder().persistent(Lantern.CODEC).networkSynchronized(Lantern.STREAM_CODEC).build());
}
