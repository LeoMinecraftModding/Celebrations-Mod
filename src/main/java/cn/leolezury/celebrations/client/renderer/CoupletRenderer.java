package cn.leolezury.celebrations.client.renderer;

import cn.leolezury.celebrations.Celebrations;
import cn.leolezury.celebrations.block.CoupletBlock;
import cn.leolezury.celebrations.block.entity.CoupletBlockEntity;
import cn.leolezury.celebrations.block.entity.CoupletText;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CoupletRenderer implements BlockEntityRenderer<CoupletBlockEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Celebrations.MOD_ID, "couplet"), "main");
    private static final int OUTLINE_RENDER_DISTANCE = Mth.square(16);
    private final Font font;
    private final CoupletModel coupletModel;

    public CoupletRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
        this.coupletModel = new CoupletModel(context.bakeLayer(LAYER_LOCATION));
    }

    public void render(CoupletBlockEntity entity, float f, PoseStack stack, MultiBufferSource bufferSource, int i, int i1) {
        BlockState blockstate = entity.getBlockState();
        CoupletBlock coupletBlock = (CoupletBlock)blockstate.getBlock();
        this.renderCoupletWithText(entity, stack, bufferSource, i, i1, blockstate, coupletBlock, coupletModel);
    }

    void renderCoupletWithText(CoupletBlockEntity entity, PoseStack stack, MultiBufferSource bufferSource, int i, int i1, BlockState state, CoupletBlock block, Model model) {
        stack.pushPose();
        this.translateCouplet(stack, -block.getYRotationDegrees(state));
        this.renderCouplet(stack, bufferSource, i, i1, model);
        this.renderCoupletText(entity.getBlockPos(), entity.getFrontText(), stack, bufferSource, i, entity.getTextLineHeight(), entity.getMaxTextLineWidth());
        stack.popPose();
    }

    void translateCouplet(PoseStack stack, float f) {
        stack.translate(0.5F, 0.5F, 0.5F);
        stack.mulPose(Axis.YP.rotationDegrees(f));
        stack.translate(0, 0, -0.49F);
    }

    void renderCouplet(PoseStack stack, MultiBufferSource bufferSource, int i, int i1, Model model) {
        stack.pushPose();
        stack.scale(1, -1, -1);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(model.renderType(new ResourceLocation(Celebrations.MOD_ID, "textures/block/couplet.png")));
        this.renderCoupletModel(stack, i, i1, model, vertexconsumer);
        stack.popPose();
    }

    void renderCoupletModel(PoseStack stack, int i, int i1, Model model, VertexConsumer consumer) {
        CoupletModel coupletModel = (CoupletModel)model;
        coupletModel.root.render(stack, consumer, i, i1);
    }

    void renderCoupletText(BlockPos pos, CoupletText text, PoseStack stack, MultiBufferSource bufferSource, int i2, int i3, int i4) {
        stack.pushPose();
        this.translateCoupletText(stack);
        int i = getDarkColor(text);
        int j = 4 * i3 / 2;
        FormattedCharSequence[] aformattedcharsequence = text.getRenderMessages(Minecraft.getInstance().isTextFilteringEnabled(), (component) -> {
            List<FormattedCharSequence> list = this.font.split(component, i4);
            return list.isEmpty() ? FormattedCharSequence.EMPTY : list.get(0);
        });
        int k;
        boolean flag;
        int l;
        if (text.hasGlowingText()) {
            k = text.getColor().getTextColor();
            flag = isOutlineVisible(pos, k);
            l = 15728880;
        } else {
            k = i;
            flag = false;
            l = i2;
        }

        for(int i1 = 0; i1 < 7; ++i1) {
            FormattedCharSequence formattedcharsequence = aformattedcharsequence[i1];
            float f = (float)(-this.font.width(formattedcharsequence) / 2);
            if (flag) {
                this.font.drawInBatch8xOutline(formattedcharsequence, f, (float)(i1 * i3 - j), k, i, stack.last().pose(), bufferSource, l);
            } else {
                this.font.drawInBatch(formattedcharsequence, f, (float)(i1 * i3 - j), k, false, stack.last().pose(), bufferSource, Font.DisplayMode.POLYGON_OFFSET, 0, l);
            }
        }

        stack.popPose();
    }

    private void translateCoupletText(PoseStack stack) {
        float scale = 1.5f;
        float f = 0.015625F * scale;
        stack.translate(0, -0.4, 0);
        stack.scale(f, -f, f);
    }

    static boolean isOutlineVisible(BlockPos pos, int i) {
        if (i == DyeColor.BLACK.getTextColor()) {
            return true;
        } else {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer localplayer = minecraft.player;
            if (localplayer != null && minecraft.options.getCameraType().isFirstPerson() && localplayer.isScoping()) {
                return true;
            } else {
                Entity entity = minecraft.getCameraEntity();
                return entity != null && entity.distanceToSqr(Vec3.atCenterOf(pos)) < (double)OUTLINE_RENDER_DISTANCE;
            }
        }
    }

    static int getDarkColor(CoupletText text) {
        int i = text.getColor().getTextColor();
        if (i == DyeColor.BLACK.getTextColor() && text.hasGlowingText()) {
            return -988212;
        } else {
            int j = (int)((double) FastColor.ARGB32.red(i) * 0.4D);
            int k = (int)((double)FastColor.ARGB32.green(i) * 0.4D);
            int l = (int)((double)FastColor.ARGB32.blue(i) * 0.4D);
            return FastColor.ARGB32.color(0, j, k, l);
        }
    }

    public static CoupletModel createCoupletModel(EntityModelSet set) {
        return new CoupletModel(set.bakeLayer(LAYER_LOCATION));
    }

    public static LayerDefinition createCoupletLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -32.0F, 0.0F, 16.0F, 32.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        
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
        public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
}
