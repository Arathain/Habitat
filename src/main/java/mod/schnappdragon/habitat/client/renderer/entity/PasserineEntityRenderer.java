package mod.schnappdragon.habitat.client.renderer.entity;

import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.client.model.PasserineEntityModel;
import mod.schnappdragon.habitat.client.renderer.HabitatModelLayers;
import mod.schnappdragon.habitat.client.renderer.entity.layers.PasserineEyesRenderer;
import mod.schnappdragon.habitat.common.entity.animal.PasserineEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class PasserineEntityRenderer extends MobEntityRenderer<PasserineEntity, PasserineEntityModel<PasserineEntity>> {
    public static final Identifier[] PASSERINE_LOCATIONS = new Identifier[]{
            new Identifier(Habitat.MOD_ID, "textures/entity/passerine/american_goldfinch.png"),
            new Identifier(Habitat.MOD_ID, "textures/entity/passerine/bali_myna.png"),
            new Identifier(Habitat.MOD_ID, "textures/entity/passerine/blue_jay.png"),
            new Identifier(Habitat.MOD_ID, "textures/entity/passerine/common_sparrow.png"),
            new Identifier(Habitat.MOD_ID, "textures/entity/passerine/eastern_bluebird.png"),
            new Identifier(Habitat.MOD_ID, "textures/entity/passerine/eurasian_bullfinch.png"),
            new Identifier(Habitat.MOD_ID, "textures/entity/passerine/flame_robin.png"),
            new Identifier(Habitat.MOD_ID, "textures/entity/passerine/northern_cardinal.png"),
            new Identifier(Habitat.MOD_ID, "textures/entity/passerine/red_throated_parrotfinch.png"),
            new Identifier(Habitat.MOD_ID, "textures/entity/passerine/violet_backed_starling.png")
    };
    public static final Identifier PASSERINE_BERDLY_LOCATION = new Identifier(Habitat.MOD_ID, "textures/entity/passerine/berdly.png");
    public static final Identifier PASSERINE_GOLDFISH_LOCATION = new Identifier(Habitat.MOD_ID, "textures/entity/passerine/goldfish.png");
    public static final Identifier PASSERINE_TURKEY_LOCATION = new Identifier(Habitat.MOD_ID, "textures/entity/passerine/turkey.png");

    public PasserineEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PasserineEntityModel<>(context.getPart(HabitatModelLayers.PASSERINE)), 0.25F);
        this.addFeature(new PasserineEyesRenderer<>(this));
    }

    @Override
    public Identifier getTexture(PasserineEntity passerine) {
        if (passerine.isBerdly())
            return PASSERINE_BERDLY_LOCATION;
        else if (passerine.isGoldfish())
            return PASSERINE_GOLDFISH_LOCATION;
        else if (passerine.isTurkey())
            return PASSERINE_TURKEY_LOCATION;

        return PASSERINE_LOCATIONS[passerine.getVariant()];
    }


    @Override
    public float getAnimationProgress(PasserineEntity passerine, float partialTicks) {
        float f = MathHelper.lerp(partialTicks, passerine.initialFlap, passerine.flap);
        float f1 = MathHelper.lerp(partialTicks, passerine.initialFlapSpeed, passerine.flapSpeed);
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}