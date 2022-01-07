package mod.schnappdragon.habitat.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.schnappdragon.habitat.client.model.PookaModel;
import mod.schnappdragon.habitat.common.entity.monster.PookaEntity;
import mod.schnappdragon.habitat.Habitat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;

public class PookaEyesLayer<T extends PookaEntity, M extends PookaModel<T>> extends RenderLayer<T, M> {
    private static final Identifier HOSTILE_EYES = new Identifier(Habitat.MOD_ID, "textures/entity/pooka/hostile_eyes.png");
    private static final Identifier PACIFIED_EYES = new Identifier(Habitat.MOD_ID, "textures/entity/pooka/pacified_eyes.png");
    private static final Identifier PASSIVE_EYES = new Identifier(Habitat.MOD_ID, "textures/entity/pooka/passive_eyes.png");

    public PookaEyesLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T pooka, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(pooka)));
        this.getParentModel().renderToBuffer(matrixStack, vertexconsumer, packedLight, LivingEntityRenderer.getOverlayCoords(pooka, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public Identifier getTextureLocation(PookaEntity pooka) {
        return switch (pooka.getState()) {
            case HOSTILE -> HOSTILE_EYES;
            case PACIFIED -> PACIFIED_EYES;
            case PASSIVE -> PASSIVE_EYES;
        };
    }
}