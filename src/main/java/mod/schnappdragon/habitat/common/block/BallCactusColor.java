package mod.schnappdragon.habitat.common.block;

import mod.schnappdragon.habitat.Habitat;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public enum BallCactusColor {
    PINK("pink"),
    RED("red"),
    ORANGE("orange"),
    YELLOW("yellow");

    private final String color;

    BallCactusColor(String colorIn) {
        this.color = colorIn;
    }

    public Item getFlower() {
        return Registry.ITEM.get(new Identifier(Habitat.MOD_ID, this.color + "_ball_cactus_flower"));
    }

    public Block getGrowingBallCactus() {
        return Registry.BLOCK.get(new Identifier(Habitat.MOD_ID, "growing_" + this.color + "_ball_cactus"));
    }

    public Block getBallCactus() {
        return Registry.BLOCK.get(new Identifier(Habitat.MOD_ID, this.color + "_ball_cactus"));
    }

    public Block getFloweringBallCactus() {
        return Registry.BLOCK.get(new Identifier(Habitat.MOD_ID, "flowering_" + this.color + "_ball_cactus"));
    }
}