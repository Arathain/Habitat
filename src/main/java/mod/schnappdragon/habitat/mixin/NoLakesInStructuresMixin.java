package mod.schnappdragon.habitat.mixin;

import mod.schnappdragon.habitat.core.registry.HabitatStructures;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.gen.feature.LakeFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LakeFeature.class)
public class NoLakesInStructuresMixin {
    @Inject(
            method = "generate",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/BlockPos;down(I)Lnet/minecraft/util/math/BlockPos;"),
            cancellable = true
    )
    private void habitat_noLakesInStructures(FeatureContext<LakeFeature.Config> context, CallbackInfoReturnable<Boolean> cir) {
        if (!context.getWorld().getStructures(ChunkSectionPos.from(context.getOrigin()), HabitatStructures.FAIRY_RING).isEmpty())
            cir.setReturnValue(false);
    }
}