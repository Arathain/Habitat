package mod.schnappdragon.habitat.core.mixin;

import mod.schnappdragon.habitat.core.registry.HabitatStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(TreeFeature.class)
public class NoTreesInStructuresMixin {
    @Inject(
            method = "place(Lnet/minecraft/world/ISeedReader;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/BaseTreeFeatureConfig;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void habitat_noTreesInStructures(WorldGenLevel serverWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, TreeConfiguration baseTreeFeatureConfig, CallbackInfoReturnable<Boolean> cir) {
        SectionPos sectionPos = SectionPos.of(blockPos);
        if (serverWorldAccess.startsForFeature(sectionPos, HabitatStructures.FAIRY_RING_STRUCTURE.get()).findAny().isPresent()) {
            cir.setReturnValue(false);
        }
    }
}