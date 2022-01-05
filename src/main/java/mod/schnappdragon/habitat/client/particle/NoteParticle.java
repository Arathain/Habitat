package mod.schnappdragon.habitat.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

public class NoteParticle<T extends ColorableParticleEffect> extends SpriteBillboardParticle {
    private NoteParticle(ClientWorld world, double x, double y, double z, T particle, SpriteProvider SpriteProvider) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        //this.speedUpWhenYMotionIsBlocked = true;
        this.collidesWithWorld = true;
        this.setSprite(SpriteProvider);
        this.velocityMultiplier = 0.66F;

        this.velocityX *= 0.01F;
        this.velocityY *= 0.01F;
        this.velocityZ *= 0.01F;
        this.velocityY += 0.2D;

        float f1 = 0.98F + this.random.nextFloat() * 0.02F;
        this.colorRed = particle.getColor().getX() * f1;
        this.colorGreen = particle.getColor().getY() * f1;
        this.colorBlue = particle.getColor().getZ() * f1;

        this.scale *= 0.8F;
        this.maxAge = 5;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    public float getSize(float scale) {
        return this.scale * MathHelper.clamp(((float) this.age + scale) / (float) this.maxAge * 32.0F, 0.0F, 1.0F);
    }
    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<ColorableParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(ColorableParticleEffect particleEffect, ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new NoteParticle(clientWorld, x, y, z, particleEffect, spriteProvider);
        }
    }
}