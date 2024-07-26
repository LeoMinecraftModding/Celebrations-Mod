package team.leomc.celebrations.mixin;

import net.minecraft.client.renderer.entity.AllayRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.leomc.celebrations.client.renderer.layer.PartyHatLayer;

@OnlyIn(Dist.CLIENT)
@Mixin(AllayRenderer.class)
public abstract class AllayRendererMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	protected void init(EntityRendererProvider.Context context, CallbackInfo ci) {
		AllayRenderer renderer = ((AllayRenderer) (Object) this);
		renderer.addLayer(new PartyHatLayer(renderer));
	}
}
