package mod.schnappdragon.habitat.common.registry;

import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.common.block.KabloomBushBlock;
import mod.schnappdragon.habitat.core.registry.HabitatBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

import java.util.List;

public class HabitatConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> PATCH_RAFFLESIA = Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig(2, 6, 1,
            () -> Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(BlockStateProvider.of(HabitatBlocks.RAFFLESIA.getDefaultState())))
                    .filtered(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchingBlock(Blocks.GRASS_BLOCK, new BlockPos(0, -1, 0))))));

    public static final ConfiguredFeature<?, ?> PATCH_KABLOOM_BUSH = Feature.RANDOM_PATCH.configure(ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(BlockStateProvider.of(HabitatBlocks.KABLOOM_BUSH.getDefaultState().with(KabloomBushBlock.AGE, 7)))), List.of(Blocks.GRASS_BLOCK)));

    public static final ConfiguredFeature<?, ?> PATCH_SLIME_FERN = HabitatFeatures.SLIME_FERN_FEATURE.configure(FeatureConfig.DEFAULT);

    public static final ConfiguredFeature<?, ?> PATCH_BALL_CACTUS = Feature.RANDOM_PATCH.configure(new RandomPatchFeatureConfig(4, 5, 1,
            () -> Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(HabitatBlocks.FLOWERING_ORANGE_BALL_CACTUS.getDefaultState(), 3).add(HabitatBlocks.FLOWERING_PINK_BALL_CACTUS.getDefaultState(), 3).add(HabitatBlocks.FLOWERING_RED_BALL_CACTUS.getDefaultState(), 2).add(HabitatBlocks.FLOWERING_YELLOW_BALL_CACTUS.getDefaultState(), 1))))
                    .filtered(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchingBlocks(List.of(Blocks.SAND, Blocks.RED_SAND), new BlockPos(0, -1, 0))))));

    public static final ConfiguredFeature<?, ?> FAIRY_RING = HabitatFeatures.FAIRY_RING_FEATURE.configure(FeatureConfig.DEFAULT);
    public static final ConfiguredFeature<?, ?> HUGE_FAIRY_RING_MUSHROOM = HabitatFeatures.HUGE_FAIRY_RING_MUSHROOM_FEATURE.configure(new HugeMushroomFeatureConfig(BlockStateProvider.of(HabitatBlocks.FAIRY_RING_MUSHROOM_BLOCK.getDefaultState().with(MushroomBlock.DOWN, false)), BlockStateProvider.of(HabitatBlocks.FAIRY_RING_MUSHROOM_STEM.getDefaultState().with(MushroomBlock.UP, false).setValue(MushroomBlock.DOWN, false)), 2));

    public static void registerConfiguredFeatures() {
        register("rafflesia_patch", PATCH_RAFFLESIA);
        register("kabloom_bush_patch", PATCH_KABLOOM_BUSH);
        register("slime_fern_patch", PATCH_SLIME_FERN);
        register("ball_cactus_patch", PATCH_BALL_CACTUS);
        register("fairy_ring", FAIRY_RING);
        register("huge_fairy_ring_mushroom", HUGE_FAIRY_RING_MUSHROOM);
    }

    private static void register(String id, ConfiguredFeature<?, ?> configuredFeature) {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(Habitat.MOD_ID, id), configuredFeature);
    }
}