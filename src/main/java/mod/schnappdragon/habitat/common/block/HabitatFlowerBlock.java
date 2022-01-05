package mod.schnappdragon.habitat.common.block;

import net.minecraft.world.effect.StatusEffect;
import net.minecraft.world.effect.StatusEffects;
import net.minecraft.world.level.block.FlowerBlock;

import java.util.function.Supplier;

public class HabitatFlowerBlock extends FlowerBlock {
    private final Supplier<StatusEffect> stewEffect;
    private final int stewEffectDuration;

    public HabitatFlowerBlock(Supplier<StatusEffect> stewEffect, int stewEffectDuration, Properties properties) {
        super(StatusEffects.REGENERATION, stewEffectDuration, properties);
        this.stewEffect = stewEffect;
        this.stewEffectDuration = stewEffectDuration;
    }

    @Override
    public StatusEffect getSuspiciousStewEffect() {
        return this.stewEffect.get();
    }

    @Override
    public int getEffectDuration() {
        return this.stewEffectDuration * (this.stewEffect.get().isInstantenous() ? 1 : 20);
    }
}
