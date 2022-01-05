package mod.schnappdragon.habitat.common.block;

import mod.schnappdragon.habitat.core.registry.HabitatBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;

public class HabitatChestBlock extends ChestBlock implements VariantChest {
    private final ChestVariant variant;

    public HabitatChestBlock(ChestVariant variantIn, Properties properties) {
        super(properties, HabitatBlockEntityTypes.CHEST);
        this.variant = variantIn;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return HabitatBlockEntityTypes.CHEST.create(pos, state);
    }

    @Override
    public ChestVariant getVariant() {
        return variant;
    }
}