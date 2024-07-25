package team.leomc.celebrations.ai;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.util.CelebrationUtils;

import java.util.Map;
import java.util.Set;

public class VillagerCelebrationAI {
	public static final String TAG_LANTERN_DIMENSION = Celebrations.id("lantern_dimension").toString();
	public static final String TAG_LANTERN_POS = Celebrations.id("lantern_pos").toString();
	public static final String TAG_BRAIN_REFRESHED_FOR_CELEBRATION = Celebrations.id("brain_refreshed_for_celebration").toString();

	public static void initVillagerBrain(Brain<Villager> brain, Villager villager) {
		if (villager != null && villager.level() instanceof ServerLevel serverLevel) {
			if (CelebrationUtils.isCelebrating(serverLevel)) {
				if (villager.isBaby()) {
					addTaskToActivity(brain, Activity.PLAY, Pair.of(0, new TakeGiftAndLitLantern(1.0f)));
				} else {
					addTaskToActivity(brain, Activity.IDLE, Pair.of(0, new PlaceLantern(0.8f)));
				}
			} else if (!villager.isBaby()) {
				addTaskToActivity(brain, Activity.IDLE, Pair.of(0, new RemoveLantern(0.8f)));
			}
		}
	}

	private static <T extends Pair<Integer, ? extends Behavior<Villager>>> void addTaskToActivity(Brain<Villager> brain, Activity activity, T task) {
		try {
			Map<Integer, Map<Activity, Set<Behavior<Villager>>>> map = (Map<Integer, Map<Activity, Set<Behavior<Villager>>>>) (Object) brain.availableBehaviorsByPriority;

			Map<Activity, Set<Behavior<Villager>>> tasksWithSamePriority = map.computeIfAbsent(task.getFirst(), (m) -> Maps.newHashMap());

			Set<Behavior<Villager>> activityTaskSet = tasksWithSamePriority.computeIfAbsent(activity, (a) -> Sets.newLinkedHashSet());

			activityTaskSet.add(task.getSecond());
		} catch (Exception ignored) {

		}
	}
}
