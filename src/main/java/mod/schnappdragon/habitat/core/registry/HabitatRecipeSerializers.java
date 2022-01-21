package mod.schnappdragon.habitat.core.registry;

import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.common.item.crafting.FairyRingMushroomSuspiciousStewRecipe;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class HabitatRecipeSerializers {
    public static final Map<RecipeSerializer<?>, Identifier> RECIPE_SERIALIZERS = new LinkedHashMap<>();

    public static final RecipeSerializer<FairyRingMushroomSuspiciousStewRecipe> CRAFTING_SPECIAL_FAIRYRINGMUSHROOMSUSPICIOUSSTEW = register("crafting_special_fairyringmushroomsuspiciousstew", new SpecialRecipeSerializer<>(FairyRingMushroomSuspiciousStewRecipe::new));

    private static <T extends CraftingRecipe> RecipeSerializer<T> register(String name, RecipeSerializer<T> recipe) {
        RECIPE_SERIALIZERS.put(recipe, new Identifier(Habitat.MOD_ID, name));
        return recipe;
    }

    public static void init() {
        RECIPE_SERIALIZERS.keySet().forEach(entityType -> Registry.register(Registry.RECIPE_SERIALIZER, RECIPE_SERIALIZERS.get(entityType), entityType));
    }
}
