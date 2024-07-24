package team.leomc.celebrations.mixin;

import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.leomc.celebrations.ai.VillagerCelebrationAI;

@Mixin(Villager.class)
public abstract class VillagerMixin {
	@Inject(method = ("registerBrainGoals"), at = @At("RETURN"))
	protected void registerBrainGoals(Brain<Villager> brain, CallbackInfo ci) {
		VillagerCelebrationAI.initVillagerBrain(brain, ((Villager) (Object) this));
	}
}
