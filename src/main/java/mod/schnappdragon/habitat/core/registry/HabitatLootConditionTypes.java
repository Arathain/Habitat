package mod.schnappdragon.habitat.core.registry;

import mod.schnappdragon.habitat.common.loot.conditions.IsModLoaded;
import mod.schnappdragon.habitat.Habitat;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class HabitatLootConditionTypes {
    public static LootItemConditionType IS_MOD_LOADED;

    public static void registerLootConditionTypes() {
        IS_MOD_LOADED = register("is_mod_loaded", new IsModLoaded.Serializer());
    }

    private static LootItemConditionType register(final String name, final Serializer<? extends LootItemCondition> serializer) {
        return Registry.register(Registry.LOOT_CONDITION_TYPE, new Identifier(Habitat.MOD_ID, name), new LootItemConditionType(serializer));
    }
}
