package andrews.pandoras_creatures.entities.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import andrews.pandoras_creatures.entities.HellhoundEntity;
import andrews.pandoras_creatures.entities.render.util.PCRenderTypes;
import andrews.pandoras_creatures.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderLayerHellhoundEyes<E extends HellhoundEntity, M extends EntityModel<E>> extends LayerRenderer<E, M>
{
	public static final ResourceLocation HELLHOUND_EYES_LAYER = new ResourceLocation(Reference.MODID, "textures/entity/hellhound/hellhound_eyes.png");
	
	public RenderLayerHellhoundEyes(IEntityRenderer<E, M> entityRenderer)
	{
		super(entityRenderer);
	}
	
	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if(entity.getHellhoundType() == 2)
		{
			Minecraft.getInstance().getTextureManager().bindTexture(HELLHOUND_EYES_LAYER);
	
			IVertexBuilder ivertexbuilder = bufferIn.getBuffer(PCRenderTypes.getEmissiveEntity(HELLHOUND_EYES_LAYER));
			
			this.getEntityModel().setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			this.getEntityModel().render(matrixStackIn, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}
