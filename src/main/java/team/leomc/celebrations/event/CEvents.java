package team.leomc.celebrations.event;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.ai.VillagerCelebrationAI;
import team.leomc.celebrations.block.entity.LanternBlockEntity;
import team.leomc.celebrations.command.CelebrationsCommand;
import team.leomc.celebrations.entity.Balloon;
import team.leomc.celebrations.entity.BalloonOwner;
import team.leomc.celebrations.network.UpdateMobPartyHatPayload;
import team.leomc.celebrations.registry.CEntities;
import team.leomc.celebrations.registry.CItems;
import team.leomc.celebrations.util.CelebrationUtils;
import team.leomc.celebrations.util.LanternUtils;
import team.leomc.celebrations.world.CelebrationSavedData;

import java.util.Calendar;
import java.util.List;

@EventBusSubscriber(modid = Celebrations.ID)
public class CEvents {
	public static final String TAG_PARTY_HAT = Celebrations.id("party_hat").toString();
	public static final String TAG_RAID_WON_CELEBRATION_TICKS_LEFT = Celebrations.id("raid_won_celebration_ticks_left").toString();
	public static final String TAG_PLACE_CANDLE_CELEBRATION_TICKS_LEFT = Celebrations.id("place_candle_celebration_ticks_left").toString();

	@SubscribeEvent
	private static void onRegisterCommands(RegisterCommandsEvent event) {
		CelebrationsCommand.register(event.getDispatcher());
	}

	@SubscribeEvent
	private static void onLevelLoad(LevelEvent.Load event) {
		if (event.getLevel() instanceof ServerLevel serverLevel) {
			CelebrationSavedData celebrationSavedData = CelebrationUtils.getCelebrationData(serverLevel);
			if (celebrationSavedData == null) {
				CelebrationUtils.putCelebrationData(serverLevel, serverLevel.getDataStorage().computeIfAbsent(CelebrationSavedData.factory(), "celebrations"));
			}
		}
	}

	@SubscribeEvent
	private static void onPostLevelTick(LevelTickEvent.Post event) {
		if (event.getLevel() instanceof ServerLevel serverLevel) {
			CelebrationSavedData celebrationSavedData = CelebrationUtils.getCelebrationData(serverLevel);
			if (celebrationSavedData != null) {
				celebrationSavedData.tick();
			}
		}
	}

	@SubscribeEvent
	private static void onPostEntityTick(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof Mob mob && !mob.level().isClientSide && mob.tickCount % 15 == 0) {
			mob.getPersistentData().putBoolean(TAG_PARTY_HAT, false);
			int month = Calendar.getInstance().get(Calendar.MONTH);
			int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			// all mobs
			if (month == Calendar.MAY && day >= 15 && day <= 30) {
				mob.getPersistentData().putBoolean(TAG_PARTY_HAT, true);
			}
			// all villagers
			if (mob instanceof Villager villager) {
				int ticks = mob.getPersistentData().getInt(TAG_RAID_WON_CELEBRATION_TICKS_LEFT);
				mob.getPersistentData().putInt(TAG_RAID_WON_CELEBRATION_TICKS_LEFT, Math.max(ticks - 40, 0));
				if (month == Calendar.SEPTEMBER && day == 22
					|| month == Calendar.DECEMBER && day == 12
					|| month == Calendar.APRIL && day == 23
					|| mob.getPersistentData().getInt(TAG_RAID_WON_CELEBRATION_TICKS_LEFT) > 0) {
					mob.getPersistentData().putBoolean(TAG_PARTY_HAT, true);
				}
				if (villager.getVillagerData().getProfession() == VillagerProfession.NITWIT && month == Calendar.AUGUST && day == 11) {
					mob.getPersistentData().putBoolean(TAG_PARTY_HAT, true);
				}
			}
			// zombie villager
			if (mob instanceof ZombieVillager
				&& (month == Calendar.SEPTEMBER && day == 22
				|| month == Calendar.DECEMBER && day == 12
				|| month == Calendar.APRIL && day == 23) && RandomSource.create(mob.getId()).nextInt(3) == 0) {
				mob.getPersistentData().putBoolean(TAG_PARTY_HAT, true);
			}
			// wandering trader
			if (mob instanceof WanderingTrader && (month == Calendar.JANUARY && day == 30 || month == Calendar.APRIL && day == 23)) {
				mob.getPersistentData().putBoolean(TAG_PARTY_HAT, true);
			}
			// all illagers
			if (mob instanceof AbstractIllager
				&& (month == Calendar.MARCH && day == 25
				|| month == Calendar.APRIL && day == 23
				|| month == Calendar.MAY && day == 26)) {
				mob.getPersistentData().putBoolean(TAG_PARTY_HAT, true);
			}
			// evoker, vex, vindicator
			if ((mob instanceof Evoker || mob instanceof Vex || mob instanceof Vindicator) && (month == Calendar.SEPTEMBER && day == 28 || month == Calendar.NOVEMBER && day == 14)) {
				mob.getPersistentData().putBoolean(TAG_PARTY_HAT, true);
			}
			// pillager
			if (mob instanceof Pillager && month == Calendar.OCTOBER && day == 24) {
				mob.getPersistentData().putBoolean(TAG_PARTY_HAT, true);
			}
			// piglin
			if (mob instanceof Piglin && (month == Calendar.FEBRUARY && day == 14 || month == Calendar.JUNE && day == 23)) {
				mob.getPersistentData().putBoolean(TAG_PARTY_HAT, true);
			}
			// piglin brute
			if (mob instanceof PiglinBrute
				&& (month == Calendar.FEBRUARY && day == 13
				|| month == Calendar.JUNE && day == 23
				|| month == Calendar.JULY && day == 1)) {
				mob.getPersistentData().putBoolean(TAG_PARTY_HAT, true);
			}
			// allay
			if (mob instanceof Allay) {
				int ticks = mob.getPersistentData().getInt(TAG_PLACE_CANDLE_CELEBRATION_TICKS_LEFT);
				mob.getPersistentData().putInt(TAG_PLACE_CANDLE_CELEBRATION_TICKS_LEFT, Math.max(ticks - 40, 0));
				if (mob.getPersistentData().getInt(TAG_PLACE_CANDLE_CELEBRATION_TICKS_LEFT) > 0) {
					mob.getPersistentData().putBoolean(TAG_PARTY_HAT, true);
				}
			}
			PacketDistributor.sendToAllPlayers(new UpdateMobPartyHatPayload(mob.getId(), mob.getPersistentData().getBoolean(TAG_PARTY_HAT)));
		}
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
						tag.putString(VillagerCelebrationAI.TAG_LANTERN_DIMENSION, serverLevel.dimension().location().toString());
						tag.putIntArray(VillagerCelebrationAI.TAG_LANTERN_POS, new int[]{lanternPos.getX(), lanternPos.getY(), lanternPos.getZ()});
					}
				}
				CelebrationSavedData celebrationSavedData = CelebrationUtils.getCelebrationData(serverLevel);
				if (celebrationSavedData != null) {
					boolean refreshed = villager.getPersistentData().getBoolean(VillagerCelebrationAI.TAG_BRAIN_REFRESHED_FOR_CELEBRATION);
					boolean should = celebrationSavedData.isCelebrating();
					if (refreshed != should) {
						villager.refreshBrain(serverLevel);
						villager.getPersistentData().putBoolean(VillagerCelebrationAI.TAG_BRAIN_REFRESHED_FOR_CELEBRATION, should);
					}
				}
			}
		}
		if (event.getEntity() instanceof Player player && player instanceof BalloonOwner owner && !player.level().isClientSide) {
			if (owner.getMainHandBalloon() != null && (owner.getMainHandBalloon().getOwner() != player || !owner.getMainHandBalloon().isMainHand() || owner.getMainHandBalloon().isRemoved())) {
				owner.setMainHandBalloon(null);
			}
			if (owner.getOffhandBalloon() != null && (owner.getOffhandBalloon().getOwner() != player || owner.getOffhandBalloon().isMainHand() || owner.getOffhandBalloon().isRemoved())) {
				owner.setOffhandBalloon(null);
			}
			if (player.getMainHandItem().is(CItems.BALLOON.get()) && (owner.getMainHandBalloon() == null || owner.getMainHandBalloon().isRemoved() || owner.getMainHandBalloon().getOwner() != player)) {
				Balloon balloon = new Balloon(CEntities.BALLOON.get(), player.level());
				balloon.setOwner(player);
				balloon.setItem(player.getMainHandItem());
				balloon.setMainHand(true);
				balloon.setPos(balloon.getTargetPos(1));
				player.level().addFreshEntity(balloon);
				owner.setMainHandBalloon(balloon);
			} else if (player.getOffhandItem().is(CItems.BALLOON.get()) && (owner.getOffhandBalloon() == null || owner.getOffhandBalloon().isRemoved() || owner.getOffhandBalloon().getOwner() != player)) {
				Balloon balloon = new Balloon(CEntities.BALLOON.get(), player.level());
				balloon.setOwner(player);
				balloon.setItem(player.getOffhandItem());
				balloon.setMainHand(false);
				balloon.setPos(balloon.getTargetPos(1));
				player.level().addFreshEntity(balloon);
				owner.setOffhandBalloon(balloon);
			}

		}
	}
}
