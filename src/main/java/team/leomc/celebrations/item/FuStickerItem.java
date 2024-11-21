package team.leomc.celebrations.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import team.leomc.celebrations.entity.FuSticker;

public class FuStickerItem extends Item {

	public FuStickerItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		BlockPos blockpos = context.getClickedPos();
		BlockState blockState = context.getLevel().getBlockState(blockpos);
		Direction direction = context.getClickedFace();
		BlockPos blockpos1 = blockpos.relative(direction);
		Player player = context.getPlayer();
		ItemStack itemstack = context.getItemInHand();
		Level level = context.getLevel();
		if (player != null && !this.mayPlace(player, direction, itemstack, blockpos1)) {
			return InteractionResult.FAIL;
		} else {
			if (blockState.getBlock() instanceof DoorBlock) {
				if (blockState.getValue(DoorBlock.OPEN)) {
					return InteractionResult.FAIL;
				}
				if (direction.getOpposite() != blockState.getValue(DoorBlock.FACING)) {
					return InteractionResult.FAIL;
				}
			}

			FuSticker hangingentity = FuSticker.create(level, blockpos1, blockpos, direction, itemstack);
			CustomData customdata = (CustomData)itemstack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY);
			if (!customdata.isEmpty()) {
				EntityType.updateCustomEntityTag(level, player, (Entity)hangingentity, customdata);
			}

			if (((HangingEntity)hangingentity).survives()) {
				if (!level.isClientSide) {
					((HangingEntity)hangingentity).playPlacementSound();
					level.gameEvent(player, GameEvent.ENTITY_PLACE, ((HangingEntity)hangingentity).position());
					level.addFreshEntity((Entity)hangingentity);
				}

				itemstack.shrink(1);
				return InteractionResult.sidedSuccess(level.isClientSide);
			} else {
				return InteractionResult.CONSUME;
			}
		}
	}

	protected boolean mayPlace(Player player, Direction direction, ItemStack hangingEntityStack, BlockPos pos) {
		return !direction.getAxis().isVertical() && player.mayUseItemAt(pos, direction, hangingEntityStack);
	}
}
