package mod.schnappdragon.habitat.core.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.common.worldgen.feature.structure.FairyRingStructure;
import mod.schnappdragon.habitat.HabitatConfig;
import mod.schnappdragon.habitat.core.HabitatConfig;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HabitatStructures {
    private static final Map<StructureFeature<?>, Identifier> STRUCTURE_FEATURES = new LinkedHashMap<>();

    public static final StructureFeature<StructurePoolFeatureConfig> FAIRY_RING = register("fairy_ring", () -> (new FairyRingStructure(StructurePoolFeatureConfig.CODEC)));

    public static void setupStructures() {
        setupMapSpacingAndLand(FAIRY_RING, new StructureConfig(HabitatConfig.COMMON.fairyRingAverage, HabitatConfig.COMMON.fairyRingMinimum, 1002806115), false);
    }
    private static void register(String name, StructureFeature<?> structureFeature) {
        STRUCTURE_FEATURES.put(structureFeature, new Identifier(Habitat.MOD_ID, name));
    }
    public static void initialize() {
        STRUCTURE_FEATURES.keySet().forEach(entityType -> Registry.register(Registry.STRUCTURE_FEATURE, STRUCTURE_FEATURES.get(entityType), entityType));
    }

    private static <F extends StructureFeature<?>> void setupMapSpacingAndLand(F structure, StructureConfig structureFeatureConfiguration, boolean transformSurroundingLand) {
        StructureFeature.STRUCTURES.put(structure.getName(), structure);

        if (transformSurroundingLand)
            StructureFeature.LAND_MODIFYING_STRUCTURES.add(structure);

        StructuresConfig.DEFAULT_STRUCTURES.put(structure, structureFeatureConfiguration);

        BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<StructureFeature<?>, StructureConfig> structureMap = settings.getValue().structureSettings().structureConfig;

            if (structureMap instanceof ImmutableMap) {
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureFeatureConfiguration);
                settings.getValue().structureSettings().structureConfig = tempMap;
            } else
                structureMap.put(structure, structureFeatureConfiguration);
        });
    }

}
