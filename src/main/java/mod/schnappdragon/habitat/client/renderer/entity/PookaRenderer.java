package mod.schnappdragon.habitat.client.renderer.entity;

import mod.schnappdragon.habitat.client.renderer.entity.models.PookaModel;
import mod.schnappdragon.habitat.common.entity.monster.PookaEntity;
import mod.schnappdragon.habitat.core.Habitat;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

public class PookaRenderer extends MobRenderer<PookaEntity, PookaModel<PookaEntity>> {
    private static final ResourceLocation POOKA_TEXTURES = new ResourceLocation(Habitat.MODID, "textures/entity/pooka/pooka.png");
    private static final ResourceLocation PACIFIED_POOKA_TEXTURES = new ResourceLocation(Habitat.MODID, "textures/entity/pooka/pooka_pacified.png");

    public PookaRenderer(EntityRenderDispatcher renderManagerIn) {
        super(renderManagerIn, new PookaModel<>(), 0.3F);
    }

    protected int getBlockLightLevel(PookaEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(PookaEntity entity) {
        return entity.isPacified() ? PACIFIED_POOKA_TEXTURES : POOKA_TEXTURES;
    }
}