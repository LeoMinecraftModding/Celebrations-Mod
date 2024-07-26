package team.leomc.celebrations.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface PartyHatParent {
	void translate(PoseStack stack);
}
