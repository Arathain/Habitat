package mod.schnappdragon.habitat.core.registry;

import mod.schnappdragon.habitat.common.block.*;
import mod.schnappdragon.habitat.Habitat;
import net.minecraft.block.Block;
import net.minecraft.core.Direction;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class HabitatBlocks {
    private static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
    private static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    public static final RegistryObject<Block> RAFFLESIA = BLOCKS.register("rafflesia", () -> new RafflesiaBlock(BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.COLOR_RED).instabreak().randomTicks().sound(SoundType.GRASS).noCollission().noOcclusion()));
    public static final RegistryObject<Block> POTTED_RAFFLESIA = BLOCKS.register("potted_rafflesia", () -> new FlowerPotBlock(RAFFLESIA.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));

    public static final RegistryObject<Block> KABLOOM_BUSH = BLOCKS.register("kabloom_bush", () -> new KabloomBushBlock(BlockBehaviour.Properties.of(Material.PLANT).instabreak().sound(SoundType.GRASS).randomTicks().noCollission().noOcclusion()));
    public static final RegistryObject<Block> POTTED_KABLOOM_BUSH = BLOCKS.register("potted_kabloom_bush", () -> new FlowerPotBlock(KABLOOM_BUSH.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> KABLOOM_FRUIT_PILE = BLOCKS.register("kabloom_fruit_pile", () -> new KabloomFruitPileBlock(BlockBehaviour.Properties.of(Material.GRASS, MaterialColor.COLOR_GREEN).instabreak().sound(SoundType.WOOL)));
    public static final RegistryObject<Block> KABLOOM_PULP_BLOCK = BLOCKS.register("kabloom_pulp_block", () -> new KabloomPulpBlock(BlockBehaviour.Properties.of(Material.GRASS, MaterialColor.GRASS).sound(SoundType.SLIME_BLOCK)));

    public static final RegistryObject<Block> SLIME_FERN = BLOCKS.register("slime_fern", () -> new SlimeFernBlock(BlockBehaviour.Properties.of(Material.PLANT).instabreak().sound(SoundType.GRASS).noCollission().noOcclusion()));
    public static final RegistryObject<Block> WALL_SLIME_FERN = BLOCKS.register("wall_slime_fern", () -> new WallSlimeFernBlock(BlockBehaviour.Properties.of(Material.PLANT).instabreak().lootFrom(SLIME_FERN).sound(SoundType.GRASS).noCollission().noOcclusion()));
    public static final RegistryObject<Block> POTTED_SLIME_FERN = BLOCKS.register("potted_slime_fern", () -> new FlowerPotBlock(SLIME_FERN.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));

    public static final RegistryObject<Block> ORANGE_BALL_CACTUS_FLOWER = BLOCKS.register("orange_ball_cactus_flower", () -> new BallCactusFlowerBlock(BallCactusColor.ORANGE, HabitatEffects.PRICKLING, 5, BlockBehaviour.Properties.of(Material.PLANT).instabreak().sound(SoundType.GRASS).noCollission().noOcclusion().randomTicks()));
    public static final RegistryObject<Block> PINK_BALL_CACTUS_FLOWER = BLOCKS.register("pink_ball_cactus_flower", () -> new BallCactusFlowerBlock(BallCactusColor.PINK, HabitatEffects.PRICKLING, 5, BlockBehaviour.Properties.of(Material.PLANT).instabreak().sound(SoundType.GRASS).noCollission().noOcclusion().randomTicks()));
    public static final RegistryObject<Block> RED_BALL_CACTUS_FLOWER = BLOCKS.register("red_ball_cactus_flower", () -> new BallCactusFlowerBlock(BallCactusColor.RED, HabitatEffects.PRICKLING, 5, BlockBehaviour.Properties.of(Material.PLANT).instabreak().sound(SoundType.GRASS).noCollission().noOcclusion().randomTicks()));
    public static final RegistryObject<Block> YELLOW_BALL_CACTUS_FLOWER = BLOCKS.register("yellow_ball_cactus_flower", () -> new BallCactusFlowerBlock(BallCactusColor.YELLOW, HabitatEffects.PRICKLING, 5, BlockBehaviour.Properties.of(Material.PLANT).instabreak().sound(SoundType.GRASS).noCollission().noOcclusion().randomTicks()));
    public static final RegistryObject<Block> GROWING_ORANGE_BALL_CACTUS = BLOCKS.register("growing_orange_ball_cactus", () -> new GrowingBallCactusBlock(BallCactusColor.ORANGE, BlockBehaviour.Properties.of(Material.CACTUS).instabreak().sound(SoundType.WOOL).noOcclusion().randomTicks()));
    public static final RegistryObject<Block> GROWING_PINK_BALL_CACTUS = BLOCKS.register("growing_pink_ball_cactus", () -> new GrowingBallCactusBlock(BallCactusColor.PINK, BlockBehaviour.Properties.of(Material.CACTUS).instabreak().sound(SoundType.WOOL).noOcclusion().randomTicks()));
    public static final RegistryObject<Block> GROWING_RED_BALL_CACTUS = BLOCKS.register("growing_red_ball_cactus", () -> new GrowingBallCactusBlock(BallCactusColor.RED, BlockBehaviour.Properties.of(Material.CACTUS).instabreak().sound(SoundType.WOOL).noOcclusion().randomTicks()));
    public static final RegistryObject<Block> GROWING_YELLOW_BALL_CACTUS = BLOCKS.register("growing_yellow_ball_cactus", () -> new GrowingBallCactusBlock(BallCactusColor.YELLOW, BlockBehaviour.Properties.of(Material.CACTUS).instabreak().sound(SoundType.WOOL).noOcclusion().randomTicks()));
    public static final RegistryObject<Block> ORANGE_BALL_CACTUS = BLOCKS.register("orange_ball_cactus", () -> new BallCactusBlock(BallCactusColor.ORANGE, BlockBehaviour.Properties.of(Material.CACTUS).strength(0.4F).sound(SoundType.WOOL).noOcclusion().randomTicks()));
    public static final RegistryObject<Block> PINK_BALL_CACTUS = BLOCKS.register("pink_ball_cactus", () -> new BallCactusBlock(BallCactusColor.PINK, BlockBehaviour.Properties.of(Material.CACTUS).strength(0.4F).sound(SoundType.WOOL).noOcclusion().randomTicks()));
    public static final RegistryObject<Block> RED_BALL_CACTUS = BLOCKS.register("red_ball_cactus", () -> new BallCactusBlock(BallCactusColor.RED, BlockBehaviour.Properties.of(Material.CACTUS).strength(0.4F).sound(SoundType.WOOL).noOcclusion().randomTicks()));
    public static final RegistryObject<Block> YELLOW_BALL_CACTUS = BLOCKS.register("yellow_ball_cactus", () -> new BallCactusBlock(BallCactusColor.YELLOW, BlockBehaviour.Properties.of(Material.CACTUS).strength(0.4F).sound(SoundType.WOOL).noOcclusion().randomTicks()));
    public static final RegistryObject<Block> FLOWERING_ORANGE_BALL_CACTUS = BLOCKS.register("flowering_orange_ball_cactus", () -> new FloweringBallCactusBlock(BallCactusColor.ORANGE, BlockBehaviour.Properties.of(Material.CACTUS).strength(0.4F).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<Block> FLOWERING_PINK_BALL_CACTUS = BLOCKS.register("flowering_pink_ball_cactus", () -> new FloweringBallCactusBlock(BallCactusColor.PINK, BlockBehaviour.Properties.of(Material.CACTUS).strength(0.4F).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<Block> FLOWERING_RED_BALL_CACTUS = BLOCKS.register("flowering_red_ball_cactus", () -> new FloweringBallCactusBlock(BallCactusColor.RED, BlockBehaviour.Properties.of(Material.CACTUS).strength(0.4F).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<Block> FLOWERING_YELLOW_BALL_CACTUS = BLOCKS.register("flowering_yellow_ball_cactus", () -> new FloweringBallCactusBlock(BallCactusColor.YELLOW, BlockBehaviour.Properties.of(Material.CACTUS).strength(0.4F).sound(SoundType.WOOL).noOcclusion()));
    public static final RegistryObject<Block> POTTED_ORANGE_BALL_CACTUS_FLOWER = BLOCKS.register("potted_orange_ball_cactus_flower", () -> new FlowerPotBlock(ORANGE_BALL_CACTUS_FLOWER.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_PINK_BALL_CACTUS_FLOWER = BLOCKS.register("potted_pink_ball_cactus_flower", () -> new FlowerPotBlock(PINK_BALL_CACTUS_FLOWER.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_RED_BALL_CACTUS_FLOWER = BLOCKS.register("potted_red_ball_cactus_flower", () -> new FlowerPotBlock(RED_BALL_CACTUS_FLOWER.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_YELLOW_BALL_CACTUS_FLOWER = BLOCKS.register("potted_yellow_ball_cactus_flower", () -> new FlowerPotBlock(YELLOW_BALL_CACTUS_FLOWER.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_ORANGE_BALL_CACTUS = BLOCKS.register("potted_orange_ball_cactus", () -> new FlowerPotBlock(ORANGE_BALL_CACTUS.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_PINK_BALL_CACTUS = BLOCKS.register("potted_pink_ball_cactus", () -> new FlowerPotBlock(PINK_BALL_CACTUS.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_RED_BALL_CACTUS = BLOCKS.register("potted_red_ball_cactus", () -> new FlowerPotBlock(RED_BALL_CACTUS.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<Block> POTTED_YELLOW_BALL_CACTUS = BLOCKS.register("potted_yellow_ball_cactus", () -> new FlowerPotBlock(YELLOW_BALL_CACTUS.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));

    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM = BLOCKS.register("fairy_ring_mushroom", () -> new FairyRingMushroomBlock(BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.COLOR_YELLOW).noCollission().instabreak().sound(SoundType.GRASS).hasPostProcess((state, reader, pos) -> true).lightLevel((state) -> 11 + state.getValue(FairyRingMushroomBlock.MUSHROOMS)).noOcclusion().randomTicks()));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_BLOCK = BLOCKS.register("fairy_ring_mushroom_block", () -> new HugeFairyRingMushroomBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).lightLevel((state) -> 15).strength(0.2F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_STEM = BLOCKS.register("fairy_ring_mushroom_stem", () -> new HugeMushroomBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.QUARTZ).strength(0.2F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRYLIGHT = BLOCKS.register("fairylight", () -> new Block(BlockBehaviour.Properties.of(Material.GRASS, MaterialColor.SAND).strength(1.0F).sound(SoundType.SHROOMLIGHT).lightLevel((state) -> 15)));
    public static final RegistryObject<Block> POTTED_FAIRY_RING_MUSHROOM = BLOCKS.register("potted_fairy_ring_mushroom", () -> new FlowerPotBlock(FAIRY_RING_MUSHROOM.get(), BlockBehaviour.Properties.copy(Blocks.FLOWER_POT).lightLevel((state) -> 12)));
    public static final RegistryObject<Block> FAIRY_SPORE_LANTERN = BLOCKS.register("fairy_spore_lantern", () -> new FairySporeLanternBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel((state) -> 12).noOcclusion()));

    public static final RegistryObject<Block> STRIPPED_FAIRY_RING_MUSHROOM_STEM = BLOCKS.register("stripped_fairy_ring_mushroom_stem", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> ENHANCED_FAIRY_RING_MUSHROOM_STEM = BLOCKS.register("enhanced_fairy_ring_mushroom_stem", () -> new LogBlock(STRIPPED_FAIRY_RING_MUSHROOM_STEM, BlockBehaviour.Properties.of(Material.WOOD, (state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MaterialColor.COLOR_YELLOW : MaterialColor.QUARTZ).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> STRIPPED_FAIRY_RING_MUSHROOM_HYPHAE = BLOCKS.register("stripped_fairy_ring_mushroom_hyphae", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_HYPHAE = BLOCKS.register("fairy_ring_mushroom_hyphae", () -> new LogBlock(STRIPPED_FAIRY_RING_MUSHROOM_HYPHAE, BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.QUARTZ).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_PLANKS = BLOCKS.register("fairy_ring_mushroom_planks", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_SLAB = BLOCKS.register("fairy_ring_mushroom_slab", () -> new SlabBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_STAIRS = BLOCKS.register("fairy_ring_mushroom_stairs", () -> new StairBlock(FAIRY_RING_MUSHROOM_PLANKS.get().getDefaultState(), BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_PRESSURE_PLATE = BLOCKS.register("fairy_ring_mushroom_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).noCollission().strength(0.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_FENCE = BLOCKS.register("fairy_ring_mushroom_fence", () -> new FenceBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_FENCE_GATE = BLOCKS.register("fairy_ring_mushroom_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_BUTTON = BLOCKS.register("fairy_ring_mushroom_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_TRAPDOOR = BLOCKS.register("fairy_ring_mushroom_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(3.0F).sound(SoundType.WOOD).noOcclusion().isValidSpawn((state, reader, pos, entity) -> false)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_DOOR = BLOCKS.register("fairy_ring_mushroom_door", () -> new DoorBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_SIGN = BLOCKS.register("fairy_ring_mushroom_sign", () -> new HabitatStandingSignBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).noCollission().strength(1.0F).sound(SoundType.WOOD), HabitatWoodType.FAIRY_RING_MUSHROOM));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_WALL_SIGN = BLOCKS.register("fairy_ring_mushroom_wall_sign", () -> new HabitatWallSignBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).noCollission().strength(1.0F).sound(SoundType.WOOD).lootFrom(FAIRY_RING_MUSHROOM_SIGN), HabitatWoodType.FAIRY_RING_MUSHROOM));

    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_VERTICAL_SLAB = BLOCKS.register("fairy_ring_mushroom_vertical_slab", () -> new VerticalSlabBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_BOOKSHELF = BLOCKS.register("fairy_ring_mushroom_bookshelf", () -> new BookshelfBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(1.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_LADDER = BLOCKS.register("fairy_ring_mushroom_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER).noOcclusion()));
    public static final RegistryObject<Block> STRIPPED_FAIRY_RING_MUSHROOM_POST = BLOCKS.register("stripped_fairy_ring_mushroom_post", () -> new WoodPostBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_POST = BLOCKS.register("fairy_ring_mushroom_post", () -> new WoodPostBlock(STRIPPED_FAIRY_RING_MUSHROOM_POST, BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_CHEST = BLOCKS.register("fairy_ring_mushroom_chest", () -> new HabitatChestBlock(ChestVariant.FAIY_RING_MUSHROOM_NORMAL, BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(2.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_TRAPPED_CHEST = BLOCKS.register("fairy_ring_mushroom_trapped_chest", () -> new HabitatTrappedChestBlock(ChestVariant.FAIY_RING_MUSHROOM_TRAPPED, BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(2.5F).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> FAIRY_RING_MUSHROOM_BEEHIVE = BLOCKS.register("fairy_ring_mushroom_beehive", () -> new HabitatBeehiveBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_YELLOW).strength(0.3F).sound(SoundType.WOOD)));

    private static <T extends Block> T create(String name, T block, boolean createItem) {
        BLOCKS.put(block, new Identifier(Habitat.MOD_ID, name));
        if (createItem) {
            ITEMS.put(new BlockItem(block, new Item.Settings().group(Habitat.GROUP)), BLOCKS.get(block));
        }
        return block;
    }

}