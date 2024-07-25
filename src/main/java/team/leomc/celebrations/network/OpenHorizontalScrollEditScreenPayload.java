package team.leomc.celebrations.network;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.block.entity.HorizontalScrollBlockEntity;
import team.leomc.celebrations.client.gui.screen.HorizontalScrollEditScreen;

public record OpenHorizontalScrollEditScreenPayload(BlockPos pos) implements CustomPacketPayload {
	public static final Type<OpenHorizontalScrollEditScreenPayload> TYPE = new Type<>(Celebrations.id("open_scroll_edit"));
	public static final Codec<OpenHorizontalScrollEditScreenPayload> CODEC = BlockPos.CODEC.xmap(OpenHorizontalScrollEditScreenPayload::new, OpenHorizontalScrollEditScreenPayload::pos);
	public static final StreamCodec<RegistryFriendlyByteBuf, OpenHorizontalScrollEditScreenPayload> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	public static void handle(OpenHorizontalScrollEditScreenPayload payload, IPayloadContext context) {
		if (FMLLoader.getDist() == Dist.CLIENT) {
			Handler.handle(payload.pos());
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class Handler {
		private static void handle(BlockPos pos) {
			if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof HorizontalScrollBlockEntity entity) {
				Minecraft.getInstance().setScreen(new HorizontalScrollEditScreen(entity));
			}
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
