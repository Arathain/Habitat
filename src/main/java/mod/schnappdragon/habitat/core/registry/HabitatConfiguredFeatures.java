package mod.schnappdragon.habitat.core.registry;

import mod.schnappdragon.habitat.common.block.KabloomBushBlock;
import mod.schnappdragon.habitat.core.Habitat;
import mod.schnappdragon.habitat.core.tags.HabitatBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

import java.util.List;

public class HabitatConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> PATCH_RAFFLESIA = Feature.RANDOM_PATCH.configured(new RandomPatchConfiguration(2, 6, 1,
            () -> Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(HabitatBlocks.RAFFLESIA.get().defaultBlockState())))
                    .filtered(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchesBlock(Blocks.GRASS_BLOCK, new BlockPos(0, -1, 0))))));

    public static final ConfiguredFeature<?, ?> PATCH_KABLOOM_BUSH = Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(HabitatBlocks.KABLOOM_BUSH.get().defaultBlockState().setValue(KabloomBushBlock.AGE, 7)))), List.of(Blocks.GRASS_BLOCK)));

    public static final ConfiguredFeature<?, ?> PATCH_SLIME_FERN = HabitatFeatures.SLIME_FERN_FEATURE.get().configured(FeatureConfiguration.NONE);

    public static final ConfiguredFeature<?, ?> PATCH_BALL_CACTUS = Feature.RANDOM_PATCH.configured(new RandomPatchConfiguration(4, 5, 1,
            () -> Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(HabitatBlocks.FLOWERING_ORANGE_BALL_CACTUS.get().defaultBlockState(), 3).add(HabitatBlocks.FLOWERING_PINK_BALL_CACTUS.get().defaultBlockState(), 3).add(HabitatBlocks.FLOWERING_RED_BALL_CACTUS.get().defaultBlockState(), 2).add(HabitatBlocks.FLOWERING_YELLOW_BALL_CACTUS.get().defaultBlockState(), 1))))
                    .filtered(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchesBlocks(List.of(Blocks.SAND, Blocks.RED_SAND), new BlockPos(0, -1, 0))))));

    public static final ConfiguredFeature<?, ?> FAIRY_RING = HabitatFeatures.FAIRY_RING_FEATURE.get().configured(FeatureConfiguration.NONE);
    public static final ConfiguredFeature<?, ?> HUGE_FAIRY_RING_MUSHROOM = HabitatFeatures.HUGE_FAIRY_RING_MUSHROOM_FEATURE.get().configured(new HugeMushroomFeatureConfiguration(BlockStateProvider.simple(HabitatBlocks.FAIRY_RING_MUSHROOM_BLOCK.get().defaultBlockState().setValue(HugeMushroomBlock.DOWN, false)), BlockStateProvider.simple(HabitatBlocks.FAIRY_RING_MUSHROOM_STEM.get().defaultBlockState().setValue(HugeMushroomBlock.UP, false).setValue(HugeMushroomBlock.DOWN, false)), 2));

    public static void registerConfiguredFeatures() {
        register("rafflesia_patch", PATCH_RAFFLESIA);
        register("kabloom_bush_patch", PATCH_KABLOOM_BUSH);
        register("slime_fern_patch", PATCH_SLIME_FERN);
        register("ball_cactus_patch", PATCH_BALL_CACTUS);
        register("fairy_ring", FAIRY_RING);
        register("huge_fairy_ring_mushroom", HUGE_FAIRY_RING_MUSHROOM);
    }

    private static void register(String id, ConfiguredFeature<?, ?> configuredFeature) {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(Habitat.MODID, id), configuredFeature);
    }
}