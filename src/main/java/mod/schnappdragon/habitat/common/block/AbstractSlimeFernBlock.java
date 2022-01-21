package mod.schnappdragon.habitat.common.block;

import mod.schnappdragon.habitat.common.registry.HabitatParticleTypes;
import mod.schnappdragon.habitat.core.registry.HabitatBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.random.ChunkRandom;

import java.util.Random;

public abstract class AbstractSlimeFernBlock extends Block implements Fertilizable {
    public AbstractSlimeFernBlock(Settings properties) {
        super(properties);
    }

    /*
     * Particle Animation Method
     */

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(10) == 0) {
            VoxelShape voxelshape = this.getCollisionShape(state, world, pos, ShapeContext.absent());
            Vec3d vector3d = voxelshape.getBoundingBox().getCenter();
            double X = (double) pos.getX() + vector3d.x;
            double Y = (double) pos.getY() + vector3d.y;
            double Z = (double) pos.getZ() + vector3d.z;
            world.addParticle(HabitatParticleTypes.FALLING_SLIME, X + (2 * random.nextDouble() - 1.0F) / 2.5D, Y - random.nextDouble() / 5, Z + (2 * random.nextDouble() - 1.0F) / 2.5D, 0.0D, 0.0D, 0.0D);
        }
    }
    /*
     * Entity Walk Method
     */

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn.getType() != EntityType.SLIME) {
            double motionY = Math.abs(entityIn.getVelocity().y);
            if (motionY < 0.1D && !entityIn.bypassesSteppingEffects()) {
                double slowedMotion = 0.4D + motionY * 0.2D;
                entityIn.setVelocity(entityIn.getVelocity().multiply(slowedMotion, 1.0D, slowedMotion));
            }
        }
    }

    /*
     * IGrowable Methods
     */


    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        if (!isClient) {
            ChunkPos chunkPos = new ChunkPos(pos);
            return ChunkRandom.getSlimeRandom(chunkPos.x, chunkPos.z, ((StructureWorldAccess) world).getSeed(), 987234911L).nextInt(10) == 0;
        }
        return false;
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        Direction[] directions = new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP};

        for (int j = 0; j < 3; ++j) {
            blockpos$mutable.set(pos, MathHelper.nextInt(random, 1, 2) - MathHelper.nextInt(random, 1, 2), MathHelper.nextInt(random, 1, 2) - MathHelper.nextInt(random, 1, 2), MathHelper.nextInt(random, 1, 2) - MathHelper.nextInt(random, 1, 2));

            if (world.isAir(blockpos$mutable) || world.getBlockState(blockpos$mutable).getMaterial().isReplaceable()) {
                for (Direction dir : directions) {
                    if (world.getBlockState(blockpos$mutable.offset(dir)).isSideSolidFullSquare(world, blockpos$mutable, dir.getOpposite())) {
                        BlockState state1 = HabitatBlocks.SLIME_FERN.getDefaultState();

                        if (dir == Direction.UP)
                            state1 = state1.with(SlimeFernBlock.ON_CEILING, true);
                        else if (dir != Direction.DOWN)
                            state1 = HabitatBlocks.WALL_SLIME_FERN.getDefaultState().setValue(WallSlimeFernBlock.HORIZONTAL_FACING, dir.getOpposite());

                        world.setBlockState(blockpos$mutable, state1, 3);
                        break;
                    }
                }
            }
        }
    }

    /*
     * Piston Push Reaction Method
     */

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.DESTROY;
    }

}
