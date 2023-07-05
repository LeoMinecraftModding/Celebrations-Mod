package cn.leolezury.celebrations.util;

import cn.leolezury.celebrations.world.CelebrationSavedData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class CelebrationUtils {
    public static Map<ResourceKey<Level>, CelebrationSavedData> celebrationSavedDataMap = new HashMap<>();

    public static CelebrationSavedData getCelebrationData(ServerLevel serverLevel) {
        return celebrationSavedDataMap.get(serverLevel.dimension());
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
