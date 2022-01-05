package mod.schnappdragon.habitat.core.registry;

import com.mojang.serialization.Codec;
import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.common.levelgen.placement.SlimeChunkFilter;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifierType;

public class HabitatPlacementModifierTypes {
    public static PlacementModifierType<SlimeChunkFilter> SLIME_CHUNK_FILTER;

    public static void registerPlacementModifierTypes() {
        SLIME_CHUNK_FILTER = register("slime_chunk_filter", SlimeChunkFilter.CODEC);
    }

    private static <P extends PlacementModifier> PlacementModifierType<P> register(String id, Codec<P> codec) {
        return Registry.register(Registry.PLACEMENT_MODIFIER_TYPE, new Identifier(Habitat.MOD_ID, id), () -> codec);
    }
}