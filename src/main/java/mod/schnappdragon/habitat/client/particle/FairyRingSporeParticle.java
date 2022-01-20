package mod.schnappdragon.habitat.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;

public class FairyRingSporeParticle extends SpriteBillboardParticle {
    private final SpriteProvider SpriteProviderWithAge;

    private FairyRingSporeParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteProvider SpriteProviderWithAge) {
        super(world, x, y, z);
        this.SpriteProviderWithAge = SpriteProviderWithAge;
        this.maxAge = (int) (60 + random.nextDouble() * 60);
        this.gravityStrength = 0.0001F;
        this.velocityMultiplier = 0.99F;
        float f = 0.9F + this.random.nextFloat() * 0.1F;
        this.colorRed = f;
        this.colorGreen = f * 0.98F;
        this.colorBlue = f * 0.98F;
        this.scale *= 0.8F;
        this.velocityX = motionX;
        this.velocityY = motionY;
        this.velocityZ = motionZ;
        this.setSpriteForAge(SpriteProviderWithAge);
    }

    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge || this.onGround || this.world.testFluidState(new BlockPos(this.x, this.y, this.z), (fluidState) -> !fluidState.isEmpty()))
            this.markDead();
        else {
            this.velocityY -= this.gravityStrength;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.setSpriteForAge(this.SpriteProviderWithAge);

            this.velocityX *= this.velocityMultiplier;
            this.velocityY *= this.velocityMultiplier;
            this.velocityZ *= this.velocityMultiplier;
        }
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    public int getBrightness(float partialTick) {
        return 240;
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider SpriteProvider;

        public Factory(SpriteProvider SpriteProvider) {
            this.SpriteProvider = SpriteProvider;
        }

        public Particle createParticle(DefaultParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FairyRingSporeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.SpriteProvider);
        }
    }
}