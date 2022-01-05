package mod.schnappdragon.habitat.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityTypeTags.class)
public class EntityTypeTagsInvoker {
    @Invoker("register")
    public static Tag.Identified<EntityType<?>> invokeRegister(String id) {
        throw new AssertionError();
    }
}
