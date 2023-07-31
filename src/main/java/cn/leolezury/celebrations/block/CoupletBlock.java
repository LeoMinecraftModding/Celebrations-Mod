package cn.leolezury.celebrations.block;

import cn.leolezury.celebrations.block.entity.CoupletBlockEntity;
import cn.leolezury.celebrations.block.entity.CoupletText;
import cn.leolezury.celebrations.init.BlockEntityInit;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class CoupletBlock extends WallSignBlock {
    private String descriptionId;
    private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(0.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D), Direction.SOUTH, Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D), Direction.EAST, Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D), Direction.WEST, Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)));

    public CoupletBlock(Properties properties) {
        super(properties, WoodType.OAK);
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CoupletBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T>type) {
        return createTickerHelper(type, BlockEntityInit.COUPLET.get(), CoupletBlockEntity::tick);
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return AABBS.get(state.getValue(FACING));
    }

    public Vec3 getSignHitboxCenterPosition(BlockState state) {
        VoxelShape shape = AABBS.get(state.getValue(FACING));
        return shape.bounds().getCenter();
    }

    private boolean tryApplyDyeToCouplet(Level level, CoupletBlockEntity entity, DyeItem item, boolean bool) {
        if (entity.updateCoupletText((text) -> {
            return text.setColor(item.getDyeColor());
        }, bool)) {
            level.playSound(null, entity.getBlockPos(), SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        } else {
            return false;
        }
    }

    private boolean tryApplyInkSacToCouplet(Level level, CoupletBlockEntity entity, boolean bool) {
        if (entity.updateCoupletText((text) -> {
            return text.setHasGlowingText(false);
        }, bool)) {
            level.playSound(null, entity.getBlockPos(), SoundEvents.INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        } else {
            return false;
        }
    }

    private boolean tryApplyGlowInkSacToCouplet(Level level, CoupletBlockEntity entity, boolean bool) {
        if (entity.updateCoupletText((text) -> {
            return text.setHasGlowingText(true);
        }, bool)) {
            level.playSound(null, entity.getBlockPos(), SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        } else {
            return false;
        }
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        boolean isValidCoupletApplicator = item instanceof DyeItem|| item instanceof InkSacItem || item instanceof GlowInkSacItem;
        boolean flag1 = isValidCoupletApplicator && player.mayBuild();
        BlockEntity $$12 = level.getBlockEntity(pos);
        if ($$12 instanceof CoupletBlockEntity coupletBlockEntity) {
            if (!level.isClientSide) {
                boolean flag2 = coupletBlockEntity.isFacingFrontText(player);
                boolean flag = coupletBlockEntity.executeClickCommandsIfPresent(player, level, pos, flag2);
                if (coupletBlockEntity.isWaxed()) {
                    level.playSound((Player)null, coupletBlockEntity.getBlockPos(), SoundEvents.WAXED_SIGN_INTERACT_FAIL, SoundSource.BLOCKS);
                    return InteractionResult.PASS;
                } else if (flag1 && !this.otherPlayerIsEditingCouplet(player, coupletBlockEntity)
                        && ((item instanceof DyeItem dyeItem && tryApplyDyeToCouplet(level, coupletBlockEntity, dyeItem, flag2))
                        || (item instanceof InkSacItem && tryApplyInkSacToCouplet(level, coupletBlockEntity, flag2))
                        || (item instanceof GlowInkSacItem && tryApplyGlowInkSacToCouplet(level, coupletBlockEntity, flag2))))
                {
                    if (!player.isCreative()) {
                        itemstack.shrink(1);
                    }

                    level.gameEvent(GameEvent.BLOCK_CHANGE, coupletBlockEntity.getBlockPos(), GameEvent.Context.of(player, coupletBlockEntity.getBlockState()));
                    player.awardStat(Stats.ITEM_USED.get(item));
                    return InteractionResult.SUCCESS;
                } else if (flag) {
                    return InteractionResult.SUCCESS;
                } else if (!this.otherPlayerIsEditingCouplet(player, coupletBlockEntity) && player.mayBuild() && this.hasEditableText(player, coupletBlockEntity, flag2)) {
                    this.openTextEdit(player, coupletBlockEntity, flag2);
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.PASS;
                }
            } else {
                return !flag1 && !coupletBlockEntity.isWaxed() ? InteractionResult.CONSUME : InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    private boolean hasEditableText(Player player, CoupletBlockEntity entity, boolean b) {
        CoupletText coupletText = entity.getText(b);
        return Arrays.stream(coupletText.getMessages(player.isTextFilteringEnabled())).allMatch((component) -> {
            return component.equals(CommonComponents.EMPTY) || component.getContents() instanceof LiteralContents;
        });
    }

    private boolean otherPlayerIsEditingCouplet(Player player, CoupletBlockEntity entity) {
        UUID uuid = entity.getPlayerWhoMayEdit();
        return uuid != null && !uuid.equals(player.getUUID());
    }

    @Override
    public @NotNull String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("block", BuiltInRegistries.BLOCK.getKey(this));
        }

        return this.descriptionId;
    }
}
