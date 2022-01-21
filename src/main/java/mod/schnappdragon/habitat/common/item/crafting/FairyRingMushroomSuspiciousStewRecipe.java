package mod.schnappdragon.habitat.common.item.crafting;

import mod.schnappdragon.habitat.common.item.FairyRingMushroomItem;
import mod.schnappdragon.habitat.core.registry.HabitatItems;
import mod.schnappdragon.habitat.core.registry.HabitatRecipeSerializers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.effect.StatusEffect;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.apache.commons.lang3.tuple.Pair;

public class FairyRingMushroomSuspiciousStewRecipe extends SpecialCraftingRecipe {
    public FairyRingMushroomSuspiciousStewRecipe(Identifier idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        boolean brown = false;
        boolean red = false;
        boolean fairy = false;
        boolean bowl = false;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack itemstack = inv.getStack(i);
            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() == Blocks.BROWN_MUSHROOM.asItem() && !brown)
                    brown = true;
                else if (itemstack.getItem() == Blocks.RED_MUSHROOM.asItem() && !red)
                    red = true;
                else if (itemstack.getItem() == HabitatItems.FAIRY_RING_MUSHROOM && !fairy)
                    fairy = true;
                else if (itemstack.getItem() == Items.BOWL && !bowl)
                    bowl = true;
                else
                    return false;
            }
        }

        return brown && red && fairy && bowl;
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        Pair<StatusEffect, Integer> effect = FairyRingMushroomItem.getStewEffect();

        ItemStack stew = new ItemStack(Items.SUSPICIOUS_STEW, 1);
        SuspiciousStewItem.addEffectToStew(stew, effect.getLeft(), effect.getRight());
        return stew;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HabitatRecipeSerializers.CRAFTING_SPECIAL_FAIRYRINGMUSHROOMSUSPICIOUSSTEW;
    }
}
