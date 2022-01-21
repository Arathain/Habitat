package mod.schnappdragon.habitat.mixin;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.passive.MooshroomEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MooshroomEntity.class)
public interface MooshroomEntityAccessor {
    @Accessor("stewEffect")
    StatusEffect getStewEffect();

    @Accessor("stewEffectDuration")
    public void setStewEffectCooldown(int stewEffectCooldown);
    @Accessor("stewEffect")
    public void setStewEffect(StatusEffect stewEffectCooldown);
}
