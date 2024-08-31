package team.leomc.celebrations.item.component;

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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public record BalloonData(DyeColor color, Component name, ItemStack attached) {
	public static final Codec<BalloonData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		DyeColor.CODEC.fieldOf("color").forGetter(BalloonData::color),
		ComponentSerialization.CODEC.fieldOf("name").forGetter(BalloonData::name),
		ItemStack.OPTIONAL_CODEC.fieldOf("attached").forGetter(BalloonData::attached)
	).apply(instance, BalloonData::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, BalloonData> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	public static final BalloonData DEFAULT = new BalloonData(DyeColor.WHITE, Component.empty(), ItemStack.EMPTY);

	public static BalloonData fromTag(Tag tag, HolderLookup.Provider provider) {
		return CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial().orElse(DEFAULT);
	}

	public Tag toTag(HolderLookup.Provider provider) {
		return CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), this).resultOrPartial().orElseGet(CompoundTag::new);
	}

	public BalloonData withName(Component name) {
		return new BalloonData(color(), name, attached());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BalloonData balloonData = (BalloonData) o;
		return color == balloonData.color && Objects.equals(name, balloonData.name) && Objects.equals(attached.getItem(), balloonData.attached.getItem()) && Objects.equals(attached.getCount(), balloonData.attached.getCount()) && Objects.equals(attached.getComponents(), balloonData.attached.getComponents());
	}

	@Override
	public int hashCode() {
		return Objects.hash(color, name, attached);
	}
}
