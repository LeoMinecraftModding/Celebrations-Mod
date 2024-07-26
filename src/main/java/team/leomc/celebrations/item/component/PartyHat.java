package team.leomc.celebrations.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import team.leomc.celebrations.Celebrations;

import java.util.Objects;

public record PartyHat(PartyHatType type, DyeColor color) {
	public static final Codec<PartyHat> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		PartyHatType.CODEC.fieldOf("type").forGetter(PartyHat::type),
		DyeColor.CODEC.fieldOf("color").forGetter(PartyHat::color)
	).apply(instance, PartyHat::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, PartyHat> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	public static final PartyHat DEFAULT = new PartyHat(PartyHatType.STRIPES, DyeColor.RED);

	public ResourceLocation getTextureLocation() {
		return Celebrations.id("textures/entity/party_hat_" + type().getSerializedName() + ".png");
	}

	public static PartyHat fromTag(Tag tag, HolderLookup.Provider provider) {
		return CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial().orElse(DEFAULT);
	}

	public Tag toTag(HolderLookup.Provider provider) {
		return CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), this).resultOrPartial().orElseGet(CompoundTag::new);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PartyHat partyHat = (PartyHat) o;
		return color == partyHat.color && type == partyHat.type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, color);
	}
}
