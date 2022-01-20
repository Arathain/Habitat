package mod.schnappdragon.habitat.common.worldgen.placement;

import com.mojang.serialization.Codec;
import mod.schnappdragon.habitat.common.registry.HabitatPlacementModifierTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.AbstractConditionalPlacementModifier;
import net.minecraft.world.gen.decorator.DecoratorContext;
import net.minecraft.world.gen.decorator.PlacementModifierType;
import net.minecraft.world.gen.random.ChunkRandom;

import java.util.Random;

public class SlimeChunkFilter extends AbstractConditionalPlacementModifier {
    private static final SlimeChunkFilter FILTER = new SlimeChunkFilter();
    public static Codec<SlimeChunkFilter> CODEC = Codec.unit(() -> FILTER);

    public static SlimeChunkFilter filter() {
        return FILTER;
    }

    protected boolean shouldPlace(DecoratorContext context, Random random, BlockPos pos) {
        return ChunkRandom.getSlimeRandom(pos.getX() >> 4, pos.getZ() >> 4, context.getWorld().getSeed(), 987234911L).nextInt(10) == 0;
    }

    @Override
    public PlacementModifierType<?> getType() {
        return HabitatPlacementModifierTypes.SLIME_CHUNK_FILTER;
    }
}