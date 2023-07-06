package cn.leolezury.celebrations.ai;

import cn.leolezury.celebrations.mixin.accessor.BrainAccessor;
import cn.leolezury.celebrations.util.CelebrationUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Map;
import java.util.Set;

public class VillagerCelebrationAI {
    public static void initVillagerBrain(Brain<Villager> brain, Villager villager) {
        if (villager != null && villager.level() instanceof ServerLevel serverLevel && CelebrationUtils.isCelebrating(serverLevel)) {
            if (villager.isBaby()) {
                addTaskToActivity(brain, Activity.PLAY, Pair.of(9, new TakeGiftAndLitLantern(1.0f)));
            } else {
                addTaskToActivity(brain, Activity.IDLE, Pair.of(3, new PlaceLantern(0.8f)));
            }
        }
    }

    private static <T extends Pair<Integer, ? extends Behavior<Villager>>> void addTaskToActivity(Brain<Villager> brain, Activity activity, T task) {
        try {
            Map<Integer, Map<Activity, Set<Behavior<Villager>>>> map = (Map<Integer, Map<Activity, Set<Behavior<Villager>>>>) ((BrainAccessor) brain).getAvailableBehaviorsByPriority();

            Map<Activity, Set<Behavior<Villager>>> tasksWithSamePriority = map.computeIfAbsent(task.getFirst(), (m) -> Maps.newHashMap());

            Set<Behavior<Villager>> activityTaskSet = tasksWithSamePriority.computeIfAbsent(activity, (a) -> Sets.newLinkedHashSet());

            activityTaskSet.add(task.getSecond());
        } catch (Exception ignored) {

        }
    }
}
