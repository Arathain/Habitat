package mod.schnappdragon.habitat.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.state.property.Properties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nullable;

public class FuelBlockItem extends BlockItem {
    private final int burnTime;

    public FuelBlockItem(Block block, int burnTime, Settings properties) {
        super(block, properties);
        this.burnTime = burnTime;
    }


    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return burnTime;
    }
    //TODO figure out what the fuck this is
}