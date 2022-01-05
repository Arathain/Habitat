package mod.schnappdragon.habitat.client.renderer;

import mod.schnappdragon.habitat.client.model.PasserineEntityModel;
import mod.schnappdragon.habitat.client.model.PookaModel;
import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.mixin.EntityModelLayersInvoker;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.resources.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


public class HabitatModelLayers {
    public static final EntityModelLayer POOKA = EntityModelLayersInvoker.register("pooka", "main");
    public static final EntityModelLayer PASSERINE = EntityModelLayersInvoker.register("passerine", "main");
}