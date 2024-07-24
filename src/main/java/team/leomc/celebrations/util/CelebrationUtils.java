package team.leomc.celebrations.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import team.leomc.celebrations.world.CelebrationSavedData;

import java.util.HashMap;
import java.util.Map;

public class CelebrationUtils {
	private static final Map<ResourceKey<Level>, CelebrationSavedData> CELEBRATIONS = new HashMap<>();

	public static CelebrationSavedData getCelebrationData(ServerLevel serverLevel) {
		return CELEBRATIONS.get(serverLevel.dimension());
	}

	public static void putCelebrationData(ServerLevel serverLevel, CelebrationSavedData celebrationSavedData) {
		CELEBRATIONS.put(serverLevel.dimension(), celebrationSavedData);
	}

	public static boolean isCelebrating(ServerLevel serverLevel) {
		CelebrationSavedData celebrationSavedData = getCelebrationData(serverLevel);
		return celebrationSavedData != null && celebrationSavedData.isCelebrating();
	}

	public static void setCelebrating(ServerLevel serverLevel, boolean celebrating) {
		CelebrationSavedData celebrationSavedData = getCelebrationData(serverLevel);
		if (celebrationSavedData != null) {
			celebrationSavedData.setCelebrating(celebrating);
		}
	}

	public static void setTicksSinceLastCelebration(ServerLevel serverLevel, int ticksSinceLastCelebration) {
		CelebrationSavedData celebrationSavedData = getCelebrationData(serverLevel);
		if (celebrationSavedData != null) {
			celebrationSavedData.setTicksSinceLastCelebration(ticksSinceLastCelebration);
		}
	}

	public static int getTicksSinceLastCelebration(ServerLevel serverLevel) {
		CelebrationSavedData celebrationSavedData = getCelebrationData(serverLevel);
		return celebrationSavedData == null ? 0 : celebrationSavedData.getTicksSinceLastCelebration();
	}

	public static void setCelebrationTicks(ServerLevel serverLevel, int celebrationTicks) {
		CelebrationSavedData celebrationSavedData = getCelebrationData(serverLevel);
		if (celebrationSavedData != null) {
			celebrationSavedData.setCelebrationTicks(celebrationTicks);
		}
	}

	public static int getCelebrationTicks(ServerLevel serverLevel) {
		CelebrationSavedData celebrationSavedData = getCelebrationData(serverLevel);
		return celebrationSavedData == null ? 0 : celebrationSavedData.getCelebrationTicks();
	}
}
