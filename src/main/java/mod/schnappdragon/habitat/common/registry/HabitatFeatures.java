package mod.schnappdragon.habitat.common.registry;

import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.common.worldgen.feature.FairyRingFeature;
import mod.schnappdragon.habitat.common.worldgen.feature.HugeFairyRingMushroomFeature;
import mod.schnappdragon.habitat.common.worldgen.feature.SlimeFernFeature;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;

public class HabitatFeatures {
    public final static Feature<DefaultFeatureConfig> SLIME_FERN_FEATURE = register("slime_fern",
            new SlimeFernFeature(DefaultFeatureConfig.CODEC));

    public final static Feature<DefaultFeatureConfig> FAIRY_RING_FEATURE = register("fairy_ring",
            new FairyRingFeature(DefaultFeatureConfig.CODEC));

    public final static Feature<HugeMushroomFeatureConfig> HUGE_FAIRY_RING_MUSHROOM_FEATURE = register("huge_fairy_ring_mushroom",
            new HugeFairyRingMushroomFeature(HugeMushroomFeatureConfig.CODEC));

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        return (F) Registry.register(Registry.FEATURE, new Identifier(Habitat.MOD_ID, name), feature);
    }
}
