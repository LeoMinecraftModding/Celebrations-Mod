package team.leomc.celebrations.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.leomc.celebrations.Celebrations;

@OnlyIn(Dist.CLIENT)
public class CRenderType extends RenderType {
	public CRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
		super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
	}

	public static RenderType entityCutoutNoCullTriangles(ResourceLocation location) {
		CompositeState state = RenderType.CompositeState.builder()
			.setShaderState(RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER)
			.setTextureState(new RenderStateShard.TextureStateShard(location, false, false))
			.setTransparencyState(NO_TRANSPARENCY)
			.setCullState(NO_CULL)
			.setLightmapState(LIGHTMAP)
			.setOverlayState(OVERLAY)
			.createCompositeState(true);
		return create(Celebrations.id("entity_cutout_no_cull_triangles").toString(), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, TRANSIENT_BUFFER_SIZE, true, false, state);
	}
}
