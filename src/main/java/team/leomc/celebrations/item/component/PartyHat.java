package team.leomc.celebrations.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

	public ResourceLocation getTextureLocation() {
		return Celebrations.id("textures/entity/party_hat_" + type().getSerializedName() + ".png");
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
