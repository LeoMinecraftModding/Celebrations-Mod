package team.leomc.celebrations.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.util.Lantern;

public class CDataComponents {
	public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Celebrations.ID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Lantern>> LANTERN = DATA_COMPONENTS.register("lantern", () -> DataComponentType.<Lantern>builder().persistent(Lantern.CODEC).networkSynchronized(Lantern.STREAM_CODEC).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> FIREWORK_AMOUNT = DATA_COMPONENTS.register("firework_amount", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
}
