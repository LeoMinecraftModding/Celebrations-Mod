package cn.leolezury.celebrations.message;

import cn.leolezury.celebrations.block.entity.CoupletBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoupletUpdateMessage {
    private final BlockPos pos;
    private final String[] lines;

    public CoupletUpdateMessage(BlockPos pos, String[] lines) {
        this.pos = pos;
        this.lines = lines;
    }

    public static CoupletUpdateMessage read(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        String[] lines = new String[7];
        for(int i = 0; i < 7; ++i) {
            lines[i] = buf.readUtf(384);
        }
        return new CoupletUpdateMessage(pos, lines);
    }

    public static void write(CoupletUpdateMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        for(int i = 0; i < 7; ++i) {
            buf.writeUtf(message.lines[i]);
        }
    }

    public static class Handler {
        public static void handle(CoupletUpdateMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.setPacketHandled(true);
            ServerPlayer serverPlayer = context.getSender();
            if (context.getDirection().getReceptionSide() == LogicalSide.SERVER && serverPlayer != null) {
                List<String> list = Stream.of(message.lines).map(ChatFormatting::stripFormatting).collect(Collectors.toList());
                if (context.getNetworkManager().getPacketListener() instanceof ServerGamePacketListenerImpl listener) {
                    listener.filterTextPacket(list).thenAcceptAsync((filteredTexts) -> {
                        updateSignText(serverPlayer, message, filteredTexts);
                    }, Objects.requireNonNull(context.getSender()).server);
                }
            }
        }
    }

    private static void updateSignText(ServerPlayer player, CoupletUpdateMessage message, List<FilteredText> filteredTexts) {
        player.resetLastActionTime();
        ServerLevel serverlevel = player.serverLevel();
        BlockPos blockpos = message.pos;
        if (serverlevel.hasChunkAt(blockpos)) {
            BlockEntity blockentity = serverlevel.getBlockEntity(blockpos);
            if (!(blockentity instanceof CoupletBlockEntity)) {
                return;
            }
            CoupletBlockEntity coupletBlockEntity = (CoupletBlockEntity)blockentity;
            coupletBlockEntity.updateSignText(player, true, filteredTexts);
        }
    }
}
