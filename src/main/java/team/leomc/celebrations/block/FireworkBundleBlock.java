package team.leomc.celebrations.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import team.leomc.celebrations.block.entity.FireworkBundleBlockEntity;
import team.leomc.celebrations.registry.CBlockEntities;
import team.leomc.celebrations.registry.CDataComponents;
import team.leomc.celebrations.util.CTags;

import java.util.List;

public class FireworkBundleBlock extends BaseEntityBlock {
	public static final MapCodec<FireworkBundleBlock> CODEC = simpleCodec(FireworkBundleBlock::new);
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final IntegerProperty FIREWORK_AMOUNT = IntegerProperty.create("firework_amount", 0, 64);

	public FireworkBundleBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(FIREWORK_AMOUNT, 0));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIT, FIREWORK_AMOUNT);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FireworkBundleBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, CBlockEntities.FIREWORK_BUNDLE.get(), FireworkBundleBlockEntity::tick);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
		ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
		if (state.hasProperty(FIREWORK_AMOUNT)) {
			stack.set(CDataComponents.FIREWORK_AMOUNT.get(), state.getValue(FIREWORK_AMOUNT));
		}
		return stack;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		List<ItemStack> drops = super.getDrops(state, builder);
		BlockState blockState = builder.getOptionalParameter(LootContextParams.BLOCK_STATE);
		if (blockState != null) {
			for (int i = 0; i < drops.size(); i++) {
				ItemStack itemStack = drops.get(i);
				if (blockState.hasProperty(FIREWORK_AMOUNT)) {
					itemStack.set(CDataComponents.FIREWORK_AMOUNT.get(), blockState.getValue(FIREWORK_AMOUNT));
				}
				drops.set(i, itemStack);
			}
		}
		return drops;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.is(CTags.Items.IGNITERS) && state.getValue(FIREWORK_AMOUNT) > 0) {
			level.setBlockAndUpdate(pos, state.setValue(LIT, true));
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
			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}
		if (stack.is(Items.FIREWORK_ROCKET)) {
			int amount = state.getValue(FIREWORK_AMOUNT);
			if (amount < 64) {
				level.setBlockAndUpdate(pos, state.setValue(FIREWORK_AMOUNT, amount + 1));
				stack.consume(1, player);
				return ItemInteractionResult.sidedSuccess(level.isClientSide);
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
