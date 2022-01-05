package mod.schnappdragon.habitat.core.registry;

import mod.schnappdragon.habitat.common.effect.HabitatEffect;
import mod.schnappdragon.habitat.Habitat;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HabitatEffects {
    public static final DeferredRegister<StatusEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Habitat.MOD_ID);

    public static final RegistryObject<StatusEffect> BLAST_ENDURANCE = EFFECTS.register("blast_endurance",
            () -> new HabitatEffect(StatusEffectCategory.BENEFICIAL, 8440968));

    public static final RegistryObject<StatusEffect> PRICKLING = EFFECTS.register("prickling",
            () -> new HabitatEffect(StatusEffectCategory.BENEFICIAL, 5794588));
}