package mod.schnappdragon.habitat.common.worldgen.feature;

import com.mojang.serialization.Codec;
import mod.schnappdragon.habitat.common.block.FairyRingMushroomBlock;
import mod.schnappdragon.habitat.core.registry.HabitatBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

import java.util.Random;

public class HugeFairyRingMushroomFeature extends HugeMushroomFeature {
    public HugeFairyRingMushroomFeature(Codec<HugeMushroomFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<HugeMushroomFeatureConfig> context) {
        HugeMushroomFeatureConfig config = context.getConfig();
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();
        int i = this.getHeight(random);
        BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();

        if (!this.canGenerate(world, pos, i, blockpos$mutableblockpos, config)) {
            return false;
        } else {
            this.generateCap(world, random, pos, i, blockpos$mutableblockpos, config);
            this.generateStem(world, random, pos, config, i, blockpos$mutableblockpos);
            return true;
        }
    }


    @Override
    protected int getHeight(Random rand) {
        int i = rand.nextInt(2) + 10;
        if (rand.nextInt(12) == 0)
            i *= 2;

        return i;
    }

    @Override
    protected void generateStem(WorldAccess world, Random rand, BlockPos pos, HugeMushroomFeatureConfig config, int i0, BlockPos.Mutable blockpos$mutable) {
        WeightedBlockStateProvider mushroomProvider = new WeightedBlockStateProvider(DataPool.<BlockState>builder().add(HabitatBlocks.FAIRY_RING_MUSHROOM.defaultBlockState(), 1).add(HabitatBlocks.FAIRY_RING_MUSHROOM.defaultBlockState().setValue(FairyRingMushroomBlock.MUSHROOMS, 2), 2).add(HabitatBlocks.FAIRY_RING_MUSHROOM.defaultBlockState().setValue(FairyRingMushroomBlock.MUSHROOMS, 3), 3).add(HabitatBlocks.FAIRY_RING_MUSHROOM.defaultBlockState().setValue(FairyRingMushroomBlock.MUSHROOMS, 4), 3));

        BlockState stem = config.stemProvider.getBlockState(rand, pos);
        if (CompatHelper.checkMods("enhanced_mushrooms"))
            stem = HabitatBlocks.ENHANCED_FAIRY_RING_MUSHROOM_STEM.defaultBlockState();

        for (int i = 0; i < i0; ++i) {
            blockpos$mutable.set(pos).move(Direction.UP, i);
            if (!world.getBlockState(blockpos$mutable).isOpaqueFullCube(world, blockpos$mutable)) {
                this.setBlockState(world, blockpos$mutable, stem);
            }

            boolean breakFlag = false;
            if (i > i0 - 7) {
                for (int x = -1; x <= 1; ++x) {
                    for (int z = -1; z <= 1; ++z) {
                        BlockPos.Mutable inPos = new BlockPos.Mutable().set(blockpos$mutable, x, 0, z);
                        if (!world.getBlockState(inPos).isOpaqueFullCube(world, inPos)) {
                            if (i > i0 - 6 && (x != 0 || z != 0) && rand.nextInt(12) == 0 && !world.getBlockState(inPos.down()).isOf(HabitatBlocks.FAIRYLIGHT)) {
                                this.setBlockState(world, inPos, HabitatBlocks.FAIRYLIGHT.defaultBlockState());
                                breakFlag = true;
                                break;
                            }
                        }
                    }
                    if (breakFlag)
                        break;
                }
            }
        }

        for (Direction dir : new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}) {
            int len = rand.nextInt(3) > 0 ? rand.nextBoolean() ? 1 + MathHelper.ceil((float) rand.nextInt(i0 - 7) / 2) : MathHelper.ceil((float) rand.nextInt(i0 - 7) / 2) : 0;
            blockpos$mutable.set(pos, dir.getOffsetX(), -1, dir.getOffsetZ());

            for (int i = 0; i < len + 1; ++i) {
                blockpos$mutable.move(Direction.UP);
                if (!world.getBlockState(blockpos$mutable).isOpaqueFullCube(world, blockpos$mutable) && world.getBlockState(blockpos$mutable.down()).isOpaqueFullCube(world, blockpos$mutable.down())) {
                    if (i < len) {
                        BlockState stemState = stem;
                        if (stemState.getBlock() instanceof MushroomBlock) {
                            stemState = stemState.with(MushroomBlock.UP, i == len - 1);
                            if (world.getBlockState(blockpos$mutable.offset(dir.getOpposite())).isOf(stemState.getBlock())) {
                                stemState = stemState.with(getPropertyFromDirection(dir.getOpposite()), false);
                                this.setBlockState(world, blockpos$mutable.offset(dir.getOpposite()), world.getBlockState(blockpos$mutable.offset(dir.getOpposite())).with(getPropertyFromDirection(dir), false));
                            }
                        }
                        this.setBlockState(world, blockpos$mutable, stemState);
                    } else if (world.getBlockState(blockpos$mutable).isAir() && rand.nextInt(3) == 0)
                        this.setBlockState(world, blockpos$mutable, mushroomProvider.getBlockState(rand, blockpos$mutable));
                }
            }
        }

        for (int i = -1; i <= 1; i += 2) {
            for (int k = -1; k <= 1; k += 2) {
                for (int j = -1; j <= 1; ++j) {
                    blockpos$mutable.set(pos, i, j, k);

                    if (world.getBlockState(blockpos$mutable).isAir() && world.getBlockState(blockpos$mutable.down()).isOpaqueFullCube(world, blockpos$mutable.down())) {
                        if (rand.nextInt(3) == 0)
                            this.setBlockState(world, blockpos$mutable, mushroomProvider.getBlockState(rand, blockpos$mutable));
                        break;
                    }
                }
            }
        }
    }

    private BooleanProperty getPropertyFromDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> MushroomBlock.NORTH;
            case EAST -> MushroomBlock.EAST;
            case SOUTH -> MushroomBlock.SOUTH;
            case WEST -> MushroomBlock.WEST;
            default -> null;
        };
    }

    protected void generateCap(WorldAccess world, Random rand, BlockPos pos, int i0, BlockPos.Mutable blockpos$mutable, HugeMushroomFeatureConfig config) {
        for (int i = i0 - 6; i <= i0; ++i) {
            int j = i < i0 ? config.foliageRadius : config.foliageRadius - 1;
            int k = config.foliageRadius - 2;

            for (int l = -j; l <= j; ++l) {
                for (int i1 = -j; i1 <= j; ++i1) {
                    boolean flag = l == -j;
                    boolean flag1 = l == j;
                    boolean flag2 = i1 == -j;
                    boolean flag3 = i1 == j;
                    boolean flag4 = flag || flag1;
                    boolean flag5 = flag2 || flag3;
                    if (i >= i0 || flag4 != flag5) {
                        blockpos$mutable.set(pos, l, i, i1);
                        if (!world.getBlockState(blockpos$mutable).isOpaqueFullCube(world, blockpos$mutable)) {
                            this.setBlockState(world, blockpos$mutable, config.capProvider.getBlockState(rand, pos).with(MushroomBlock.UP, i >= i0 - 1).with(MushroomBlock.WEST, l < -k).with(MushroomBlock.EAST, l > k).with(MushroomBlock.NORTH, i1 < -k).with(MushroomBlock.SOUTH, i1 > k));
                        }
                    }
                }
            }
        }
    }

    protected int getCapSize(int i1, int i2, int i3, int i4) {
        return i4 < i2 && i4 >= i2 - 3 || i4 == i2 ? i3 : 0;
    }
}