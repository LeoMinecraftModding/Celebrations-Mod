package cn.leolezury.celebrations.ai;

import cn.leolezury.celebrations.util.CelebrationUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.ForgeEventFactory;

public class RemoveLantern extends Behavior<Villager> {
    private final float speedModifier;
    private BlockPos targetPos;
    private int ticksSinceReached = 0;
    private int coolDown;

    public RemoveLantern(float speed) {
        super(ImmutableMap.of(
                        MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_ABSENT,
                        MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                160, 240);
        this.speedModifier = speed;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Villager villager) {
        if (CelebrationUtils.isCelebrating(level)) return false;
        if (coolDown-- > 0) return false;
        coolDown = Math.max(coolDown, 0);
        if (villager.isBaby()) return false;
        if (!ForgeEventFactory.getMobGriefingEvent(level, villager)) {
            coolDown = 1200;
            return false;
        }
        if (villager.getPersistentData().getString("LanternDim").isEmpty() || (villager.getPersistentData().getIntArray("LanternPos").length < 3)) {
            coolDown = 12000;
            return false;
        }
        return true;
    }

    @Override
    protected void start(ServerLevel level, Villager villager, long gameTime) {
        this.coolDown = 20 * (level.random.nextInt(11) + 5);
        this.ticksSinceReached = 0;

        String lanternDim = villager.getPersistentData().getString("LanternDim");
        int[] lanternPos = villager.getPersistentData().getIntArray("LanternPos");
        if (lanternDim.equals(villager.level().dimension().location().toString()) && lanternPos.length >= 3) {
            targetPos = new BlockPos(lanternPos[0] ,lanternPos[1], lanternPos[2]);
        }
        if (targetPos != null) {
            villager.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            villager.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, this.speedModifier, 1));
            villager.setItemSlot(EquipmentSlot.MAINHAND, Items.IRON_AXE.getDefaultInstance());
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
        return targetPos != null;
    }

    @Override
    protected void tick(ServerLevel level, Villager villager, long gameTime) {
        if (targetPos != null) {
            villager.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            villager.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, this.speedModifier, 2));
            villager.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(targetPos));

            if (targetPos.closerToCenterThan(villager.position(), 3)) {
                this.ticksSinceReached++;
                if (ticksSinceReached > 10) {
                    level.setBlockAndUpdate(targetPos, Blocks.AIR.defaultBlockState());
                    CompoundTag tag = villager.getPersistentData();
                    tag.putString("LanternDim", "");
                    tag.putIntArray("LanternPos", new int[]{});
                    targetPos = null;
                }
            }
        }
    }
}
