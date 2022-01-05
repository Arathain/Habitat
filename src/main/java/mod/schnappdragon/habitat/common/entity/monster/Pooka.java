package mod.schnappdragon.habitat.common.entity.monster;

import mod.schnappdragon.habitat.HabitatConfig;
import mod.schnappdragon.habitat.common.registry.HabitatSoundEvents;
import mod.schnappdragon.habitat.common.registry.HabitatCriterionTriggers;
import mod.schnappdragon.habitat.core.HabitatConfig;
import mod.schnappdragon.habitat.core.registry.*;
import mod.schnappdragon.habitat.common.registry.HabitatEntityTypeTags;
import mod.schnappdragon.habitat.common.registry.HabitatItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.MathHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.World;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.StatusEffect;
import net.minecraft.world.effect.StatusEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.IForgeShearable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class Pooka extends RabbitEntity implements Monster, Shearable {
    private static final TrackedData<Integer> DATA_STATE_ID = DataTracker.registerData(Pooka.class, TrackedDataHandlerRegistry.INTEGER);
    private int aidId;
    private int aidDuration;
    private int ailmentId;
    private int ailmentDuration;
    private int forgiveTicks;
    private int aidTicks;

    public Pooka(EntityType<? extends Pooka> entityType, World world) {
        super(entityType, world);
        this.experiencePoints= 3;
    }

    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new Pooka.PookaPanicGoal(2.2D));
        this.targetSelector.add(1, (new Pooka.PookaHurtByTargetGoal()).setGroupRevenge());
        this.targetSelector.add(2, new Pooka.PookaNearestAttackableTargetGoal<>(PlayerEntity.class));
        this.targetSelector.add(2, new Pooka.PookaNearestAttackableTargetGoal<>(MobEntity.class, mob -> mob.getType.isIn(HabitatEntityTypeTags.POOKA_ATTACK_TARGETS)));
        this.goalSelector.add(2, new AnimalMateGoal(this, 0.8D));
        this.goalSelector.add(3, new Pooka.PookaTemptGoal(1.25D, Ingredient.fromTag(HabitatItemTags.POOKA_FOOD), false));
        this.goalSelector.add(4, new Pooka.PookaMeleeAttackGoal());
        this.goalSelector.add(4, new Pooka.PookaAvoidEntityGoal<>(MobEntity.class, 10.0F, 2.2D, 2.2D, mob -> mob.getType().is(HabitatEntityTypeTags.PACIFIED_POOKA_SCARED_BY)));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.6D));
        this.goalSelector.add(11, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F));
    }

    public static DefaultAttributeContainer.Builder registerPookaAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 3.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3F).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0D).add(EntityAttributes.GENERIC_ARMOR, 8.0D);
    }


    @Override
    protected int getXpToDrop(PlayerEntity player) {
        return this.experiencePoints;
    }

    /*
     * Data Methods
     */

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DATA_STATE_ID, 0);
    }

    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("AidId", this.aidId);
        compound.putInt("AidDuration", this.aidDuration);
        compound.putInt("AilmentId", this.ailmentId);
        compound.putInt("AilmentDuration", this.ailmentDuration);
        compound.putInt("ForgiveTicks", this.forgiveTicks);
        compound.putInt("AidTicks", this.aidTicks);
        compound.putInt("State", this.getStateId());
    }

    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setAidAndAilment(
                compound.getInt("AidId"),
                compound.getInt("AidDuration"),
                compound.getInt("AilmentId"),
                compound.getInt("AilmentDuration")
        );
        this.forgiveTicks = compound.getInt("ForgiveTicks");
        this.aidTicks = compound.getInt("AidTicks");
        this.setStateId(compound.getInt("State"));
    }

    private void setAidAndAilment(int aidI, int aidD, int ailI, int ailD) {
        this.aidId = aidI;
        this.aidDuration = aidD;
        this.ailmentId = ailI;
        this.ailmentDuration = ailD;
    }

    private void setState(Pooka.State state) {
        this.setStateId(state.ordinal());
    }

    private void setStateId(int state) {
        this.dataTracker.set(DATA_STATE_ID, state);
    }

    public Pooka.State getState() {
        return Pooka.State.getById(this.getStateId());
    }

    public int getStateId() {
        return MathHelper.clamp(this.dataTracker.get(DATA_STATE_ID), 0, 2);
    }

    public boolean isHostile() {
        return this.getState().equals(Pooka.State.HOSTILE);
    }

    public boolean isPacified() {
        return this.getState().equals(Pooka.State.PACIFIED);
    }

    private void setForgiveTimer() {
        this.forgiveTicks = 12000;
    }

    private void setAidTimer() {
        this.aidTicks = (int) ((20.0F + this.random.nextFloat() * 4.0F) * (float) HabitatConfig.COMMON.pookaAidCooldown.get());
    }

    private void resetAidTimer() {
        this.aidTicks = 0;
    }

    /*
     * Update AI Tasks
     */

    @Override
    public void jump() {
        if (!this.world.isClient)
            this.world.sendEntityStatus(this, (byte) 14);

        super.jump();
    }

    @Override
    public void mobTick() {
        if (this.forgiveTicks > 0)
            forgiveTicks--;

        if (this.aidTicks > 0)
            aidTicks--;

        if (this.onGround && this.isHostile() && this.jumpDelayTicks == 0) {
            LivingEntity livingentity = this.getTarget();
            if (livingentity != null && this.distanceToSqr(livingentity) < 16.0D) {
                this.facePoint(livingentity.getX(), livingentity.getZ());
                this.moveControl.setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), this.moveControl.getSpeedModifier());
                this.startJumping();
                this.wasOnGround = true;
            }
        }

        super.mobTick();
    }

    private void facePoint(double x, double z) {
        this.setYRot((float) (MathHelper.atan2(z - this.getZ(), x - this.getX()) * (double) (180F / (float) Math.PI)) - 90.0F);
    }

    /*
     * Leash Methods
     */

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return !this.isHostile();
    }

    @Override
    protected void updateLeash() {
        super.updateLeash();

        if (this.isHostile())
            this.detachLeash(true, true);
    }

    /*
     * Conversion Methods
     */

    @Override
    public boolean isShearable() {
        return this.isAlive() && !this.isHostile() && !this.isBaby();
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nullable Player player, @Nonnull ItemStack item, Level world, BlockPos pos, int fortune) {
        this.level.gameEvent(player, GameEvent.SHEAR, pos);
        world.playSound(null, this, HabitatSoundEvents.POOKA_SHEAR.get(), SoundSource.HOSTILE, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        if (!this.level.isClientSide()) {
            ((ServerLevel) this.level).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5D), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            this.discard();
            world.addFreshEntity(convertPookaToRabbit(this));
        }
        return Collections.singletonList(new ItemStack(HabitatItems.FAIRY_RING_MUSHROOM.get()));
    }

    public static RabbitEntity convertPookaToRabbit(Pooka pooka) {
        RabbitEntity rabbit = EntityType.RABBIT.create(pooka.level);
        rabbit.moveTo(pooka.getX(), pooka.getY(), pooka.getZ(), pooka.getYRot(), pooka.getXRot());
        rabbit.setHealth(pooka.getHealth());
        rabbit.yBodyRot = pooka.yBodyRot;
        if (pooka.hasCustomName()) {
            rabbit.setCustomName(pooka.getCustomName());
            rabbit.setCustomNameVisible(pooka.isCustomNameVisible());
        }

        if (pooka.isPersistenceRequired())
            rabbit.setPersistenceRequired();

        rabbit.setRabbitType(pooka.getRabbitType());
        rabbit.setBaby(pooka.isBaby());
        rabbit.setInvulnerable(pooka.isInvulnerable());
        return rabbit;
    }

    public static Pooka convertRabbitToPooka(Rabbit rabbit) {
        Pooka pooka = HabitatEntityTypes.POOKA.get().create(rabbit.level);
        pooka.moveTo(rabbit.getX(), rabbit.getY(), rabbit.getZ(), rabbit.getYRot(), rabbit.getXRot());
        pooka.setHealth(rabbit.getHealth());
        pooka.yBodyRot = rabbit.yBodyRot;
        if (rabbit.hasCustomName()) {
            pooka.setCustomName(rabbit.getCustomName());
            pooka.setCustomNameVisible(rabbit.isCustomNameVisible());
        }

        pooka.setPersistenceRequired();
        pooka.setForgiveTimer();

        Pair<Integer, Integer> aid = pooka.getRandomAid();
        Pair<Integer, Integer> ailment = pooka.getRandomAilment();
        pooka.setAidAndAilment(aid.getLeft(), aid.getRight(), ailment.getLeft(), ailment.getRight());

        pooka.setRabbitType(rabbit.getRabbitType());
        pooka.setBaby(rabbit.isBaby());
        pooka.setInvulnerable(rabbit.isInvulnerable());
        return pooka;
    }

    /*
     * Sound Methods
     */

    public SoundSource getSoundSource() {
        return this.isHostile() ? SoundSource.HOSTILE : SoundSource.NEUTRAL;
    }

    protected SoundEvent getJumpSound() {
        return HabitatSoundEvents.POOKA_JUMP;
    }

    protected SoundEvent getAmbientSound() {
        return HabitatSoundEvents.POOKA_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return HabitatSoundEvents.POOKA_HURT;
    }

    protected SoundEvent getDeathSound() {
        return HabitatSoundEvents.POOKA_DEATH;
    }

    /*
     * Taming Methods
     */

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (this.isFood(stack)) {
            if (this.isHostile()) {
                if (!this.world.isClient) {
                    this.setPersistent();
                    this.usePlayerItem(player, hand, stack);

                    if (this.forgiveTicks == 0) {
                        int roll = random.nextInt(5);

                        if ((this.isBaby() && roll > 0 || roll == 0) && this.isAlone()) {
                            this.setState(State.PACIFIED);
                            this.playSound(HabitatSoundEvents.POOKA_PACIFY, 1.0F, 1.0F);
                            HabitatCriterionTriggers.PACIFY_POOKA.trigger((ServerPlayer) player);
                            this.navigation.stop();
                            this.setTarget(null);
                            this.setLastHurtByMob(null);
                            this.world.sendEntityStatus(this, (byte) 18);
                        } else
                            this.world.sendEntityStatus(this, (byte) 12);
                    } else {
                        this.forgiveTicks -= (double) this.forgiveTicks * 0.1D;
                        this.world.sendEntityStatus(this, (byte) 12);
                    }

                    this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
                }

                return ActionResult.success(this.world.isClient);
            }
            else if (this.getHealth() < this.getMaxHealth() && stack.isEdible()) {
                if (!this.world.isClient) {
                    this.usePlayerItem(player, hand, stack);
                    this.heal(stack.getItem().getFoodProperties().getNutrition());
                    this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
                }

                return ActionResult.success(this.world.isClient);
            }
        }

        ActionResult result = super.interactMob(player, hand);
        if (result.shouldSwingHand())
            this.setPersistent();

        return result;
    }


    protected void usePlayerItem(PlayerEntity player, Hand hand, ItemStack stack) {
        if (this.isFood(stack))
            this.playSound(HabitatSoundEvents.POOKA_EAT, 1.0F, 1.0F);

        super.usePlayerItem(player, hand, stack);
    }

    private boolean isAlone() {
        return this.level.getEntitiesOfClass(Pooka.class, this.getBoundingBox().inflate(16.0D, 10.0D, 16.0D), Pooka::isHostile).size() == 1;
    }

    public void unpacify() {
        this.resetLove();
        this.resetAidTimer();
        this.setForgiveTimer();
        this.setState(Pooka.State.HOSTILE);
        this.world.sendEntityStatus(this, (byte) 13);
    }

    /*
     * Breeding Methods
     */

    @Override
    public Pooka getBreedOffspring(ServerLevel serverWorld, AgeableMob entity) {
        Pooka pooka = HabitatEntityTypes.POOKA.get().create(serverWorld);
        Pooka.State state = Pooka.State.HOSTILE;
        int i = this.getRandomRabbitType(serverWorld);

        Pair<Integer, Integer> aid = this.getRandomAid();
        int aidI = aid.getLeft();
        int aidD = aid.getRight();

        Pair<Integer, Integer> ailment = this.getRandomAilment();
        int ailI = ailment.getLeft();
        int ailD = ailment.getRight();

        if (entity instanceof Pooka parent) {
            if (!this.isHostile() && !parent.isHostile()) state = Pooka.State.PASSIVE;

            if (this.random.nextInt(20) != 0) {
                if (this.random.nextBoolean())
                    i = parent.getRabbitType();
                else
                    i = this.getRabbitType();
            }

            if (this.random.nextInt(20) != 0) {
                if (this.random.nextBoolean()) {
                    aidI = parent.aidId;
                    aidD = parent.aidDuration;
                } else {
                    aidI = this.aidId;
                    aidD = this.aidDuration;
                }
            }

            if (this.random.nextInt(20) != 0) {
                if (this.random.nextBoolean()) {
                    ailI = parent.ailmentId;
                    ailD = parent.ailmentDuration;
                } else {
                    ailI = this.ailmentId;
                    ailD = this.ailmentDuration;
                }
            }
        }

        pooka.setState(state);
        pooka.setRabbitType(i);
        pooka.setAidAndAilment(aidI, aidD, ailI, ailD);
        pooka.setPersistenceRequired();
        return pooka;
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(HabitatItemTags.POOKA_FOOD);
    }

    public boolean canMate(Animal animal) {
        return animal instanceof Pooka pooka && !this.isHostile() && !pooka.isHostile() && super.canMate(animal);
    }

    /*
     * Spawn Methods
     */

    public static boolean checkPookaSpawnRules(EntityType<Pooka> pooka, LevelAccessor world, MobSpawnType reason, BlockPos pos, Random rand) {
        return world.getBlockState(pos.below()).is(BlockTags.RABBITS_SPAWNABLE_ON);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        Pair<Integer, Integer> aid = this.getRandomAid();
        Pair<Integer, Integer> ailment = this.getRandomAilment();
        int i = this.getRandomRabbitType(worldIn);

        if (spawnDataIn instanceof Rabbit.RabbitGroupData data)
            i = data.rabbitType;
        else
            spawnDataIn = new Rabbit.RabbitGroupData(i);

        this.setRabbitType(i);
        this.setState(Pooka.State.HOSTILE);
        this.setAidAndAilment(aid.getLeft(), aid.getRight(), ailment.getLeft(), ailment.getRight());
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private int getRandomRabbitType(LevelAccessor world) {
        Biome biome = world.getBiome(this.blockPosition());
        int i = this.random.nextInt(100);
        if (biome.getPrecipitation() == Biome.Precipitation.SNOW)
            return i < 80 ? 1 : 3;
        else if (biome.getBiomeCategory() == Biome.BiomeCategory.DESERT)
            return 4;
        else
            return i < 50 ? 0 : (i < 90 ? 5 : 2);
    }

    private Pair<Integer, Integer> getRandomAid() {
        return getEffect(HabitatConfig.COMMON.pookaPositiveEffects);
    }

    private Pair<Integer, Integer> getRandomAilment() {
        return getEffect(HabitatConfig.COMMON.pookaNegativeEffects);
    }

    private Pair<Integer, Integer> getEffect(ForgeConfigSpec.ConfigValue<String> config) {
        List<String> stewEffectPairs = Arrays.asList(StringUtils.deleteWhitespace(config.get()).split(","));
        String[] pair = stewEffectPairs.get(this.random.nextInt(stewEffectPairs.size())).split(":");

        return Pair.of(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]) * 20);
    }

    /*
     * Damage Methods
     */

    public boolean doHurtTarget(Entity entityIn) {
        if (entityIn.getType() == EntityType.RABBIT && entityIn.isAlive() && !entityIn.isInvulnerableTo(DamageSource.mobAttack(this))) {
            this.playSound(HabitatSoundEvents.POOKA_ATTACK.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.gameEvent(GameEvent.ENTITY_DAMAGED, this);

            Rabbit rabbit = (Rabbit) entityIn;
            rabbit.playSound(HabitatSoundEvents.RABBIT_CONVERTED_TO_POOKA.get(), 1.0F, rabbit.isBaby() ? (rabbit.getRandom().nextFloat() - rabbit.getRandom().nextFloat()) * 0.2F + 1.5F : (rabbit.getRandom().nextFloat() - rabbit.getRandom().nextFloat()) * 0.2F + 1.0F);
            rabbit.discard();
            this.level.addFreshEntity(convertRabbitToPooka(rabbit));

            for (int i = 0; i < 8; i++)
                ((ServerLevel) this.level).sendParticles(HabitatParticleTypes.FAIRY_RING_SPORE.get(), rabbit.getParticleX(0.5D), rabbit.getY(0.5D), rabbit.getParticleZ(0.5D), 0, rabbit.getRandom().nextGaussian(), 0.0D, rabbit.getRandom().nextGaussian(), 0.01D);
            return false;
        }

        boolean flag = entityIn.hurt(DamageSource.mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (flag) {
            this.doEnchantDamageEffects(this, entityIn);
            this.setLastHurtMob(entityIn);
            this.playSound(HabitatSoundEvents.POOKA_ATTACK.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

            if (!this.isBaby() && entityIn instanceof LivingEntity) {
                StatusEffect effect = StatusEffect.byId(ailmentId);

                if (effect != null)
                    ((LivingEntity) entityIn).addEffect(new StatusEffectInstance(effect, ailmentDuration * (this.level.getDifficulty() == Difficulty.HARD ? 2 : 1)));
            }
        }

        return flag;
    }

    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source))
            return false;
        else {
            if (!this.level.isClientSide && this.isAlive()) {
                StatusEffect effect = StatusEffect.byId(aidId);
                if (!this.isBaby() && effect != null)
                    this.addEffect(new StatusEffectInstance(effect, aidDuration));

                if (this.isPacified() && source.getEntity() instanceof Player && !source.isCreativePlayer())
                    this.unpacify();
            }

            return super.hurt(source, amount);
        }
    }

    /*
     * Particle Status Updates
     */

    public void handleEntityEvent(byte id) {
        switch (id) {
            case 12 -> spawnParticles(ParticleTypes.SMOKE, 7, true);
            case 13 -> spawnParticles(ParticleTypes.ANGRY_VILLAGER, 7, true);
            case 14 -> spawnParticles(HabitatParticleTypes.FAIRY_RING_SPORE.get(), 1, false);
            case 15 -> spawnParticles(HabitatParticleTypes.FAIRY_RING_SPORE.get(), 8, false);
            default -> super.handleEntityEvent(id);
        }
    }

    protected void spawnParticles(ParticleOptions particle, int number, boolean vanillaPresets) {
        for (int i = 0; i < number; i++) {
            double d0 = this.random.nextGaussian() * (vanillaPresets ? 0.02D : 0.01D);
            double d1 = vanillaPresets ? this.random.nextGaussian() * 0.02D : 0.0D;
            double d2 = this.random.nextGaussian() * (vanillaPresets ? 0.02D : 0.01D);
            double d3 = vanillaPresets ? 0.5D : 0.0D;
            this.level.addParticle(particle, this.getParticleX(0.5D + d3), this.getParticleY() + d3, this.getParticleZ(0.5D + d3), d0, d1, d2);
        }
    }

    /*
     * State
     */

    public enum State {
        HOSTILE,
        PACIFIED,
        PASSIVE;

        public static Pooka.State getById(int id) {
            return switch(id) {
                case 0 -> Pooka.State.HOSTILE;
                case 1 -> Pooka.State.PACIFIED;
                default -> Pooka.State.PASSIVE; //2
            };
        }
    }

    /*
     * AI Goals
     */

    class PookaPanicGoal extends PanicGoal {
        public PookaPanicGoal(double speedIn) {
            super(Pooka.this, speedIn);
        }

        @Override
        public void tick() {
            super.tick();
            Pooka.this.setSpeedModifier(this.speedModifier);
        }
    }

    class PookaTemptGoal extends TemptGoal {
        public PookaTemptGoal(double speed, Ingredient temptItem, boolean scaredByMovement) {
            super(Pooka.this, speed, temptItem, scaredByMovement);
        }

        @Override
        public boolean canUse() {
            return !Pooka.this.isHostile() && super.canUse();
        }

        @Override
        public void tick() {
            super.tick();
            StatusEffect aid = StatusEffect.byId(Pooka.this.aidId);

            if (!Pooka.this.isBaby() && Pooka.this.aidTicks == 0 && aid != null) {
                this.player.addEffect(new StatusEffectInstance(aid, Pooka.this.aidDuration * 2));
                Pooka.this.setAidTimer();
            }
        }
    }

    class PookaHurtByTargetGoal extends HurtByTargetGoal {
        private int timestamp;

        public PookaHurtByTargetGoal() {
            super(Pooka.this);
        }

        @Override
        public void start() {
            this.timestamp = this.mob.getLastHurtByMobTimestamp();
            super.start();
        }

        @Override
        public boolean canUse() {
            return this.mob.getLastHurtByMobTimestamp() != this.timestamp && this.mob.getLastHurtByMob() != null && this.canAttack(this.mob.getLastHurtByMob(), TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting());
        }

        @Override
        public boolean canContinueToUse() {
            return Pooka.this.isHostile() && super.canContinueToUse();
        }

        @Override
        protected void alertOther(Mob mob, LivingEntity target) {
            if (mob instanceof Pooka pooka && this.mob.hasLineOfSight(target)) {
                if (pooka.isHostile())
                    super.alertOther(mob, target);
                else if (pooka.isPacified() && target instanceof Player) {
                    pooka.unpacify();
                    super.alertOther(mob, target);
                }
            }
        }
    }

    class PookaNearestAttackableTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public PookaNearestAttackableTargetGoal(Class<T> targetClassIn, @Nullable Predicate<LivingEntity> targetPredicate) {
            super(Pooka.this, targetClassIn, 10, true, false, targetPredicate);
        }

        public PookaNearestAttackableTargetGoal(Class<T> targetClassIn) {
            super(Pooka.this, targetClassIn, true);
        }

        @Override
        public boolean canUse() {
            return Pooka.this.isHostile() && super.canUse();
        }
    }

    class PookaAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        public PookaAvoidEntityGoal(Class<T> entity, float range, double v1, double v2, Predicate<LivingEntity> predicate) {
            super(Pooka.this, entity, range, v1, v2, predicate);
        }

        @Override
        public boolean canUse() {
            return !Pooka.this.isHostile() && super.canUse();
        }
    }

    class PookaMeleeAttackGoal extends MeleeAttackGoal {
        public PookaMeleeAttackGoal() {
            super(Pooka.this, 1.4D, true);
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return 4.0F + attackTarget.getBbWidth();
        }

        @Override
        public boolean canUse() {
            return Pooka.this.isHostile() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return Pooka.this.isHostile() && super.canContinueToUse();
        }
    }
}