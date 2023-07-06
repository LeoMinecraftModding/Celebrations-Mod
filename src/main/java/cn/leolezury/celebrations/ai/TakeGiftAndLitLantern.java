package cn.leolezury.celebrations.ai;

import cn.leolezury.celebrations.block.entity.LanternBlockEntity;
import cn.leolezury.celebrations.util.CelebrationUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

public class TakeGiftAndLitLantern extends Behavior<Villager> {
    private final float speedModifier;
    private BlockPos targetPos;
    private int ticksSinceReached = 0;
    private int coolDown;

    public TakeGiftAndLitLantern(float speed) {
        super(ImmutableMap.of(
                        MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_ABSENT,
                        MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                160, 240);
        this.speedModifier = speed;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Villager villager) {
        if (!CelebrationUtils.isCelebrating(level)) return false;
        if (coolDown-- > 0) return false;
        coolDown = Math.max(coolDown, 0);
        if (!villager.isBaby()) return false;
        if (!ForgeEventFactory.getMobGriefingEvent(level, villager) || level.random.nextInt(2) == 0) {
            coolDown = 1200;
            return false;
        }
        return true;
    }

    @Override
    protected void start(ServerLevel level, Villager villager, long gameTime) {
        this.coolDown = 20 * (level.random.nextInt(11) + 5);
        this.ticksSinceReached = 0;

        targetPos = getValidLanternPos(level, villager);
        if (targetPos != null) {
            villager.setItemSlot(EquipmentSlot.MAINHAND, Items.FLINT_AND_STEEL.getDefaultInstance());
            villager.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            villager.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, this.speedModifier, 1));
        }
    }

    @Override
    protected void stop(ServerLevel level, Villager villager, long gameTime) {
        super.stop(level, villager, gameTime);
        targetPos = null;
        villager.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Villager villager, long gameTime) {
        return targetPos != null && isValidLanternAt(level, targetPos);
    }

    @Override
    protected void tick(ServerLevel level, Villager villager, long gameTime) {
        if (targetPos != null) {
            villager.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            villager.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, this.speedModifier, 2));
            villager.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(targetPos));

            if (targetPos.closerToCenterThan(villager.position(), 2)) {
                this.ticksSinceReached++;

                if (ticksSinceReached > 40) {
                    if (level.getBlockEntity(targetPos) instanceof LanternBlockEntity lanternBlockEntity) {
                        villager.setItemSlot(EquipmentSlot.MAINHAND, lanternBlockEntity.getGift());
                        lanternBlockEntity.setGift(ItemStack.EMPTY);
                        lanternBlockEntity.setGiftOwnerType("");
                        lanternBlockEntity.setGiftOwnerName("");
                    }
                    BlockState lanternState = level.getBlockState(targetPos);
                    if (lanternState.hasProperty(BlockStateProperties.LIT) && !lanternState.getValue(BlockStateProperties.LIT)) {
                        level.setBlockAndUpdate(targetPos, lanternState.setValue(BlockStateProperties.LIT, true));
                        level.playSound(null, targetPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 2.0f, 1.0f);
                    }

                    targetPos = null;
                }
            }
        }
    }

    @Nullable
    private static BlockPos getValidLanternPos(ServerLevel level, LivingEntity villager) {
        BlockPos blockPos = villager.blockPosition();

        for (int x = -4; x <= 4; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -4; z <= 4; z++) {
                    if (level.getBlockEntity(blockPos.offset(x, y, z)) instanceof LanternBlockEntity lanternBlockEntity && lanternBlockEntity.getGift() != null && !lanternBlockEntity.getGift().isEmpty()) {
                        return blockPos.offset(x, y, z);
                    }
                }
            }
        }

        return null;
    }

    public static boolean isValidLanternAt(ServerLevel serverLevel, BlockPos pos) {
        return serverLevel.getBlockEntity(pos) instanceof LanternBlockEntity lanternBlockEntity && lanternBlockEntity.getGift() != null && !lanternBlockEntity.getGift().isEmpty();
    }
}
