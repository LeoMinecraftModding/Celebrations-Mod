package team.leomc.celebrations.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.block.entity.HorizontalScrollBlockEntity;

@OnlyIn(Dist.CLIENT)
public class HorizontalScrollRenderer extends SignLikeRenderer<HorizontalScrollBlockEntity, HorizontalScrollRenderer.ScrollModel> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Celebrations.id("horizontal_scroll"), "main");

	public HorizontalScrollRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected ScrollModel makeModel(BlockEntityRendererProvider.Context context) {
		return new ScrollModel(context.bakeLayer(LAYER_LOCATION));
	}

	@Override
	protected void translateSignLike(PoseStack stack, float f) {
		stack.translate(0.5F, 1.5F, 0.5F);
		stack.mulPose(Axis.YP.rotationDegrees(f));
		stack.translate(0, 0, -0.49F);
	}

	@Override
	protected RenderType getRenderType() {
		return model.renderType(Celebrations.id("textures/block/horizontal_scroll.png"));
	}

	@Override
	protected void renderSignLikeModel(PoseStack stack, int i, int i1, Model model, VertexConsumer consumer) {
		ScrollModel scrollModel = (ScrollModel) model;
		scrollModel.root.render(stack, consumer, i, i1);
	}

	@Override
	protected void translateSignLikeText(PoseStack stack) {
		float scale = 1.5f;
		float f = 0.015625F * scale;
		stack.translate(0.3f / 16f, -22f / 16f, 0.001);
		stack.scale(f, -f, f);
	}

	public static ScrollModel createScrollModel(EntityModelSet set) {
		return new ScrollModel(set.bakeLayer(LAYER_LOCATION));
	}

	public static LayerDefinition createScrollLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@OnlyIn(Dist.CLIENT)
	public static final class ScrollModel extends Model {
		public final ModelPart root;
		private final ModelPart main;

		public ScrollModel(ModelPart root) {
			super(RenderType::entityCutoutNoCull);
			this.root = root;
			this.main = root.getChild("main");
		}

		@Override
		public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
			main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		}
	}
}
