package cn.leolezury.celebrations.block;

import cn.leolezury.celebrations.Celebrations;
import cn.leolezury.celebrations.block.entity.LanternBlockEntity;
import cn.leolezury.celebrations.init.BlockEntityInit;
import cn.leolezury.celebrations.item.LanternBlockItem;
import cn.leolezury.celebrations.util.CBNbtUtils;
import cn.leolezury.celebrations.util.CBTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CBLanternBlock extends BaseEntityBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public CBLanternBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
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
                CompoundTag tag = itemStack.getOrCreateTag();
                CBNbtUtils.writeLanternInfo(lanternBlockEntity, tag);
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
        return createTickerHelper(type, BlockEntityInit.LANTERN.get(), LanternBlockEntity::tick);
    }
    
    public BlockState updateShape(BlockState state, Direction dir, BlockState state1, LevelAccessor accessor, BlockPos p_249685_, BlockPos pos) {
        return dir == Direction.UP && !this.canSurvive(state, accessor, p_249685_) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, dir, state1, accessor, p_249685_, pos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        return reader.getBlockState(pos.above()).isFaceSturdy(reader, pos.above(), Direction.DOWN, SupportType.CENTER);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(CBTags.Items.LANTERN_IGNITERS) && !state.getValue(LIT)) {
            if (level.getBlockEntity(pos) instanceof LanternBlockEntity lanternBlockEntity) {
                if (lanternBlockEntity.getGift() != null && !lanternBlockEntity.getGift().isEmpty()) {
                    if (!level.isClientSide) {
                        player.sendSystemMessage(Component.translatable("message." + Celebrations.MOD_ID + ".cannot_ignite_due_to_gift").withStyle(ChatFormatting.ITALIC));
                    }
                    return InteractionResult.FAIL;
                }
            }
            level.setBlockAndUpdate(pos, state.setValue(LIT, true));
            if (!level.isClientSide && !player.getAbilities().instabuild) {
                if (!stack.isDamageableItem()) {
                    stack.shrink(1);
                    if (stack.is(Items.FIRE_CHARGE)) {
                        level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 2.0F, 1.0F);
                    }
                } else {
                    stack.hurtAndBreak(1, player, (p) -> {
                        p.broadcastBreakEvent(hand);
                    });
                    if (stack.is(Items.FLINT_AND_STEEL)) {
                        level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 2.0F, 1.0F);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        if (stack.is(Items.POTION) && level.getBlockEntity(pos) instanceof LanternBlockEntity lanternBlockEntity) {
            if (PotionUtils.getPotion(stack).equals(Potions.WATER)) {
                lanternBlockEntity.clearEffects();
                level.setBlockAndUpdate(pos, state.setValue(LIT, false));
                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(hand, Items.GLASS_BOTTLE.getDefaultInstance());
                }
                return InteractionResult.SUCCESS;
            }
            boolean succeed = false;
            for (MobEffectInstance instance : PotionUtils.getMobEffects(stack)) {
                if (!instance.getEffect().isInstantenous()) {
                    lanternBlockEntity.addEffect(new MobEffectInstance(instance));
                    if (!player.getAbilities().instabuild) {
                        player.setItemInHand(hand, Items.GLASS_BOTTLE.getDefaultInstance());
                    }
                    succeed = true;
                }
            }
            if (succeed) {
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
