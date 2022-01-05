package mod.schnappdragon.habitat.core.registry;

import com.mojang.serialization.Codec;
import mod.schnappdragon.habitat.client.particle.ColorableParticleEffect;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;

public class HabitatParticleTypes {
    public static DefaultParticleType FALLING_SLIME;
    public static DefaultParticleType LANDING_SLIME;
    public static DefaultParticleType FAIRY_RING_SPORE;
    public static ParticleType<ColorableParticleEffect> FEATHER;
    public static ParticleType<ColorableParticleEffect> NOTE;


    public static void init() {
        FALLING_SLIME = Registry.register(Registry.PARTICLE_TYPE, "habitat:falling_slime", FabricParticleTypes.simple(false));
        LANDING_SLIME = Registry.register(Registry.PARTICLE_TYPE, "habitat:landing_slime", FabricParticleTypes.simple(false));
        FAIRY_RING_SPORE = Registry.register(Registry.PARTICLE_TYPE, "habitat:fairy_ring_spore", FabricParticleTypes.simple(false));
        FEATHER = Registry.register(Registry.PARTICLE_TYPE, "habitat:feather", new ParticleType<ColorableParticleEffect>(true, ColorableParticleEffect.DESERIALIZER) {
            @Override
            public Codec<ColorableParticleEffect> getCodec() {
                return ColorableParticleEffect.codec(FEATHER);
            }
        });
        NOTE = Registry.register(Registry.PARTICLE_TYPE, "habitat:note", new ParticleType<ColorableParticleEffect>(true, ColorableParticleEffect.DESERIALIZER) {
            @Override
            public Codec<ColorableParticleEffect> getCodec() {
                return ColorableParticleEffect.codec(NOTE);
            }
        });
    }


}
