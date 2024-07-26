package team.leomc.celebrations.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.AllayModel;
import net.minecraft.client.model.geom.ModelPart;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import team.leomc.celebrations.client.renderer.layer.PartyHatParent;

@OnlyIn(Dist.CLIENT)
@Mixin(AllayModel.class)
public abstract class AllayModelMixin implements PartyHatParent {
	@Shadow
	@Final
	private ModelPart head;

	@Shadow
	public abstract ModelPart root();

	@Override
	public void translate(PoseStack stack) {
		this.root().translateAndRotate(stack);
		this.head.translateAndRotate(stack);
		stack.mulPose(Axis.YP.rotationDegrees(180.0F));
		stack.scale(1, -1, -1);
		stack.translate(0, -0.125, 0);
	}
}
