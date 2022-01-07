package mod.schnappdragon.habitat.client.particle;

import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.common.registry.HabitatParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Habitat.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HabitatParticleProviders {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        registerFactory(HabitatParticleTypes.FALLING_SLIME.get(), FallingSlimeParticle.FallingSlimeProvider::new);
        registerFactory(HabitatParticleTypes.LANDING_SLIME.get(), LandingSlimeParticle.LandingSlimeProvider::new);
        registerFactory(HabitatParticleTypes.FAIRY_RING_SPORE.get(), FairyRingSporeParticle.Provider::new);
        registerFactory(HabitatParticleTypes.FEATHER.get(), FeatherParticle.Provider::new);
        registerFactory(HabitatParticleTypes.NOTE.get(), NoteParticle.Provider::new);
    }

    private static <T extends ParticleOptions> void registerFactory(ParticleType<T> particle, ParticleEngine.SpriteParticleRegistration<T> factory) {
        Minecraft.getInstance().particleEngine.register(particle, factory);
    }
}