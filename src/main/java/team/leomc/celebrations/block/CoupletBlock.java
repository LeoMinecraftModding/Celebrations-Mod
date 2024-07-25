package team.leomc.celebrations.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import team.leomc.celebrations.block.entity.CoupletBlockEntity;
import team.leomc.celebrations.network.OpenCoupletEditScreenPayload;

public class CoupletBlock extends WallSignLikeBlock {
	public static final MapCodec<CoupletBlock> CODEC = simpleCodec(CoupletBlock::new);

	public CoupletBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CoupletBlockEntity(pos, state);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return super.canSurvive(state, level, pos) && level.getBlockState(pos.below()).isAir() && !level.getBlockState(pos.above()).is(this);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (player instanceof ServerPlayer serverPlayer) {
			PacketDistributor.sendToPlayer(serverPlayer, new OpenCoupletEditScreenPayload(pos));
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
