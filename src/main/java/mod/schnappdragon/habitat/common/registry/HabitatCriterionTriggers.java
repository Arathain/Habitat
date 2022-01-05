package mod.schnappdragon.habitat.common.registry;

import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.common.advancement.HabitatCriterionTrigger;
import mod.schnappdragon.habitat.mixin.CriteriaInvoker;
import net.minecraft.util.Identifier;

public class HabitatCriterionTriggers {
    public static final HabitatCriterionTrigger PACIFY_POOKA = new HabitatCriterionTrigger(get("pacify_pooka"));
    public static final HabitatCriterionTrigger FEED_PASSERINE = new HabitatCriterionTrigger(get("feed_passerine"));

    public static void registerCriteriaTriggers() {
        CriteriaInvoker.register(PACIFY_POOKA);
        CriteriaInvoker.register(FEED_PASSERINE);
    }

    private static Identifier get(String id) {
        return new Identifier(Habitat.MOD_ID, id);
    }
}