package mod.schnappdragon.habitat.common.registry;


import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.mixin.BlockTagsMixin;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag.Identified;

public class HabitatBlockTags {
    public static final Identified<Block> RAFFLESIA_PLANTABLE_ON = makeTag("rafflesia_plantable_on");
    public static final Identified<Block> BALL_CACTUS_FLOWERS = makeTag("ball_cactus_flowers");
    public static final Identified<Block> GROWING_BALL_CACTI = makeTag("growing_ball_cacti");
    public static final Identified<Block> BALL_CACTI = makeTag("ball_cacti");
    public static final Identified<Block> FLOWERING_BALL_CACTI = makeTag("flowering_ball_cacti");
    public static final Identified<Block> BALL_CACTUS_PLANTABLE_ON = makeTag("ball_cactus_plantable_on");
    public static final Identified<Block> BALL_CACTUS_FLOWER_PLACEABLE_ON = makeTag("ball_cactus_flower_placeable_on");
    public static final Identified<Block> BEEHIVES = makeTag("beehives");
    public static final Identified<Block> FAIRY_RING_MUSHROOM_STEMS = makeTag("fairy_ring_mushroom_stems");
    public static final Identified<Block> PASSERINE_PERCHABLE_ON = makeTag("passerine_perchable_on");
    public static final Identified<Block> PASSERINE_SPAWNABLE_ON = makeTag("passerine_spawnable_on");

    private static Identified<Block> makeTag(String id) {
        return BlockTagsMixin.invokeRegister(Habitat.MOD_ID + ":" + id);
    }
}
