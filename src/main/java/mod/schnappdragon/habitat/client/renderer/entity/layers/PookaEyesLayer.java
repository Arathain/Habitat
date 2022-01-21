package mod.schnappdragon.habitat.client.renderer.entity.layers;

import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.client.model.PookaModel;
import mod.schnappdragon.habitat.common.entity.monster.PookaEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PookaEyesLayer<T extends PookaEntity, M extends PookaModel<T>> extends FeatureRenderer<T, M> {
    private static final Identifier HOSTILE_EYES = new Identifier(Habitat.MOD_ID, "textures/entity/pooka/hostile_eyes.png");
    private static final Identifier PACIFIED_EYES = new Identifier(Habitat.MOD_ID, "textures/entity/pooka/pacified_eyes.png");
    private static final Identifier PASSIVE_EYES = new Identifier(Habitat.MOD_ID, "textures/entity/pooka/passive_eyes.png");

    public PookaEyesLayer(FeatureRendererContext<T, M> parent) {
        super(parent);
    }


    public Identifier getTextureLocation(PookaEntity pooka) {
        return switch (pooka.getState()) {
            case HOSTILE -> HOSTILE_EYES;
            case PACIFIED -> PACIFIED_EYES;
            case PASSIVE -> PASSIVE_EYES;
        };
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T pooka, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(getTextureLocation(pooka)));
        this.getContextModel().render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(pooka, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
    }
}