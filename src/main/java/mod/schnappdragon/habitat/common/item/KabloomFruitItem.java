package mod.schnappdragon.habitat.common.item;

import mod.schnappdragon.habitat.common.entity.projectile.ThrownKabloomFruitEntity;
import mod.schnappdragon.habitat.common.registry.HabitatSoundEvents;
import mod.schnappdragon.habitat.core.registry.HabitatItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class KabloomFruitItem extends Item {
    public KabloomFruitItem(Item.Settings builder) {
        super(builder);
    }

    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getStackInHand(handIn);
        worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), HabitatSoundEvents.KABLOOM_FRUIT_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (playerIn.getRandom().nextFloat() * 0.4F + 0.8F));
        playerIn.getItemCooldownManager().set(this, 20);

        if (!playerIn.getAbilities().creativeMode) {
            itemstack.decrement(1);
        }

        if (!worldIn.isClient) {
            ThrownKabloomFruitEntity kabloomfruitentity = new ThrownKabloomFruitEntity(worldIn, playerIn);
            kabloomfruitentity.setItem(new ItemStack(HabitatItems.KABLOOM_FRUIT));
            kabloomfruitentity.setVelocity(playerIn, playerIn.getPitch(), playerIn.getYaw(), 0.0F, 0.5F, 0.9F);
            worldIn.spawnEntity(kabloomfruitentity);
        }

        return new TypedActionResult<>(ActionResult.SUCCESS, itemstack);
    }
}