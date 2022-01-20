package mod.schnappdragon.habitat.common.registry;

import mod.schnappdragon.habitat.common.entity.projectile.ThrownKabloomFruitEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import org.jetbrains.annotations.Nullable;

public class HabitatDamageSources {
    public static final DamageSource SNOWGRAVE = (new EmptyDamageSource("habitat.snowgrave")).setUsesMagic();

    public static DamageSource causeKabloomDamage(ThrownKabloomFruitEntity kabloom, @Nullable Entity indirectEntityIn, boolean isExplosion) {
        ProjectileDamageSource source = new ProjectileDamageSource("habitat.kabloom", kabloom, indirectEntityIn);
        if (isExplosion) source.setExplosive();
        return source;
    }
    private static class EmptyDamageSource extends DamageSource {
        public EmptyDamageSource(String name) {
            super(name);
        }
    }
}