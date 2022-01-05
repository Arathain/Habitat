package mod.schnappdragon.habitat.client.renderer.entity;

import mod.schnappdragon.habitat.client.model.PookaModel;
import mod.schnappdragon.habitat.client.renderer.HabitatModelLayers;
import mod.schnappdragon.habitat.client.renderer.entity.layers.PookaEyesLayer;
import mod.schnappdragon.habitat.common.entity.monster.Pooka;
import mod.schnappdragon.habitat.Habitat;
import net.minecraft.client.renderer.entity.EntityRendererFactory;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

public class PookaRenderer extends MobRenderer<Pooka, PookaModel<Pooka>> {
    private static final Identifier POOKA_TEXTURES = new Identifier(Habitat.MOD_ID, "textures/entity/pooka/pooka.png");

    public PookaRenderer(EntityRendererFactory.Context context) {
        super(context, new PookaModel<>(context.bakeLayer(HabitatModelLayers.POOKA)), 0.3F);
        this.addLayer(new PookaEyesLayer<>(this));
    }

    protected int getBlockLightLevel(Pooka entityIn, BlockPos partialTicks) {
        return 15;
    }

    @Override
    public Identifier getTextureLocation(Pooka entity) {
        return POOKA_TEXTURES;
    }
}