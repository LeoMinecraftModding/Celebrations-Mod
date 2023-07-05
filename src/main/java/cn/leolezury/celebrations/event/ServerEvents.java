package cn.leolezury.celebrations.event;

import cn.leolezury.celebrations.Celebrations;
import cn.leolezury.celebrations.util.CelebrationUtils;
import cn.leolezury.celebrations.world.CelebrationSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Celebrations.MOD_ID)
public class ServerEvents {
    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.level instanceof ServerLevel serverLevel) {
            CelebrationSavedData celebrationSavedData = CelebrationUtils.celebrationSavedDataMap.get(serverLevel.dimension());
            if (celebrationSavedData == null) {
                CelebrationUtils.celebrationSavedDataMap.put(serverLevel.dimension(), serverLevel.getDataStorage().computeIfAbsent(CelebrationSavedData::load, CelebrationSavedData::create, "celebrations"));
            } else {
                celebrationSavedData.tick();
            }
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Villager villager && !villager.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) villager.level();
            CelebrationSavedData celebrationSavedData = CelebrationUtils.celebrationSavedDataMap.get(serverLevel.dimension());
            if (celebrationSavedData != null) {
                boolean refreshed = villager.getPersistentData().getBoolean("BrainRefreshedForCelebration");
                boolean should = celebrationSavedData.isCelebrating();
                if (refreshed != should) {
                    villager.refreshBrain(serverLevel);
                    villager.getPersistentData().putBoolean("BrainRefreshedForCelebration", should);
                }
            }
        }
    }
}
