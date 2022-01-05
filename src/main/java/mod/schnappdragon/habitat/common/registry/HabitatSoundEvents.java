package mod.schnappdragon.habitat.common.registry;

import mod.schnappdragon.habitat.Habitat;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class HabitatSoundEvents {
    private static final Map<SoundEvent, Identifier> SOUND_EVENTS = new LinkedHashMap<>();

    public static final SoundEvent RAFFLESIA_SPEW = create("block.rafflesia.spew");
    public static final SoundEvent RAFFLESIA_POP = create("block.rafflesia.pop");
    public static final SoundEvent RAFFLESIA_SLURP = create("block.rafflesia.slurp");
    public static final SoundEvent RAFFLESIA_FILL_BOWL = create("block.rafflesia.fill_bowl");

    public static final SoundEvent KABLOOM_BUSH_RUSTLE = create("block.kabloom_bush.rustle");
    public static final SoundEvent KABLOOM_BUSH_SHEAR = create("block.kabloom_bush.shear");

    public static final SoundEvent SLIME_FERN_DROP = create("block.slime_fern.drip");

    public static final SoundEvent FLOWERING_BALL_CACTUS_SHEAR = create("block.flowering_ball_cactus.shear");

    public static final SoundEvent FAIRY_RING_MUSHROOM_SHEAR = create("block.fairy_ring_mushroom.shear");
    public static final SoundEvent FAIRY_RING_MUSHROOM_DUST = create("block.fairy_ring_mushroom.dust");

    public static final SoundEvent KABLOOM_FRUIT_THROW = create("entity.kabloom_fruit.throw");
    public static final SoundEvent KABLOOM_FRUIT_EXPLODE = create("entity.kabloom_fruit.explode");

    public static final SoundEvent POOKA_AMBIENT = create("entity.pooka.ambient");
    public static final SoundEvent POOKA_ATTACK = create("entity.pooka.attack");
    public static final SoundEvent POOKA_DEATH = create("entity.pooka.death");
    public static final SoundEvent POOKA_EAT = create("entity.pooka.eat");
    public static final SoundEvent POOKA_HURT = create("entity.pooka.hurt");
    public static final SoundEvent POOKA_JUMP = create("entity.pooka.jump");
    public static final SoundEvent POOKA_PACIFY = create("entity.pooka.pacify");
    public static final SoundEvent POOKA_SHEAR = create("entity.pooka.shear");
    public static final SoundEvent RABBIT_CONVERTED_TO_POOKA = create("entity.rabbit.converted_to_pooka");
    public static final SoundEvent PARROT_IMITATE_POOKA = create("entity.parrot.imitate.pooka");

    public static final SoundEvent PASSERINE_AMBIENT = create("entity.passerine.ambient");
    public static final SoundEvent PASSERINE_DEATH = create("entity.passerine.death");
    public static final SoundEvent PASSERINE_FLAP = create("entity.passerine.flap");
    public static final SoundEvent PASSERINE_HURT = create("entity.passerine.hurt");
    public static final SoundEvent PASSERINE_STEP = create("entity.passerine.step");

    private static SoundEvent create(String name) {
        Identifier id = new Identifier(Habitat.MOD_ID, name);
        SoundEvent soundEvent = new SoundEvent(id);
        SOUND_EVENTS.put(soundEvent, id);
        return soundEvent;
    }

    public static void init() {
        SOUND_EVENTS.keySet().forEach(effect -> Registry.register(Registry.SOUND_EVENT, SOUND_EVENTS.get(effect), effect));
    }
}