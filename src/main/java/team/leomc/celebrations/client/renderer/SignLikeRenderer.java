package team.leomc.celebrations.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.leomc.celebrations.block.WallSignLikeBlock;
import team.leomc.celebrations.block.entity.SignLikeBlockEntity;
import team.leomc.celebrations.util.SignLikeText;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class SignLikeRenderer<E extends SignLikeBlockEntity, T extends Model> implements BlockEntityRenderer<E> {
	protected static final int OUTLINE_RENDER_DISTANCE = Mth.square(16);
	protected final Font font;
	protected final T model;

	public SignLikeRenderer(BlockEntityRendererProvider.Context context) {
		this.font = context.getFont();
		this.model = makeModel(context);
	}

	protected abstract T makeModel(BlockEntityRendererProvider.Context context);

	public void render(E entity, float f, PoseStack stack, MultiBufferSource bufferSource, int i, int i1) {
		BlockState blockstate = entity.getBlockState();
		WallSignLikeBlock signLikeBlock = (WallSignLikeBlock) blockstate.getBlock();
		this.renderSignLikeWithText(entity, stack, bufferSource, i, i1, blockstate, signLikeBlock, model);
	}

	protected void renderSignLikeWithText(E entity, PoseStack stack, MultiBufferSource bufferSource, int i, int i1, BlockState state, WallSignLikeBlock block, Model model) {
		stack.pushPose();
		this.translateSignLike(stack, -block.getYRotationDegrees(state));
		this.renderSignLike(stack, bufferSource, i, i1, model);
		this.renderSignLikeText(entity.getBlockPos(), entity.getText(), stack, bufferSource, i, entity.getTextLineHeight(), entity.getMaxTextLineWidth());
		stack.popPose();
	}

	protected void translateSignLike(PoseStack stack, float f) {
		stack.translate(0.5F, 0.5F, 0.5F);
		stack.mulPose(Axis.YP.rotationDegrees(f));
		stack.translate(0, 0, -0.49F);
	}

	protected void renderSignLike(PoseStack stack, MultiBufferSource bufferSource, int i, int i1, Model model) {
		stack.pushPose();
		stack.scale(1, -1, -1);
		VertexConsumer vertexconsumer = bufferSource.getBuffer(getRenderType());
		this.renderSignLikeModel(stack, i, i1, model, vertexconsumer);
		stack.popPose();
	}

	protected abstract RenderType getRenderType();

	protected abstract void renderSignLikeModel(PoseStack stack, int i, int i1, Model model, VertexConsumer consumer);

	void renderSignLikeText(BlockPos pos, SignLikeText text, PoseStack stack, MultiBufferSource bufferSource, int i2, int textLineHeight, int textLineWidth) {
		stack.pushPose();
		this.translateSignLikeText(stack);
		int i = getDarkColor(text);
		int j = 4 * textLineHeight / 2;
		FormattedCharSequence[] aformattedcharsequence = text.getRenderMessages((component) -> {
			List<FormattedCharSequence> list = this.font.split(component, textLineWidth);
			return list.isEmpty() ? FormattedCharSequence.EMPTY : list.getFirst();
		});
		int k;
		boolean flag;
		int l;
		if (text.glow()) {
			k = text.color().getTextColor();
			flag = isOutlineVisible(pos, k);
			l = 15728880;
		} else {
			k = i;
			flag = false;
			l = i2;
		}

		for (int i1 = 0; i1 < text.messages().size(); ++i1) {
			FormattedCharSequence formattedcharsequence = aformattedcharsequence[i1];
			float f = (float) (-this.font.width(formattedcharsequence) / 2);
			if (flag) {
				this.font.drawInBatch8xOutline(formattedcharsequence, f, (float) (i1 * textLineHeight - j), k, i, stack.last().pose(), bufferSource, l);
			} else {
				this.font.drawInBatch(formattedcharsequence, f, (float) (i1 * textLineHeight - j), k, false, stack.last().pose(), bufferSource, Font.DisplayMode.POLYGON_OFFSET, 0, l);
			}
		}

		stack.popPose();
	}

	protected void translateSignLikeText(PoseStack stack) {
		float scale = 1.5f;
		float f = 0.015625F * scale;
		stack.translate(0, -3f / 16f, 0);
		stack.scale(f, -f, f);
	}

	protected static boolean isOutlineVisible(BlockPos pos, int i) {
		if (i == DyeColor.BLACK.getTextColor()) {
			return true;
		} else {
			Minecraft minecraft = Minecraft.getInstance();
			LocalPlayer localplayer = minecraft.player;
			if (localplayer != null && minecraft.options.getCameraType().isFirstPerson() && localplayer.isScoping()) {
				return true;
			} else {
				Entity entity = minecraft.getCameraEntity();
				return entity != null && entity.distanceToSqr(Vec3.atCenterOf(pos)) < (double) OUTLINE_RENDER_DISTANCE;
			}
		}
	}

	protected static int getDarkColor(SignLikeText text) {
		int i = text.color().getTextColor();
		if (i == DyeColor.BLACK.getTextColor() && text.glow()) {
			return -988212;
		} else {
			int j = (int) ((double) FastColor.ARGB32.red(i) * 0.4D);
			int k = (int) ((double) FastColor.ARGB32.green(i) * 0.4D);
			int l = (int) ((double) FastColor.ARGB32.blue(i) * 0.4D);
			return FastColor.ARGB32.color(0, j, k, l);
		}
	}
}
