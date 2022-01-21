package mod.schnappdragon.habitat.common.block;

import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;

import java.util.function.Supplier;

public class HabitatFlowerBlock extends FlowerBlock {
    private final Supplier<StatusEffect> stewEffect;
    private final int stewEffectDuration;

    public HabitatFlowerBlock(Supplier<StatusEffect> stewEffect, int stewEffectDuration, Settings properties) {
        super(StatusEffects.REGENERATION, stewEffectDuration, properties);
        this.stewEffect = stewEffect;
        this.stewEffectDuration = stewEffectDuration;
    }
    @Override
    public StatusEffect getEffectInStew() {
        return this.stewEffect.get();
    }

    @Override
    public int getEffectInStewDuration() {
        return this.stewEffectDuration * (this.stewEffect.get().isInstant() ? 1 : 20);
    }

}
