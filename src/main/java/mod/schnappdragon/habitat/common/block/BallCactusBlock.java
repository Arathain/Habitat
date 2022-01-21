package mod.schnappdragon.habitat.common.block;


import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Random;

public class BallCactusBlock extends AbstractBallCactusBlock implements Fertilizable {
    public BallCactusBlock(BallCactusColor colorIn, Settings properties) {
        super(colorIn, properties);
    }

    /*
     * Flower Adding Method
     */

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).getItem() == getColor().getFlower()) {
            if (!player.getAbilities().creativeMode)
                player.getStackInHand(hand).decrement(1);
            world.setBlockState(pos, getColor().getFloweringBallCactus().getDefaultState(), 2);
            world.playSound(null, pos, BlockSoundGroup.GRASS.getPlaceSound(), SoundCategory.BLOCKS, BlockSoundGroup.GRASS.getVolume() + 1.0F / 2.0F, BlockSoundGroup.GRASS.getPitch() * 0.8F);
            world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return ActionResult.success(world.isClient);
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    /*
     * Growth Methods
     */

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, getColor().getFloweringBallCactus().getDefaultState());
        super.randomTick(state, world, pos, random);
    }

    public boolean isFertilizable(BlockView var1, BlockPos var2, BlockState var3, boolean var4) {
        return true;
    }

    public boolean canGrow(World var1, Random var2, BlockPos var3, BlockState var4) {
        return true;
    }

    public void grow(ServerWorld world, Random var2, BlockPos pos, BlockState var4) {
        world.setBlockState(pos, getColor().getFloweringBallCactus().getDefaultState());
    }
}
