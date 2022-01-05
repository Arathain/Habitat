package mod.schnappdragon.habitat.core.registry;

import mod.schnappdragon.habitat.common.levelgen.placement.SlimeChunkFilter;
import mod.schnappdragon.habitat.Habitat;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.BiomePlacementModifier;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.RarityFilterPlacementModifier;
import net.minecraft.world.gen.decorator.SquarePlacementModifier;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

public class HabitatPlacedFeatures {
    public static final PlacedFeature PATCH_RAFFLESIA = HabitatConfiguredFeatures.PATCH_RAFFLESIA.withPlacement(RarityFilterPlacementModifier.of(4), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());
    public static final PlacedFeature PATCH_RAFFLESIA_SPARSE = HabitatConfiguredFeatures.PATCH_RAFFLESIA.withPlacement(RarityFilterPlacementModifier.of(40), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());

    public static final PlacedFeature PATCH_KABLOOM_BUSH = HabitatConfiguredFeatures.PATCH_KABLOOM_BUSH.withPlacement(RarityFilterPlacementModifier.of(225), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());

    public static final PlacedFeature PATCH_SLIME_FERN = HabitatConfiguredFeatures.PATCH_SLIME_FERN.withPlacement(SlimeChunkFilter.filter(), HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.aboveBottom(40)), BiomePlacementModifier.of());

    public static final PlacedFeature PATCH_BALL_CACTUS = HabitatConfiguredFeatures.PATCH_BALL_CACTUS.withPlacement(RarityFilterPlacementModifier.of(25), SquarePlacementModifier.of(), PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());

    public static final PlacedFeature FAIRY_RING = HabitatConfiguredFeatures.FAIRY_RING.withPlacement();

    public static void registerPlacedFeatures() {
        register("rafflesia_patch", PATCH_RAFFLESIA);
        register("sparse_rafflesia_patch", PATCH_RAFFLESIA_SPARSE);
        register("kabloom_bush_patch", PATCH_KABLOOM_BUSH);
        register("slime_fern_patch", PATCH_SLIME_FERN);
        register("ball_cactus_patch", PATCH_BALL_CACTUS);
        register("fairy_ring", FAIRY_RING);
    }

    private static void register(String id, PlacedFeature placedFeature) {
        Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(Habitat.MOD_ID, id), placedFeature);
    }
}