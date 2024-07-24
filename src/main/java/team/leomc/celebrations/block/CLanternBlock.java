package team.leomc.celebrations.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.block.entity.LanternBlockEntity;
import team.leomc.celebrations.item.LanternBlockItem;
import team.leomc.celebrations.registry.CBlockEntities;
import team.leomc.celebrations.registry.CDataComponents;
import team.leomc.celebrations.util.CTags;

import java.util.List;

public class CLanternBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public CLanternBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(WATERLOGGED, false));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return null;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIT, WATERLOGGED);
		super.createBlockStateDefinition(builder);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LanternBlockEntity(pos, state);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		List<ItemStack> drops = super.getDrops(state, builder);
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		for (int i = 0; i < drops.size(); i++) {
			ItemStack itemStack = drops.get(i);
			if (itemStack.getItem() instanceof LanternBlockItem && blockEntity instanceof LanternBlockEntity lanternBlockEntity) {
				itemStack.applyComponentsAndValidate(DataComponentPatch.builder().set(CDataComponents.LANTERN.get(), lanternBlockEntity.getLantern()).build());
			}
			drops.set(i, itemStack);
		}
		return drops;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, CBlockEntities.LANTERN.get(), LanternBlockEntity::tick);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return direction == Direction.UP && !this.canSurvive(state, level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		return reader.getBlockState(pos.above()).isFaceSturdy(reader, pos.above(), Direction.DOWN, SupportType.CENTER);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.is(CTags.Items.IGNITERS) && !state.getValue(LIT)) {
			if (level.getBlockEntity(pos) instanceof LanternBlockEntity lanternBlockEntity) {
				if (lanternBlockEntity.getGift() != null && !lanternBlockEntity.getGift().isEmpty()) {
					if (!level.isClientSide) {
						player.sendSystemMessage(Component.translatable("message." + Celebrations.ID + ".cannot_ignite_due_to_gift").withStyle(ChatFormatting.ITALIC));
					}
					return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
				}
			}
			if (!level.isClientSide) {
				level.setBlockAndUpdate(pos, state.setValue(LIT, true));
			}
			if (!level.isClientSide) {
				if (!stack.isDamageableItem()) {
					stack.consume(1, player);
					if (stack.is(Items.FIRE_CHARGE)) {
						level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 2.0F, 1.0F);
					}
				} else {
					stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
					if (stack.is(Items.FLINT_AND_STEEL)) {
						level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 2.0F, 1.0F);
					}
				}
			}
			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}
		if (stack.has(DataComponents.POTION_CONTENTS) && level.getBlockEntity(pos) instanceof LanternBlockEntity lanternBlockEntity) {
			PotionContents potions = stack.get(DataComponents.POTION_CONTENTS);
			if (potions != null && potions.potion().isPresent()) {
				Potion potion = potions.potion().get().value();
				if (potion.equals(Potions.WATER)) {
					lanternBlockEntity.clearEffects();
					level.setBlockAndUpdate(pos, state.setValue(LIT, false));
					if (!player.getAbilities().instabuild) {
						player.setItemInHand(hand, Items.GLASS_BOTTLE.getDefaultInstance());
					}
					return ItemInteractionResult.sidedSuccess(level.isClientSide);
				}
				boolean succeed = false;
				for (MobEffectInstance instance : potions.getAllEffects()) {
					if (!instance.getEffect().value().isInstantenous()) {
						lanternBlockEntity.addEffect(new MobEffectInstance(instance));
						if (!player.getAbilities().instabuild) {
							player.setItemInHand(hand, Items.GLASS_BOTTLE.getDefaultInstance());
						}
						succeed = true;
					}
				}
				if (succeed) {
					return ItemInteractionResult.sidedSuccess(level.isClientSide);
				}
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
