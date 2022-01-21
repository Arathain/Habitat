package mod.schnappdragon.habitat.common.worldgen.feature;

import com.mojang.serialization.Codec;
import mod.schnappdragon.habitat.common.block.SlimeFernBlock;
import mod.schnappdragon.habitat.common.block.WallSlimeFernBlock;
import mod.schnappdragon.habitat.core.registry.HabitatBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class SlimeFernFeature extends Feature<DefaultFeatureConfig> {
    public SlimeFernFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        Random rand = context.getRandom();

        int i = 0;
        BlockPos centrePos = pos.add(7, 0, 7);
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        Direction[] directions = new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP};

        for (int j = 0; j < 64; ++j) {
            blockpos$mutable.set(centrePos, rand.nextInt(4) - rand.nextInt(4), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(4) - rand.nextInt(4));

            if (world.isAir(blockpos$mutable)) {
                for (Direction dir : directions) {
                    if (world.getBlockState(blockpos$mutable.offset(dir)).isIn(BlockTags.BASE_STONE_OVERWORLD)) {
                        BlockState state;
                        if (dir == Direction.DOWN)
                            state = HabitatBlocks.SLIME_FERN.getDefaultState();
                        else if (dir == Direction.UP)
                            state = HabitatBlocks.SLIME_FERN.getDefaultState().setValue(SlimeFernBlock.ON_CEILING, true);
                        else
                            state = HabitatBlocks.WALL_SLIME_FERN.getDefaultState().setValue(WallSlimeFernBlock.HORIZONTAL_FACING, dir.getOpposite());

                        this.setBlockState(world, blockpos$mutable, state);

                        ++i;
                        break;
                    }
                }
            }
        }

        return i > 0;
    }
}
