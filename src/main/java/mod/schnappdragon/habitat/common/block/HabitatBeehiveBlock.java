package mod.schnappdragon.habitat.common.block;

import mod.schnappdragon.habitat.core.registry.HabitatTileEntityTypes;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class HabitatBeehiveBlock extends BeehiveBlock {
    public HabitatBeehiveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return HabitatTileEntityTypes.BEEHIVE.get().create();
    }
}