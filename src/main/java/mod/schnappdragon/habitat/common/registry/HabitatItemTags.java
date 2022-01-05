package mod.schnappdragon.habitat.common.registry;

import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.mixin.ItemTagsInvoker;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag.Identified;

public class HabitatItemTags {
    public static final Identified<Item> BALL_CACTUS_FLOWERS = makeTag("ball_cactus_flowers");
    public static final Identified<Item> BALL_CACTI = makeTag("ball_cacti");
    public static final Identified<Item> FAIRY_RING_MUSHROOM_STEMS = makeTag("fairy_ring_mushroom_stems");
    public static final Identified<Item> POOKA_FOOD = makeTag("pooka_food");
    public static final Identified<Item> PASSERINE_FOOD = makeTag("passerine_food");

    private static Identified<Item> makeTag(String id) {
        return ItemTagsInvoker.invokeRegister(Habitat.MOD_ID + ":" + id);
    }
}
