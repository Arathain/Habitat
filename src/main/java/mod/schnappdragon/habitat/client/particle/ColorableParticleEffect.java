package mod.schnappdragon.habitat.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.Vec3f;

import java.util.Locale;

public class ColorableParticleEffect implements ParticleEffect {
    public static final ParticleEffect.Factory<ColorableParticleEffect> DESERIALIZER = new ParticleEffect.Factory<>() {
        public ColorableParticleEffect read(ParticleType<ColorableParticleEffect> type, StringReader reader) throws CommandSyntaxException {
            Vec3f vec3f = ColorableParticleEffect.readVec3f(reader);
            return new ColorableParticleEffect(type, vec3f);
        }

        public ColorableParticleEffect read(ParticleType<ColorableParticleEffect> type, PacketByteBuf buffer) {
            return new ColorableParticleEffect(type, ColorableParticleEffect.readVec3f(buffer));
        }
    };
    protected final ParticleType<ColorableParticleEffect> type;
    protected final Vec3f color;

    public static Codec<ColorableParticleEffect> codec(ParticleType<ColorableParticleEffect> type) {
        return RecordCodecBuilder.create((builder) -> builder.group(Vec3f.CODEC.fieldOf("color").forGetter((codec) -> codec.color)).apply(builder, (color) -> new ColorableParticleEffect(type, color)));
    }

    public ColorableParticleEffect(ParticleType<ColorableParticleEffect> type, Vec3f color) {
        this.type = type;
        this.color = color;
    }

    public static Vec3f readVec3f(StringReader reader) throws CommandSyntaxException {
        reader.expect(' ');
        float f = reader.readFloat();
        reader.expect(' ');
        float f1 = reader.readFloat();
        reader.expect(' ');
        float f2 = reader.readFloat();
        return new Vec3f(f, f1, f2);
    }

    public static Vec3f readVec3f(PacketByteBuf buffer) {
        return new Vec3f(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
    }

    public void write(PacketByteBuf buffer) {
        buffer.writeFloat(this.color.getX());
        buffer.writeFloat(this.color.getY());
        buffer.writeFloat(this.color.getZ());
    }

    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", this.getType().getCodec(), this.color.getX(), this.color.getY(), this.color.getZ());
    }

    public Vec3f getColor() {
        return this.color;
    }

    public ParticleType<ColorableParticleEffect> getType() {
        return this.type;
    }
}
