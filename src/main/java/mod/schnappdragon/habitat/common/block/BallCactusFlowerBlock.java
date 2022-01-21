package mod.schnappdragon.habitat.common.block;

import mod.schnappdragon.habitat.common.registry.HabitatBlockTags;
import net.minecraft.block.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Random;
import java.util.function.Supplier;

public class BallCactusFlowerBlock extends HabitatFlowerBlock implements Fertilizable {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 3.0D, 11.0D);
    private final BallCactusColor color;

    public BallCactusFlowerBlock(BallCactusColor colorIn, Supplier<StatusEffect> stewEffect, int stewEffectDuration, AbstractBlock.Settings properties) {
        super(stewEffect, stewEffectDuration, properties);
        this.color = colorIn;
    }

    public BallCactusColor getColor() {
        return color;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.NONE;
    }

    /*
     * Position Validity Method
     */

    public boolean canPlaceAt(BlockState state, WorldView worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isIn(HabitatBlockTags.BALL_CACTUS_FLOWER_PLACEABLE_ON) || worldIn.getBlockState(pos.down()).isIn(HabitatBlockTags.BALL_CACTUS_PLANTABLE_ON);
    }

    /*
     * Growth Methods
     */

    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (canGrow(world, pos)) {
            world.setBlockState(pos, color.getGrowingBallCactus().getDefaultState());
        }
    }

    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState var3, boolean var4) {
        return canGrow((World) world, pos);
    }

    public boolean canGrow(World world, Random var2, BlockPos pos, BlockState var4) {
        return canGrow(world, pos);
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState var4) {
        world.setBlockState(pos, (random.nextBoolean() ? color.getGrowingBallCactus() : color.getBallCactus()).getDefaultState());
    }

    public boolean canGrow(World worldIn, BlockPos pos) {
        return !worldIn.getBlockState(pos.down()).isIn(HabitatBlockTags.BALL_CACTUS_FLOWER_PLACEABLE_ON);
    }
}
