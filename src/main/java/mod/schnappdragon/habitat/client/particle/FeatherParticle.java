package mod.schnappdragon.habitat.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class FeatherParticle<T extends ColorableParticleEffect> extends SpriteBillboardParticle {
    protected float rollFactor;
    protected int rollLimit;
    protected int rollTicks;

    private FeatherParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, T particle, SpriteProvider SpriteProvider) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.setSprite(SpriteProvider);
        this.collidesWithWorld = true;

        this.maxAge = 360 + this.random.nextInt(40);

        float f = 0.09F + this.random.nextFloat() * 0.01F;
        this.scale(f / 0.3F);
        this.scale = f;

        float f1 = 0.98F + this.random.nextFloat() * 0.02F;
        this.colorRed = particle.getColor().getX() * f1;
        this.colorGreen = particle.getColor().getY() * f1;
        this.colorBlue = particle.getColor().getZ() * f1;

        this.gravityStrength = 0.01F + random.nextFloat() * 0.01F;

        this.velocityX = motionX;
        this.velocityY = motionY;
        this.velocityZ = motionZ;

        this.rollFactor = (random.nextBoolean() ? -1 : 1) * (0.8F + random.nextFloat() * 4.0F) * (float) Math.PI / 180.0F;
        this.rollLimit = (6 + random.nextInt(6)) * 20;
        this.rollTicks = this.rollLimit;
        this.angle = random.nextFloat() * (2.0F * (float) Math.PI);
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.prevAngle = this.angle;

        if (this.age++ >= this.maxAge || this.world.getFluidState(new BlockPos(this.x, this.y, this.z)).isIn(FluidTags.LAVA))
            this.markDead();
        else if (!this.world.getFluidState(new BlockPos(this.x, this.y, this.z)).isEmpty()) {
            this.velocityX *= 0.99D;
            this.velocityY = 0.0D;
            this.velocityZ *= 0.99D;
            this.angle = 0.0F;
            this.age++;
        } else {
            this.velocityY -= 0.04 * this.gravityStrength;

            if (!this.onGround) {
                if (this.rollTicks > 0) this.rollTicks--;
                this.angle += (float) this.rollTicks / this.rollLimit * this.rollFactor;
            }
            else
                this.age++;
        }

        this.move(velocityX, velocityY, velocityZ);
    }

    public static class Factory implements ParticleFactory<ColorableParticleEffect> {
        private final SpriteProvider SpriteProvider;

        public Factory(SpriteProvider SpriteProvider) {
            this.SpriteProvider = SpriteProvider;
        }


        @Nullable
        @Override
        public Particle createParticle(ColorableParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new FeatherParticle<>(world, x, y, z, velocityX, velocityY, velocityZ, parameters, SpriteProvider);
        }
    }
}
