package mod.schnappdragon.habitat.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.schnappdragon.habitat.client.model.PasserineEntityModel;
import mod.schnappdragon.habitat.common.entity.animal.PasserineEntity;
import mod.schnappdragon.habitat.Habitat;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resources.Identifier;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.util.Identifier;

public class PasserineEyesRenderer<T extends PasserineEntity, M extends PasserineEntityModel<T>> extends FeatureRenderer<T, M> {
    private static final Identifier EYES = new Identifier(Habitat.MOD_ID, "textures/entity/passerine/eyes.png");
    private static final Identifier BERDLY_EYES = new Identifier(Habitat.MOD_ID, "textures/entity/passerine/berdly_eyes.png");
    private static final Identifier GOLDFISH_EYES = new Identifier(Habitat.MOD_ID, "textures/entity/passerine/goldfish_eyes.png");

    public PasserineEyesRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider buffer, int packedLight, T passerine, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(passerine.isAsleep() || passerine.isTurkey())) {
            VertexConsumer vertexconsumer = buffer.getBuffer(RenderLayer.getEntityCutoutNoCull(getTextureLocation(passerine)));
            this.getContextModel().render(matrixStack, vertexconsumer, packedLight, LivingEntityRenderer.getOverlay(passerine, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public Identifier getTextureLocation(PasserineEntity passerineEntity) {
        if (passerineEntity.isBerdly())
            return BERDLY_EYES;
        else if (passerineEntity.isGoldfish())
            return GOLDFISH_EYES;

        return EYES;
    }
}