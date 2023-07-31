package cn.leolezury.celebrations.ai;

import cn.leolezury.celebrations.block.entity.LanternBlockEntity;
import cn.leolezury.celebrations.util.CBUtils;
import cn.leolezury.celebrations.util.CelebrationUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

public class PlaceLantern extends Behavior<Villager> {
    private final float speedModifier;
    private BlockPos targetPos;
    private int ticksSinceReached = 0;
    private int coolDown;

    public PlaceLantern(float speed) {
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
        if (villager.isBaby()) return false;
        if (!ForgeEventFactory.getMobGriefingEvent(level, villager)) {
            coolDown = 1200;
            return false;
        }
        if (CBUtils.getVillagerLanternBlock(villager).defaultBlockState().isAir() || (!villager.getPersistentData().getString("LanternDim").isEmpty() && (villager.getPersistentData().getIntArray("LanternPos").length >= 3))) {
            coolDown = 12000;
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
            villager.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            villager.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, this.speedModifier, 1));
            villager.setItemSlot(EquipmentSlot.MAINHAND, CBUtils.getVillagerLanternItem(villager));
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
        return targetPos != null && canPlaceLantern(level, targetPos);
    }

    @Override
    protected void tick(ServerLevel level, Villager villager, long gameTime) {
        if (targetPos != null) {
            villager.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            villager.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, this.speedModifier, 2));
            villager.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(targetPos));

            if (targetPos.closerToCenterThan(villager.position(), 5)) {
                this.ticksSinceReached++;
                if (ticksSinceReached > 10) {
                    BlockState state = CBUtils.getVillagerLanternBlock(villager).defaultBlockState();
                    level.setBlockAndUpdate(targetPos, state);
                    SoundType soundType = state.getSoundType();
                    level.playSound(null, targetPos, soundType.getPlaceSound(), SoundSource.BLOCKS, soundType.getVolume(), soundType.getPitch());

                    if (level.getBlockEntity(targetPos) instanceof LanternBlockEntity lanternBlockEntity) {
                        // TODO: loot table
                        ItemStack gift = Items.EMERALD.getDefaultInstance();

                        lanternBlockEntity.setGift(gift);
                        lanternBlockEntity.setGiftOwnerType("Mob");
                        lanternBlockEntity.setGiftOwnerName(villager.getDisplayName().getString());
                    }

                    CompoundTag tag = villager.getPersistentData();
                    tag.putString("LanternDim", level.dimension().location().toString());
                    tag.putIntArray("LanternPos", new int[]{targetPos.getX(), targetPos.getY(), targetPos.getZ()});
                    targetPos = null;
                }
            }
        }
    }

    @Nullable
    private static BlockPos getValidLanternPos(ServerLevel level, LivingEntity villager) {
        RandomSource random = villager.getRandom();
        BlockPos blockPos = villager.blockPosition();

        for (int i = 0; i < 64; i++) {
            BlockPos pos = blockPos.offset(random.nextInt(21) - 10, random.nextInt(7) - 3, random.nextInt(21) - 10);
            if (canPlaceLantern(level, pos)) {
                return pos;
            }
        }

        return null;
    }

    public static boolean canPlaceLantern(ServerLevel serverLevel, BlockPos pos) {
        return serverLevel.getBlockState(pos).isAir() && serverLevel.getBlockState(pos.above()).isFaceSturdy(serverLevel, pos.above(), Direction.DOWN, SupportType.CENTER);
    }
}
