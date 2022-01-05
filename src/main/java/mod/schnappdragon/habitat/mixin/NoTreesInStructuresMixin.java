package mod.schnappdragon.habitat.mixin;

import mod.schnappdragon.habitat.core.registry.HabitatStructures;
import net.minecraft.core.SectionPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TreeFeature.class)
public class NoTreesInStructuresMixin {
    @Inject(
            method = "generate(Lnet/minecraft/world/gen/feature/util/FeatureContext;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void habitat_noTreesInStructures(FeatureContext<TreeFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
        if (!context.getWorld().getStructures(ChunkSectionPos.from(context.getOrigin()), HabitatStructures.FAIRY_RING).isEmpty())
            cir.setReturnValue(false);
    }
}