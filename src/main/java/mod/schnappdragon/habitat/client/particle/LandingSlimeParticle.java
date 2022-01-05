package mod.schnappdragon.habitat.client.particle;

import net.minecraft.client.particle.BlockLeakParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.DefaultParticleType;

public class LandingSlimeParticle extends BlockLeakParticle.Landing {
    public LandingSlimeParticle(ClientWorld world, double x, double y, double z, Fluid fluid) {
        super(world, x, y, z, fluid);
    }

    public static class LandingSlimeFactory implements ParticleFactory<DefaultParticleType> {
        protected final SpriteProvider SpriteProvider;

        public LandingSlimeFactory(SpriteProvider SpriteProvider) {
            this.SpriteProvider = SpriteProvider;
        }

        public Particle createParticle(DefaultParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            LandingSlimeParticle slimeparticle = new LandingSlimeParticle(worldIn, x, y, z, Fluids.EMPTY);
            slimeparticle.setMaxAge((int) (128.0D / (Math.random() * 0.8D + 0.2D)));
            slimeparticle.setColor(0.443F, 0.675F, 0.427F);
            slimeparticle.setSprite(this.SpriteProvider);
            return slimeparticle;
        }
    }
}
