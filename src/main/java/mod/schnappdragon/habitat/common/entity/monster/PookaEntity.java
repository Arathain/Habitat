package mod.schnappdragon.habitat.common.entity.monster;

import mod.schnappdragon.habitat.common.entity.ai.goal.RabbitAvoidEntityGoal;
import mod.schnappdragon.habitat.common.registry.*;
import mod.schnappdragon.habitat.core.HabitatConfig;
import mod.schnappdragon.habitat.core.registry.HabitatItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PookaEntity extends RabbitEntity implements Monster, Shearable {
    private static final TrackedData<Integer> DATA_STATE_ID = DataTracker.registerData(PookaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private int aidId;
    private int aidDuration;
    private int ailmentId;
    private int ailmentDuration;
    private int forgiveTicks;
    private int aidTicks;

    public PookaEntity(EntityType<? extends PookaEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints= 3;
    }

    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new PookaEntity.PookaPanicGoal(2.2D));
        this.targetSelector.add(1, (new PookaEntity.PookaHurtByTargetGoal()).setGroupRevenge());
        this.targetSelector.add(2, new PookaEntity.PookaActiveTargetGoal<>(PlayerEntity.class));
        this.targetSelector.add(2, new PookaEntity.PookaActiveTargetGoal<>(MobEntity.class, mob -> mob.getType().isIn(HabitatEntityTypeTags.POOKA_ATTACK_TARGETS)));
        this.goalSelector.add(2, new AnimalMateGoal(this, 0.8D));
        this.goalSelector.add(3, new PookaEntity.PookaTemptGoal(1.25D, Ingredient.fromTag(HabitatItemTags.POOKA_FOOD), false));
        this.goalSelector.add(4, new PookaEntity.PookaMeleeAttackGoal());
        this.goalSelector.add(4, new PookaEntity.PookaAvoidEntityGoal<>(MobEntity.class, 10.0F, 2.2D, 2.2D, mob -> mob.getType().isIn(HabitatEntityTypeTags.PACIFIED_POOKA_SCARED_BY)));
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

    private void setState(PookaEntity.State state) {
        this.setStateId(state.ordinal());
    }

    private void setStateId(int state) {
        this.dataTracker.set(DATA_STATE_ID, state);
    }

    public PookaEntity.State getState() {
        return PookaEntity.State.getById(this.getStateId());
    }

    public int getStateId() {
        return MathHelper.clamp(this.dataTracker.get(DATA_STATE_ID), 0, 2);
    }

    public boolean isHostile() {
        return this.getState().equals(PookaEntity.State.HOSTILE);
    }

    public boolean isPacified() {
        return this.getState().equals(PookaEntity.State.PACIFIED);
    }

    private void setForgiveTimer() {
        this.forgiveTicks = 12000;
    }

    private void setAidTimer() {
        this.aidTicks = (int) ((20.0F + this.random.nextFloat() * 4.0F) * (float) HabitatConfig.COMMON.pookaAidCooldown);
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

        if (this.onGround && this.isHostile() && this.ticksUntilJump == 0) {
            LivingEntity livingentity = this.getTarget();
            if (livingentity != null && this.squaredDistanceTo(livingentity) < 16.0D) {
                this.facePoint(livingentity.getX(), livingentity.getZ());
                this.moveControl.moveTo(livingentity.getX(), livingentity.getY(), livingentity.getZ(), this.moveControl.getSpeed());
                this.startJump();
                this.lastOnGround = true;
            }
        }

        super.mobTick();
    }

    private void facePoint(double x, double z) {
        this.setYaw((float) (MathHelper.atan2(z - this.getZ(), x - this.getX()) * (double) (180F / (float) Math.PI)) - 90.0F);
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

    @Override
    public void sheared(SoundCategory category) {
        this.world.emitGameEvent(GameEvent.SHEAR, this.getBlockPos());
        world.playSound(null, this.getBlockPos(), HabitatSoundEvents.POOKA_SHEAR, SoundCategory.HOSTILE, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        if (!this.world.isClient()) {
            ((ServerWorld) this.world).spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getBodyY(0.5D), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            this.discard();
            world.spawnEntity(convertPookaToRabbit(this));
        }
        int i = 1 + this.random.nextInt(3);
        for(int j = 0; j < i; ++j) {
            ItemEntity itemEntity = this.dropItem((ItemConvertible) HabitatItems.FAIRY_RING_MUSHROOM, 1);
            if (itemEntity != null) {
                itemEntity.setVelocity(
                        itemEntity.getVelocity()
                                .add(
                                        (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F),
                                        (double)(this.random.nextFloat() * 0.05F),
                                        (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)
                                )
                );
            }
        }
    }

    public static RabbitEntity convertPookaToRabbit(PookaEntity pooka) {
        RabbitEntity rabbit = EntityType.RABBIT.create(pooka.world);
        rabbit.refreshPositionAndAngles(pooka.getX(), pooka.getY(), pooka.getZ(), pooka.getYaw(), pooka.getPitch());
        rabbit.setHealth(pooka.getHealth());
        rabbit.bodyYaw = pooka.bodyYaw;
        if (pooka.hasCustomName()) {
            rabbit.setCustomName(pooka.getCustomName());
            rabbit.setCustomNameVisible(pooka.isCustomNameVisible());
        }

        if (pooka.isPersistent())
            rabbit.setPersistent();

        rabbit.setRabbitType(pooka.getRabbitType());
        rabbit.setBaby(pooka.isBaby());
        rabbit.setInvulnerable(pooka.isInvulnerable());
        return rabbit;
    }

    public static PookaEntity convertRabbitToPooka(RabbitEntity rabbit) {
        PookaEntity pooka = HabitatEntities.POOKA.create(rabbit.world);
        pooka.refreshPositionAndAngles(rabbit.getX(), rabbit.getY(), rabbit.getZ(), rabbit.getYaw(), rabbit.getPitch());
        pooka.setHealth(rabbit.getHealth());
        pooka.bodyYaw = rabbit.bodyYaw;
        if (rabbit.hasCustomName()) {
            pooka.setCustomName(rabbit.getCustomName());
            pooka.setCustomNameVisible(rabbit.isCustomNameVisible());
        }

        pooka.setPersistent();
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

    public SoundCategory getSoundCategory() {
        return this.isHostile() ? SoundCategory.HOSTILE : SoundCategory.NEUTRAL;
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
                    this.eat(player, hand, stack);

                    if (this.forgiveTicks == 0) {
                        int roll = random.nextInt(5);

                        if ((this.isBaby() && roll > 0 || roll == 0) && this.isAlone()) {
                            this.setState(State.PACIFIED);
                            this.playSound(HabitatSoundEvents.POOKA_PACIFY, 1.0F, 1.0F);
                            HabitatCriterionTriggers.PACIFY_POOKA.trigger((ServerPlayerEntity) player);
                            this.navigation.stop();
                            this.setTarget(null);
                            this.setAttacker(null);
                            this.world.sendEntityStatus(this, (byte) 18);
                        } else
                            this.world.sendEntityStatus(this, (byte) 12);
                    } else {
                        this.forgiveTicks -= (double) this.forgiveTicks * 0.1D;
                        this.world.sendEntityStatus(this, (byte) 12);
                    }

                    this.emitGameEvent(GameEvent.MOB_INTERACT, this.getCameraBlockPos());
                }

                return ActionResult.success(this.world.isClient);
            }
            else if (this.getHealth() < this.getMaxHealth() && stack.isFood()) {
                if (!this.world.isClient) {
                    this.eat(player, hand, stack);
                    this.heal(stack.getItem().getFoodComponent().getSaturationModifier());
                    this.emitGameEvent(GameEvent.MOB_INTERACT, this.getCameraBlockPos());
                }

                return ActionResult.success(this.world.isClient);
            }
        }

        ActionResult result = super.interactMob(player, hand);
        if (result.shouldSwingHand())
            this.setPersistent();

        return result;
    }


    protected void eat(PlayerEntity player, Hand hand, ItemStack stack) {
        if (this.isFood(stack))
            this.playSound(HabitatSoundEvents.POOKA_EAT, 1.0F, 1.0F);

        super.eat(player, hand, stack);
    }

    private boolean isAlone() {
        return this.world.getEntitiesByClass(PookaEntity.class, this.getBoundingBox().expand(16.0D, 10.0D, 16.0D), PookaEntity::isHostile).size() == 1;
    }

    public void unpacify() {
        this.resetLoveTicks();
        this.resetAidTimer();
        this.setForgiveTimer();
        this.setState(PookaEntity.State.HOSTILE);
        this.world.sendEntityStatus(this, (byte) 13);
    }

    /*
     * Breeding Methods
     */

    @Override
    public PookaEntity createChild(ServerWorld serverWorld, PassiveEntity entity) {
        PookaEntity pooka = HabitatEntities.POOKA.create(serverWorld);
        PookaEntity.State state = PookaEntity.State.HOSTILE;
        int i = this.getRandomRabbitType(serverWorld);

        Pair<Integer, Integer> aid = this.getRandomAid();
        int aidI = aid.getLeft();
        int aidD = aid.getRight();

        Pair<Integer, Integer> ailment = this.getRandomAilment();
        int ailI = ailment.getLeft();
        int ailD = ailment.getRight();

        if (entity instanceof PookaEntity parent) {
            if (!this.isHostile() && !parent.isHostile()) state = PookaEntity.State.PASSIVE;

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
        pooka.setPersistent();
        return pooka;
    }

    public boolean isFood(ItemStack stack) {
        return stack.isIn(HabitatItemTags.POOKA_FOOD);
    }

    public boolean canBreedWith(AnimalEntity animal) {
        return animal instanceof PookaEntity pooka && !this.isHostile() && !pooka.isHostile() && super.canBreedWith(animal);
    }

    /*
     * Spawn Methods
     */

    public static boolean checkPookaSpawnRules(ServerWorldAccess world, BlockPos pos, SpawnReason reason) {
        return world.getBlockState(pos.down()).isIn(BlockTags.RABBITS_SPAWNABLE_ON);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        Pair<Integer, Integer> aid = this.getRandomAid();
        Pair<Integer, Integer> ailment = this.getRandomAilment();
        int i = this.getRandomRabbitType((ServerWorld) world);

        if (entityData instanceof RabbitEntity.RabbitData data)
            i = data.type;
        else
            entityData = new RabbitEntity.RabbitData(i);

        this.setRabbitType(i);
        this.setState(PookaEntity.State.HOSTILE);
        this.setAidAndAilment(aid.getLeft(), aid.getRight(), ailment.getLeft(), ailment.getRight());
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }



    private int getRandomRabbitType(ServerWorld world) {
        Biome biome = world.getBiome(this.getBlockPos());
        int i = this.random.nextInt(100);
        if (biome.getPrecipitation() == Biome.Precipitation.SNOW)
            return i < 80 ? 1 : 3;
        else if (biome.getCategory() == Biome.Category.DESERT)
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

    private Pair<Integer, Integer> getEffect(String config) {
        List<String> stewEffectPairs = Arrays.asList(StringUtils.deleteWhitespace(config).split(","));
        String[] pair = stewEffectPairs.get(this.random.nextInt(stewEffectPairs.size())).split(":");

        return Pair.of(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]) * 20);
    }

    /*
     * Damage Methods
     */

    public boolean tryAttack(Entity entityIn) {
        if (entityIn.getType() == EntityType.RABBIT && entityIn.isAlive() && !entityIn.isInvulnerableTo(DamageSource.mob(this))) {
            this.playSound(HabitatSoundEvents.POOKA_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.emitGameEvent(GameEvent.ENTITY_DAMAGED, this);

            RabbitEntity rabbit = (RabbitEntity) entityIn;
            rabbit.playSound(HabitatSoundEvents.RABBIT_CONVERTED_TO_POOKA, 1.0F, rabbit.isBaby() ? (rabbit.getRandom().nextFloat() - rabbit.getRandom().nextFloat()) * 0.2F + 1.5F : (rabbit.getRandom().nextFloat() - rabbit.getRandom().nextFloat()) * 0.2F + 1.0F);
            rabbit.discard();
            this.world.spawnEntity(convertRabbitToPooka(rabbit));

            for (int i = 0; i < 8; i++)
                ((ServerWorld) this.world).spawnParticles(HabitatParticleTypes.FAIRY_RING_SPORE, rabbit.getParticleX(0.5D), rabbit.getBodyY(0.5D), rabbit.getParticleZ(0.5D), 0, rabbit.getRandom().nextGaussian(), 0.0D, rabbit.getRandom().nextGaussian(), 0.01D);
            return false;
        }

        boolean flag = entityIn.damage(DamageSource.mob(this), (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        if (flag) {
            this.applyDamageEffects(this, entityIn);
            this.onAttacking(entityIn);
            this.playSound(HabitatSoundEvents.POOKA_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

            if (!this.isBaby() && entityIn instanceof LivingEntity) {
                StatusEffect effect = StatusEffect.byRawId(ailmentId);

                if (effect != null)
                    ((LivingEntity) entityIn).addStatusEffect(new StatusEffectInstance(effect, ailmentDuration * (this.world.getDifficulty() == Difficulty.HARD ? 2 : 1)));
            }
        }

        return flag;
    }

    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source))
            return false;
        else {
            if (!this.world.isClient && this.isAlive()) {
                StatusEffect effect = StatusEffect.byRawId(aidId);
                if (!this.isBaby() && effect != null)
                    this.addStatusEffect(new StatusEffectInstance(effect, aidDuration));

                if (this.isPacified() && source.getAttacker() instanceof PlayerEntity && !source.isSourceCreativePlayer())
                    this.unpacify();
            }

            return super.damage(source, amount);
        }
    }

    /*
     * Particle Status Updates
     */

    public void handleStatus(byte id) {
        switch (id) {
            case 12 -> spawnParticles(ParticleTypes.SMOKE, 7, true);
            case 13 -> spawnParticles(ParticleTypes.ANGRY_VILLAGER, 7, true);
            case 14 -> spawnParticles(HabitatParticleTypes.FAIRY_RING_SPORE, 1, false);
            case 15 -> spawnParticles(HabitatParticleTypes.FAIRY_RING_SPORE, 8, false);
            default -> super.handleStatus(id);
        }
    }

    protected void spawnParticles(ParticleEffect particle, int number, boolean vanillaPresets) {
        for (int i = 0; i < number; i++) {
            double d0 = this.random.nextGaussian() * (vanillaPresets ? 0.02D : 0.01D);
            double d1 = vanillaPresets ? this.random.nextGaussian() * 0.02D : 0.0D;
            double d2 = this.random.nextGaussian() * (vanillaPresets ? 0.02D : 0.01D);
            double d3 = vanillaPresets ? 0.5D : 0.0D;
            this.world.addParticle(particle, this.getParticleX(0.5D + d3), this.getRandomBodyY() + d3, this.getParticleZ(0.5D + d3), d0, d1, d2);
        }
    }

    /*
     * State
     */

    public enum State {
        HOSTILE,
        PACIFIED,
        PASSIVE;

        public static PookaEntity.State getById(int id) {
            return switch(id) {
                case 0 -> PookaEntity.State.HOSTILE;
                case 1 -> PookaEntity.State.PACIFIED;
                default -> PookaEntity.State.PASSIVE; //2
            };
        }
    }

    /*
     * AI Goals
     */

    class PookaPanicGoal extends EscapeDangerGoal {
        public PookaPanicGoal(double speedIn) {
            super(PookaEntity.this, speedIn);
        }

        @Override
        public void tick() {
            super.tick();
            PookaEntity.this.setSpeed(this.speed);
        }
    }

    class PookaTemptGoal extends TemptGoal {
        public PookaTemptGoal(double speed, Ingredient temptItem, boolean scaredByMovement) {
            super(PookaEntity.this, speed, temptItem, scaredByMovement);
        }

        @Override
        public boolean canStart() {
            return !PookaEntity.this.isHostile() && super.canStart();
        }

        @Override
        public void tick() {
            super.tick();
            StatusEffect aid = StatusEffect.byRawId(PookaEntity.this.aidId);

            if (!PookaEntity.this.isBaby() && PookaEntity.this.aidTicks == 0 && aid != null) {
                this.closestPlayer.addStatusEffect(new StatusEffectInstance(aid, PookaEntity.this.aidDuration * 2));
                PookaEntity.this.setAidTimer();
            }
        }
    }

    class PookaHurtByTargetGoal extends RevengeGoal {
        private int timestamp;

        public PookaHurtByTargetGoal() {
            super(PookaEntity.this);
        }

        @Override
        public void start() {
            this.timestamp = this.mob.getLastAttackedTime();
            super.start();
        }

        @Override
        public boolean canStart() {
            return this.mob.getLastAttackedTime() != this.timestamp && this.mob.getAttacker() != null && this.canTrack(this.mob.getAttacker(), TargetPredicate.createAttackable().ignoreVisibility().ignoreDistanceScalingFactor());
        }

        @Override
        public boolean shouldContinue() {
            return PookaEntity.this.isHostile() && super.shouldContinue();
        }


        @Override
        protected void setMobEntityTarget(MobEntity mob, LivingEntity target) {
            if (mob instanceof PookaEntity pooka && this.mob.canSee(target)) {
                if (pooka.isHostile())
                    super.setMobEntityTarget(mob, target);
                else if (pooka.isPacified() && target instanceof PlayerEntity) {
                    pooka.unpacify();
                    super.setMobEntityTarget(mob, target);
                }
            }
        }
    }

    class PookaActiveTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {
        public PookaActiveTargetGoal(Class<T> targetClassIn, @Nullable Predicate<LivingEntity> targetPredicate) {
            super(PookaEntity.this, targetClassIn, 10, true, false, targetPredicate);
        }

        public PookaActiveTargetGoal(Class<T> targetClassIn) {
            super(PookaEntity.this, targetClassIn, true);
        }

        @Override
        public boolean canStart() {
            return PookaEntity.this.isHostile() && super.canStart();
        }
    }

    class PookaAvoidEntityGoal<T extends LivingEntity> extends RabbitAvoidEntityGoal<T> {
        public PookaAvoidEntityGoal(Class<T> entity, float range, double v1, double v2, Predicate<LivingEntity> predicate) {
            super(PookaEntity.this, entity, range, v1, v2);
        }

        @Override
        public boolean canStart() {
            return !PookaEntity.this.isHostile() && super.canStart();
        }
    }

    class PookaMeleeAttackGoal extends MeleeAttackGoal {
        public PookaMeleeAttackGoal() {
            super(PookaEntity.this, 1.4D, true);
        }

        @Override
        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            return 4.0F + entity.getWidth();
        }

        @Override
        public boolean canStart() {
            return PookaEntity.this.isHostile() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return PookaEntity.this.isHostile() && super.shouldContinue();
        }
    }
}