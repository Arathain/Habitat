package mod.schnappdragon.habitat.core.api.conditions;

import com.google.gson.JsonObject;
import mod.schnappdragon.habitat.Habitat;
import net.minecraft.resources.Identifier;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public final class QuarkFlagRecipeCondition implements ICondition {
    private final Identifier location;
    private final String flag;

    public QuarkFlagRecipeCondition(Identifier location, String flag) {
        this.location = location;
        this.flag = flag;
    }

    @Override
    public Identifier getID() {
        return this.location;
    }

    @Override
    public boolean test() {
        return CompatHelper.checkQuarkFlag(this.flag);
    }

    public static class Serializer implements IConditionSerializer<QuarkFlagRecipeCondition> {
        private final Identifier location;

        public Serializer() {
            this.location = new Identifier(Habitat.MOD_ID, "quark_flag");
        }

        @Override
        public void write(JsonObject json, QuarkFlagRecipeCondition value) {
            json.addProperty("flag", value.flag);
        }

        @Override
        public QuarkFlagRecipeCondition read(JsonObject json) {
            return new QuarkFlagRecipeCondition(this.location, json.getAsJsonPrimitive("flag").getAsString());
        }

        @Override
        public Identifier getID() {
            return this.location;
        }
    }
}
