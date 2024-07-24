package team.leomc.celebrations.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.block.entity.SignLikeBlockEntity;
import team.leomc.celebrations.util.SignLikeText;

public record UpdateSignLikeTextPayload(BlockPos pos, SignLikeText text) implements CustomPacketPayload {
	public static final Type<UpdateSignLikeTextPayload> TYPE = new Type<>(Celebrations.id("update_sign_like_text"));
	public static final Codec<UpdateSignLikeTextPayload> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		BlockPos.CODEC.fieldOf("pos").forGetter(UpdateSignLikeTextPayload::pos),
		SignLikeText.CODEC.fieldOf("text").forGetter(UpdateSignLikeTextPayload::text)
	).apply(instance, UpdateSignLikeTextPayload::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateSignLikeTextPayload> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	public static void handle(UpdateSignLikeTextPayload payload, IPayloadContext context) {
		if (context.player().level().getBlockEntity(payload.pos()) instanceof SignLikeBlockEntity entity) {
			entity.setText(payload.text());
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
