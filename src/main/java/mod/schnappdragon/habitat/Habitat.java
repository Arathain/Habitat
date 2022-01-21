package mod.schnappdragon.habitat;

import mod.schnappdragon.habitat.common.registry.HabitatCriterionTriggers;
import mod.schnappdragon.habitat.common.registry.HabitatEntities;
import mod.schnappdragon.habitat.common.registry.HabitatSoundEvents;
import mod.schnappdragon.habitat.core.registry.HabitatRecipeSerializers;
import net.fabricmc.api.ModInitializer;

public class Habitat implements ModInitializer {
	public static final String MOD_ID = "habitat";

	@Override
	public void onInitialize() {
		HabitatSoundEvents.init();
		HabitatEntities.init();
		HabitatCriterionTriggers.registerCriteriaTriggers();
		HabitatRecipeSerializers.init();
	}
}
