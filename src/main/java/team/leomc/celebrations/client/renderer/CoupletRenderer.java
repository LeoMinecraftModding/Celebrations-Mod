package team.leomc.celebrations.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.leomc.celebrations.Celebrations;
import team.leomc.celebrations.block.entity.CoupletBlockEntity;

@OnlyIn(Dist.CLIENT)
public class CoupletRenderer extends SignLikeRenderer<CoupletBlockEntity, CoupletRenderer.CoupletModel> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Celebrations.id("couplet"), "main");

	public CoupletRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected CoupletModel makeModel(BlockEntityRendererProvider.Context context) {
		return new CoupletModel(context.bakeLayer(LAYER_LOCATION));
	}

	@Override
	protected RenderType getRenderType() {
		return model.renderType(Celebrations.id("textures/block/couplet.png"));
	}

	@Override
	protected void renderSignLikeModel(PoseStack stack, int i, int i1, Model model, VertexConsumer consumer) {
		CoupletModel coupletModel = (CoupletModel) model;
		coupletModel.root.render(stack, consumer, i, i1);
	}

	@Override
	public AABB getRenderBoundingBox(CoupletBlockEntity blockEntity) {
		return AABB.encapsulatingFullBlocks(blockEntity.getBlockPos(), blockEntity.getBlockPos().below());
	}

	public static CoupletModel createCoupletModel(EntityModelSet set) {
		return new CoupletModel(set.bakeLayer(LAYER_LOCATION));
	}

	public static LayerDefinition createCoupletLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -32.0F, 0.0F, 16.0F, 32.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@OnlyIn(Dist.CLIENT)
	public static final class CoupletModel extends Model {
		public final ModelPart root;
		private final ModelPart main;

		public CoupletModel(ModelPart root) {
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
