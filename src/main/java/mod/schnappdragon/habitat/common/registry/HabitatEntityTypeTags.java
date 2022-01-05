package mod.schnappdragon.habitat.common.registry;

import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.mixin.EntityTypeTagsInvoker;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.Tag.Identified;

public class HabitatEntityTypeTags {
    public static final Identified<EntityType<?>> POOKA_ATTACK_TARGETS = makeTag("pooka_attack_targets");
    public static final Identified<EntityType<?>> PACIFIED_POOKA_SCARED_BY = makeTag("pacified_pooka_scared_by");

    private static Identified<EntityType<?>> makeTag(String id) {
        return EntityTypeTagsInvoker.invokeRegister(Habitat.MOD_ID + ":" + id);
    }
}
