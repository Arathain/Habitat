package mod.schnappdragon.habitat;

import mod.schnappdragon.habitat.common.registry.HabitatSoundEvents;
import net.fabricmc.api.ModInitializer;

public class Habitat implements ModInitializer {
	public static final String MOD_ID = "habitat";

	@Override
	public void onInitialize() {
		HabitatSoundEvents.init();
	}
}
