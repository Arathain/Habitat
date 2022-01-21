package mod.schnappdragon.habitat.client.renderer.entity;

import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.client.model.PookaModel;
import mod.schnappdragon.habitat.client.renderer.HabitatModelLayers;
import mod.schnappdragon.habitat.client.renderer.entity.layers.PookaEyesLayer;
import mod.schnappdragon.habitat.common.entity.monster.PookaEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class PookaRenderer extends MobEntityRenderer<PookaEntity, PookaModel<PookaEntity>> {
    private static final Identifier POOKA_TEXTURES = new Identifier(Habitat.MOD_ID, "textures/entity/pooka/pooka.png");

    public PookaRenderer(EntityRendererFactory.Context context) {
        super(context, new PookaModel<>(context.getPart(HabitatModelLayers.POOKA)), 0.3F);
        this.addFeature(new PookaEyesLayer<>(this));
    }

    protected int getBlockLightLevel(PookaEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    @Override
    public Identifier getTexture(PookaEntity entity) {
        return POOKA_TEXTURES;
    }
}