package mod.schnappdragon.habitat.mixin;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityModelLayers.class)
public interface EntityModelLayersInvoker {
    @Invoker("register")
    static EntityModelLayer register(String id, String layer) {
        throw new AssertionError();
    }
}