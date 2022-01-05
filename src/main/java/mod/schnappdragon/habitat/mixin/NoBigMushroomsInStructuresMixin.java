package mod.schnappdragon.habitat.mixin;

import mod.schnappdragon.habitat.core.registry.HabitatStructures;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.gen.feature.HugeMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HugeMushroomFeature.class)
public class NoBigMushroomsInStructuresMixin {
    @Inject(
            method = "generate",
            at = @At("HEAD"),
            cancellable = true
    )
    private void habitat_noBigMushroomsInStructures(FeatureContext<HugeMushroomFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
        if (!context.getWorld().getStructures(ChunkSectionPos.from(context.getOrigin()), HabitatStructures.FAIRY_RING).isEmpty())
            cir.setReturnValue(false);
    }
}