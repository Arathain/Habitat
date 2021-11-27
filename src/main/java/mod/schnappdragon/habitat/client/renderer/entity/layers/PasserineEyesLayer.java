package mod.schnappdragon.habitat.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.schnappdragon.habitat.client.model.PasserineModel;
import mod.schnappdragon.habitat.common.entity.animal.Passerine;
import mod.schnappdragon.habitat.core.Habitat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class PasserineEyesLayer<T extends Passerine, M extends PasserineModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation EYES = new ResourceLocation(Habitat.MODID, "textures/entity/passerine/eyes/eyes.png");
    private static final ResourceLocation BERDLY_EYES = new ResourceLocation(Habitat.MODID, "textures/entity/passerine/eyes/berdly_eyes.png");

    public PasserineEyesLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T passerine, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!passerine.isSleeping()) {
            VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(passerine)));
            this.getParentModel().renderToBuffer(matrixStack, vertexconsumer, packedLight, LivingEntityRenderer.getOverlayCoords(passerine, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public ResourceLocation getTextureLocation(Passerine passerine) {
        return passerine.isBerdly() ? BERDLY_EYES : EYES;
    }
}