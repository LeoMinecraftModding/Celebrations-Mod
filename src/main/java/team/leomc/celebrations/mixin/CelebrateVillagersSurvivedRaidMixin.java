package team.leomc.celebrations.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.CelebrateVillagersSurvivedRaid;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.leomc.celebrations.event.CEvents;

@Mixin(CelebrateVillagersSurvivedRaid.class)
public class CelebrateVillagersSurvivedRaidMixin {
	@Inject(method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/Villager;J)V", at = @At("TAIL"))
	protected void tick(ServerLevel level, Villager owner, long gameTime, CallbackInfo ci) {
		owner.getPersistentData().putInt(CEvents.TAG_RAID_WON_CELEBRATION_TICKS_LEFT, 48000);
	}
}
