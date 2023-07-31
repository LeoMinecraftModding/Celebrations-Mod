package cn.leolezury.celebrations.client.renderer;

import cn.leolezury.celebrations.Celebrations;
import cn.leolezury.celebrations.block.FuStickerBlock;
import cn.leolezury.celebrations.block.entity.FuStickerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FuStickerRenderer implements BlockEntityRenderer<FuStickerBlockEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Celebrations.MOD_ID, "fu_sticker"), "main");
    private final StickerModel stickerModel;
    public FuStickerRenderer(BlockEntityRendererProvider.Context context) {
        this.stickerModel = new StickerModel(context.bakeLayer(LAYER_LOCATION));
    }

    public void render(FuStickerBlockEntity entity, float f, PoseStack stack, MultiBufferSource bufferSource, int i, int i1) {
        BlockState blockstate = entity.getBlockState();
        FuStickerBlock stickerBlock = (FuStickerBlock) blockstate.getBlock();
        stack.pushPose();

        float angle = -stickerBlock.getYRotationDegrees(blockstate);
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.YP.rotationDegrees(angle));
        stack.translate(0, 0, -0.49F);

        stack.pushPose();
        stack.scale(1, -1, -1);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(stickerModel.renderType(stickerBlock.getTextureLocation()));
        this.stickerModel.root.render(stack, vertexconsumer, i, i1);
        stack.popPose();

        stack.popPose();
    }

    public static LayerDefinition createStickerLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @OnlyIn(Dist.CLIENT)
    public static final class StickerModel extends Model {
        public final ModelPart root;
        private final ModelPart main;

        public StickerModel(ModelPart root) {
            super(RenderType::entityCutoutNoCull);
            this.root = root;
            this.main = root.getChild("main");
        }

        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
}
