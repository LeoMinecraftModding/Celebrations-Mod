package cn.leolezury.celebrations.event;

import cn.leolezury.celebrations.Celebrations;
import cn.leolezury.celebrations.block.entity.LanternBlockEntity;
import cn.leolezury.celebrations.command.CelebrationsCommand;
import cn.leolezury.celebrations.init.ItemInit;
import cn.leolezury.celebrations.util.CBUtils;
import cn.leolezury.celebrations.util.CelebrationUtils;
import cn.leolezury.celebrations.world.CelebrationSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Celebrations.MOD_ID)
public class ServerEvents {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CelebrationsCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            CelebrationSavedData celebrationSavedData = CelebrationUtils.getCelebrationData(serverLevel);
            if (celebrationSavedData == null) {
                CelebrationUtils.putCelebrationData(serverLevel, serverLevel.getDataStorage().computeIfAbsent(CelebrationSavedData::load, CelebrationSavedData::create, "celebrations"));
            }
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.level instanceof ServerLevel serverLevel) {
            CelebrationSavedData celebrationSavedData = CelebrationUtils.getCelebrationData(serverLevel);
            if (celebrationSavedData != null) {
                celebrationSavedData.tick();
            }
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Villager villager && !villager.level().isClientSide) {
            if (villager.tickCount % 100 == 0) {
                if (CBUtils.getVillagerLanternBlock(villager).defaultBlockState().isAir()) {
                    List<Item> lanterns = List.of(ItemInit.CHINESE_STYLED_BAMBOO_LANTERN.get(), ItemInit.CHINESE_STYLED_PAPER_LANTERN.get(), ItemInit.CHINESE_STYLED_RED_LANTERN.get(), ItemInit.JAPANESE_STYLED_PAPER_LANTERN.get(), ItemInit.JAPANESE_STYLED_RED_LANTERN.get());
                    CBUtils.setVillagerLantern(villager, lanterns.get(villager.getRandom().nextInt(lanterns.size())).getDefaultInstance());
                }

                ServerLevel serverLevel = (ServerLevel) villager.level();
                if (villager.isBaby()) {
                    BlockPos blockPos = villager.blockPosition();
                    BlockPos lanternPos = null;

                    for (int x = -4; x <= 4; x++) {
                        for (int y = -2; y <= 4; y++) {
                            for (int z = -4; z <= 4; z++) {
                                if (serverLevel.getBlockEntity(blockPos.offset(x, y, z)) instanceof LanternBlockEntity lanternBlockEntity && lanternBlockEntity.getGift() != null && !lanternBlockEntity.getGift().isEmpty()) {
                                    lanternPos = blockPos.offset(x, y, z);
                                }
                            }
                        }
                    }

                    if (lanternPos != null) {
                        CompoundTag tag = villager.getPersistentData();
                        tag.putString("LanternDim", serverLevel.dimension().location().toString());
                        tag.putIntArray("LanternPos", new int[]{lanternPos.getX(), lanternPos.getY(), lanternPos.getZ()});
                    }
                }
                CelebrationSavedData celebrationSavedData = CelebrationUtils.getCelebrationData(serverLevel);
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
}
