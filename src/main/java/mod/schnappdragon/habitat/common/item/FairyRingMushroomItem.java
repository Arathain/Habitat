package mod.schnappdragon.habitat.common.item;

import mod.schnappdragon.habitat.common.entity.monster.PookaEntity;
import mod.schnappdragon.habitat.common.registry.HabitatParticleTypes;
import mod.schnappdragon.habitat.common.registry.HabitatSoundEvents;
import mod.schnappdragon.habitat.core.HabitatConfig;
import mod.schnappdragon.habitat.mixin.MooshroomEntityAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.event.GameEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class FairyRingMushroomItem extends BlockItem {
    public FairyRingMushroomItem(Block blockIn, Settings builder) {
        super(blockIn, builder);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof MooshroomEntity mooshroom && ((MooshroomEntity) entity).getMooshroomType() == MooshroomEntity.Type.BROWN) {
            if (!user.world.isClient) {
                if (((MooshroomEntityAccessor) mooshroom).getStewEffect() == null) {
                    Pair<StatusEffect, Integer> effect = getStewEffect();

                    ((MooshroomEntityAccessor) mooshroom).setStewEffect(effect.getLeft());
                    ((MooshroomEntityAccessor) mooshroom).setStewEffectCooldown(effect.getRight());

                    if (!user.getAbilities().creativeMode)
                        stack.decrement(1);

                    for (int i = 0; i < 4; ++i) {
                        ((ServerWorld) user.world).spawnParticles(ParticleTypes.EFFECT, mooshroom.getParticleX(0.5D), mooshroom.getBodyY(0.5D), mooshroom.getParticleZ(0.5D), 0, 0.0D, mooshroom.getRandom().nextDouble(), 0.0D, 0.2D);
                        ((ServerWorld) user.world).spawnParticles(HabitatParticleTypes.FAIRY_RING_SPORE, mooshroom.getX() + mooshroom.getRandom().nextDouble() / 2.0D, mooshroom.getBodyY(0.5D), mooshroom.getZ() + mooshroom.getRandom().nextDouble() / 2.0D, 0, mooshroom.getRandom().nextGaussian(), 0.0D, mooshroom.getRandom().nextGaussian(), 0.01D);
                    }

                    user.world.playSound(null, mooshroom.getBlockPos(), SoundEvents.ENTITY_MOOSHROOM_EAT, mooshroom.getSoundCategory(), 2.0F, 1.0F);
                    return ActionResult.SUCCESS;
                }

                for (int i = 0; i < 2; ++i)
                    ((ServerWorld) user.world).spawnParticles(ParticleTypes.SMOKE, mooshroom.getX() + mooshroom.getRandom().nextDouble() / 2.0D, mooshroom.getBodyY(0.5D), mooshroom.getZ() + mooshroom.getRandom().nextDouble() / 2.0D, 0, 0.0D, mooshroom.getRandom().nextDouble(), 0.0D, 0.2D);
            }

            return ActionResult.success(user.world.isClient);
        } else if (entity.getType() == EntityType.RABBIT && entity.isAlive()) {
            if (!user.world.isClient) {
                RabbitEntity rabbit = (RabbitEntity) entity;
                user.world.emitGameEvent(GameEvent.MOB_INTERACT, rabbit.getCameraBlockPos());
                rabbit.playSound(HabitatSoundEvents.RABBIT_CONVERTED_TO_POOKA, 1.0F, rabbit.isBaby() ? (rabbit.getRandom().nextFloat() - rabbit.getRandom().nextFloat()) * 0.2F + 1.5F : (rabbit.getRandom().nextFloat() - rabbit.getRandom().nextFloat()) * 0.2F + 1.0F);
                rabbit.discard();
                PookaEntity pooka = PookaEntity.convertRabbitToPooka(rabbit);
                user.world.spawnEntity(pooka);
                user.world.sendEntityStatus(pooka, (byte) 15);

                if (!user.getAbilities().creativeMode)
                    stack.decrement(1);
            }

            return ActionResult.success(user.world.isClient);
        }

        return super.useOnEntity(stack, user, entity, hand);
    }

    public static Pair<StatusEffect, Integer> getStewEffect() {
        List<String> stewEffectPairs = Arrays.asList(StringUtils.deleteWhitespace(HabitatConfig.COMMON.suspiciousStewEffects).split(","));
        String[] pair = stewEffectPairs.get((int) (Math.random() * stewEffectPairs.size())).split(":");
        StatusEffect effect = StatusEffect.byRawId(Integer.parseInt(pair[0]));

        return Pair.of(effect != null ? effect : StatusEffects.GLOWING, Integer.parseInt(pair[1]) * 20);
    }
}