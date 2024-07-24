package team.leomc.celebrations.event;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.block.entity.LanternBlockEntity;
import team.leomc.celebrations.command.CelebrationsCommand;
import team.leomc.celebrations.registry.CItems;
import team.leomc.celebrations.util.CelebrationUtils;
import team.leomc.celebrations.util.LanternUtils;
import team.leomc.celebrations.world.CelebrationSavedData;

import java.util.List;

@EventBusSubscriber(modid = Celebrations.ID)
public class CEvents {
	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event) {
		CelebrationsCommand.register(event.getDispatcher());
	}

	@SubscribeEvent
	public static void onLevelLoad(LevelEvent.Load event) {
		if (event.getLevel() instanceof ServerLevel serverLevel) {
			CelebrationSavedData celebrationSavedData = CelebrationUtils.getCelebrationData(serverLevel);
			if (celebrationSavedData == null) {
				CelebrationUtils.putCelebrationData(serverLevel, serverLevel.getDataStorage().computeIfAbsent(CelebrationSavedData.factory(), "celebrations"));
			}
		}
	}

	@SubscribeEvent
	public static void onLevelTick(LevelTickEvent.Post event) {
		if (event.getLevel() instanceof ServerLevel serverLevel) {
			CelebrationSavedData celebrationSavedData = CelebrationUtils.getCelebrationData(serverLevel);
			if (celebrationSavedData != null) {
				celebrationSavedData.tick();
			}
		}
	}

	@SubscribeEvent
	public static void onLivingTick(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof Villager villager && !villager.level().isClientSide) {
			if (villager.tickCount % 100 == 0) {
				if (LanternUtils.getVillagerLanternBlock(villager).defaultBlockState().isAir()) {
					List<Item> lanterns = List.of(CItems.CHINESE_STYLED_BAMBOO_LANTERN.get(), CItems.CHINESE_STYLED_PAPER_LANTERN.get(), CItems.CHINESE_STYLED_RED_LANTERN.get(), CItems.JAPANESE_STYLED_PAPER_LANTERN.get(), CItems.JAPANESE_STYLED_RED_LANTERN.get());
					LanternUtils.setVillagerLantern(villager, lanterns.get(villager.getRandom().nextInt(lanterns.size())).getDefaultInstance());
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
