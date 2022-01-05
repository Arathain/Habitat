package mod.schnappdragon.habitat.common.block;

import mod.schnappdragon.habitat.Habitat;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

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
        return ForgeRegistries.ITEMS.getValue(new Identifier(Habitat.MOD_ID, this.color + "_ball_cactus_flower"));
    }

    public Block getGrowingBallCactus() {
        return ForgeRegistries.BLOCKS.getValue(new Identifier(Habitat.MOD_ID, "growing_" + this.color + "_ball_cactus"));
    }

    public Block getBallCactus() {
        return ForgeRegistries.BLOCKS.getValue(new Identifier(Habitat.MOD_ID, this.color + "_ball_cactus"));
    }

    public Block getFloweringBallCactus() {
        return ForgeRegistries.BLOCKS.getValue(new Identifier(Habitat.MOD_ID, "flowering_" + this.color + "_ball_cactus"));
    }
}