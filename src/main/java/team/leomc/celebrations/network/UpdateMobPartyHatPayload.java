package team.leomc.celebrations.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.event.CEvents;

public record UpdateMobPartyHatPayload(int id, boolean partyHat) implements CustomPacketPayload {
	public static final Type<UpdateMobPartyHatPayload> TYPE = new Type<>(Celebrations.id("update_mob_party_hat"));
	public static final Codec<UpdateMobPartyHatPayload> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.INT.fieldOf("id").forGetter(UpdateMobPartyHatPayload::id),
		Codec.BOOL.fieldOf("party_hat").forGetter(UpdateMobPartyHatPayload::partyHat)
	).apply(instance, UpdateMobPartyHatPayload::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateMobPartyHatPayload> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	public static void handle(UpdateMobPartyHatPayload payload, IPayloadContext context) {
		Entity entity = context.player().level().getEntity(payload.id());
		if (entity instanceof Mob mob) {
			mob.getPersistentData().putBoolean(CEvents.TAG_PARTY_HAT, payload.partyHat());
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
