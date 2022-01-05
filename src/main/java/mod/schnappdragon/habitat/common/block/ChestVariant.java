package mod.schnappdragon.habitat.common.block;

import mod.schnappdragon.habitat.Habitat;
import net.minecraft.resources.Identifier;

public enum ChestVariant {
    FAIY_RING_MUSHROOM_NORMAL("fairy_ring_mushroom"),
    FAIY_RING_MUSHROOM_TRAPPED("fairy_ring_mushroom", true);

    private final String location;

    ChestVariant(String name, boolean trapped) {
        this.location = name + "_" + (trapped ? "trapped" : "normal");
    }

    ChestVariant(String name) {
        this(name, false);
    }

    public Identifier getSingle() {
        return new Identifier(Habitat.MOD_ID, "entity/chest/" + this.location);
    }

    public Identifier getRight() {
        return new Identifier(Habitat.MOD_ID, "entity/chest/" + this.location + "_right");
    }

    public Identifier getLeft() {
        return new Identifier(Habitat.MOD_ID, "entity/chest/" + this.location + "_left");
    }
}