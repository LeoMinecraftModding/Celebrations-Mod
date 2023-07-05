package cn.leolezury.celebrations.mixin;

import cn.leolezury.celebrations.ai.VillagerCelebrationAI;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerMixin {
    @Inject(method = ("registerBrainGoals"), at = @At("RETURN"))
    protected void registerBrainGoals(Brain<Villager> brain, CallbackInfo ci) {
        VillagerCelebrationAI.initVillagerBrain(brain, ((Villager) (Object) this).level());
    }
}
