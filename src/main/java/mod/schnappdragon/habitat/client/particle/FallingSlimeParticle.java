package mod.schnappdragon.habitat.client.particle;

import mod.schnappdragon.habitat.HabitatClient;
import mod.schnappdragon.habitat.common.registry.HabitatSoundEvents;
import mod.schnappdragon.habitat.common.registry.HabitatParticleTypes;
import net.minecraft.client.particle.BlockLeakParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;

public class FallingSlimeParticle extends BlockLeakParticle.ContinuousFalling {
    private FallingSlimeParticle(ClientWorld world, double x, double y, double z, Fluid fluid, ParticleEffect particleData) {
        super(world, x, y, z, fluid, particleData);
    }

    protected void updateVelocity() {
        if (this.onGround) {
            this.markDead();
            this.world.addParticle(this.nextParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
            this.world.playSound(this.x + 0.5D, this.y, this.z + 0.5D, HabitatSoundEvents.SLIME_FERN_DROP, SoundCategory.BLOCKS, 0.3F + this.world.random.nextFloat() * 2.0F / 3.0F, 1.0F, false);
        }
    }

    public static class FallingSlimeFactory implements ParticleFactory<DefaultParticleType> {
        protected final SpriteProvider SpriteProvider;

        public FallingSlimeFactory(SpriteProvider SpriteProvider) {
            this.SpriteProvider = SpriteProvider;
        }

        public Particle createParticle(DefaultParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingSlimeParticle slimeparticle = new FallingSlimeParticle(worldIn, x, y, z, Fluids.EMPTY, HabitatParticleTypes.LANDING_SLIME);
            slimeparticle.gravityStrength = 0.01F;
            slimeparticle.setColor(0.463F, 0.745F, 0.427F);
            slimeparticle.setSprite(this.SpriteProvider);
            return slimeparticle;
        }
    }
}