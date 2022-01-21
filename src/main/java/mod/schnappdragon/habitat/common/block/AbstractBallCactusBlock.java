package mod.schnappdragon.habitat.common.block;

import mod.schnappdragon.habitat.common.registry.HabitatBlockTags;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public abstract class AbstractBallCactusBlock extends PlantBlock {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D);
    protected static final Box TOUCH_BOX = new Box(0.125D, 0, 0.125D, 0.875D, 0.4375D, 0.875D);
    private final BallCactusColor color;

    public AbstractBallCactusBlock(BallCactusColor colorIn, Settings properties) {
        super(properties);
        this.color = colorIn;
    }

    public BallCactusColor getColor() {
        return color;
    }


    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    /*
     * Position Validity Method
     */

    public boolean canPlaceAt(BlockState state, WorldView worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isIn(HabitatBlockTags.BALL_CACTUS_PLANTABLE_ON);
    }

    /*
     * Entity Collision Method
     */

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof LivingEntity && entityIn.getType() != EntityType.BEE && worldIn.getOtherEntities(null, TOUCH_BOX.offset(pos)).contains(entityIn)) {
            entityIn.damage(DamageSource.CACTUS, 1.0F);
        }
    }


    /*
     * Pathfinding Method
     */

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}