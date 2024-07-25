package team.leomc.celebrations.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import team.leomc.celebrations.block.entity.HorizontalScrollBlockEntity;
import team.leomc.celebrations.network.OpenHorizontalScrollEditScreenPayload;

public class HorizontalScrollBlock extends WallSignLikeBlock {
	public static final MapCodec<HorizontalScrollBlock> CODEC = simpleCodec(HorizontalScrollBlock::new);

	public HorizontalScrollBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new HorizontalScrollBlockEntity(pos, state);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (player instanceof ServerPlayer serverPlayer) {
			PacketDistributor.sendToPlayer(serverPlayer, new OpenHorizontalScrollEditScreenPayload(pos));
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
