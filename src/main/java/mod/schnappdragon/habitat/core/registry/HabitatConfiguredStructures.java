package mod.schnappdragon.habitat.core.registry;

import mod.schnappdragon.habitat.Habitat;
import net.minecraft.structure.PlainsVillageData;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class HabitatConfiguredStructures {
    public static final ConfiguredStructureFeature<?, ?> CONFIGURED_FAIRY_RING = HabitatStructures.FAIRY_RING.configure(new StructurePoolFeatureConfig(() -> PlainsVillageData.STRUCTURE_POOLS, 0));

    public static void registerConfiguredStructures() {
        register("fairy_ring", CONFIGURED_FAIRY_RING);
    }

    private static void register(String id, ConfiguredStructureFeature<?, ?> structure) {
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new Identifier(Habitat.MOD_ID, id), structure);
    }
}