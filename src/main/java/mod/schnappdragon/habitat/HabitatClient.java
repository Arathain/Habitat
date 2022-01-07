package mod.schnappdragon.habitat;

import mod.schnappdragon.habitat.client.renderer.entity.PasserineEntityRenderer;
import mod.schnappdragon.habitat.common.registry.HabitatEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class HabitatClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(HabitatEntities.PASSERINE, PasserineEntityRenderer::new);
    }
}
