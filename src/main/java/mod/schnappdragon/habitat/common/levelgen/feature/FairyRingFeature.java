package mod.schnappdragon.habitat.common.levelgen.feature;

import com.mojang.serialization.Codec;
import mod.schnappdragon.habitat.common.block.FairyRingMushroomBlock;
import mod.schnappdragon.habitat.core.registry.HabitatBlocks;
import mod.schnappdragon.habitat.core.registry.HabitatConfiguredFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Arrays;
import java.util.Random;

public class FairyRingFeature extends Feature<DefaultFeatureConfig> {
    public FairyRingFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        ChunkGenerator generator = context.getGenerator();
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        Random rand = context.getRandom();

        int[][] XZ_PAIRS = {
                {1, 5}, {2, 5}, {3, 4}, {4, 4}, {4, 3}, {5, 2}, {5, 1}, {5, 0},
                {0, 5}, {-1, 5}, {-2, 5}, {-3, 4}, {-4, 4}, {-4, 3}, {-5, 2}, {-5, 1},
                {-1, -5}, {-2, -5}, {-3, -4}, {-4, -4}, {-4, -3}, {-5, -2}, {-5, -1}, {-5, 0},
                {0, -5}, {1, -5}, {2, -5}, {3, -4}, {4, -4}, {4, -3}, {5, -2}, {5, -1}
        };
        int[] bigXZ = XZ_PAIRS[rand.nextInt(32)];
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for (int[] XZ : XZ_PAIRS) {
            for (int y = 5; y >= -6; --y) {
                blockpos$mutable.set(pos, XZ[0], y, XZ[1]);
                BlockState base = world.getBlockState(blockpos$mutable.down());
                if (world.isAir(blockpos$mutable) && base.isOpaque()) {
                    if (Arrays.equals(XZ, bigXZ)) {
                        ConfiguredFeature<?, ?> configuredfeature = HabitatConfiguredFeatures.HUGE_FAIRY_RING_MUSHROOM;

                        if (configuredfeature.generate(world, generator, rand, blockpos$mutable))
                            break;
                    }

                    this.setBlockState(world, blockpos$mutable, this.getMushroom(rand));
                    break;
                }
            }
        }

        return true;
    }

    private BlockState getMushroom(Random random) {
        return HabitatBlocks.FAIRY_RING_MUSHROOM.defaultBlockState().setValue(FairyRingMushroomBlock.MUSHROOMS, 1 + random.nextInt(4));
    }
}