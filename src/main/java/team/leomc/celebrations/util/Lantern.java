package team.leomc.celebrations.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Lantern(List<MobEffectInstance> effects, ItemStack gift, Component giftSender) {
	public static final Codec<Lantern> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		MobEffectInstance.CODEC.listOf().fieldOf("effects").forGetter(Lantern::effects),
		ItemStack.OPTIONAL_CODEC.fieldOf("gift").forGetter(Lantern::gift),
		ComponentSerialization.CODEC.fieldOf("gift_sender").forGetter(Lantern::giftSender)
	).apply(instance, Lantern::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, Lantern> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	public static final Lantern DEFAULT = new Lantern(new ArrayList<>(), ItemStack.EMPTY, Component.empty());

	public static Lantern fromTag(Tag tag, HolderLookup.Provider provider) {
		return CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial().orElse(DEFAULT);
	}

	public Tag toTag(HolderLookup.Provider provider) {
		return CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), this).resultOrPartial().orElseGet(CompoundTag::new);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Lantern lantern = (Lantern) o;
		return Objects.equals(gift.getItem(), lantern.gift.getItem()) && Objects.equals(gift.getCount(), lantern.gift.getCount()) && Objects.equals(gift.getComponents(), lantern.gift.getComponents()) && Objects.equals(giftSender, lantern.giftSender) && Objects.equals(effects, lantern.effects);
	}

	@Override
	public int hashCode() {
		return Objects.hash(effects, gift, giftSender);
	}
}
