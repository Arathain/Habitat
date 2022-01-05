package mod.schnappdragon.habitat.mixin;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockTags.class)
public interface ItemTagsInvoker {
    @Invoker("register")
    static Tag.Identified<Item> invokeRegister(String id) {
        throw new AssertionError();
    }
}
