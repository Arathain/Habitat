package mod.schnappdragon.habitat.common.entity.animal;

import mod.schnappdragon.habitat.common.registry.HabitatBlockTags;
import mod.schnappdragon.habitat.common.registry.HabitatCriterionTriggers;
import mod.schnappdragon.habitat.core.misc.HabitatDamageSources;
import mod.schnappdragon.habitat.client.particle.ColorableParticleEffect;
import mod.schnappdragon.habitat.common.registry.HabitatParticleTypes;
import mod.schnappdragon.habitat.common.registry.HabitatSoundEvents;
import mod.schnappdragon.habitat.common.registry.HabitatItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.MessageType;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class PasserineEntity extends AnimalEntity implements Flutterer {
    private static final TrackedData<Integer> DATA_VARIANT_ID = DataTracker.registerData(PasserineEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> DATA_SLEEPING = DataTracker.registerData(PasserineEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public float flap;
    public float flapSpeed;
    public float initialFlapSpeed;
    public float initialFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;

    private PasserineEntity.PreenGoal preenGoal;
    private int preenAnim;

    public PasserineEntity(EntityType<? extends PasserineEntity> passerine, World worldIn) {
        super(passerine, worldIn);
        this.moveControl = new PasserineMoveControl(10, false);
        this.lookControl = new PasserineLookControl();
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
    }

    protected void initGoals() {
        this.goalSelector.add(1, new FlyGoal(this, 1.2));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25D));
        this.goalSelector.add(2, new PasserineEntity.PasserineTemptGoal(1.0D, Ingredient.fromTag(HabitatItemTags.PASSERINE_FOOD), false));
        this.goalSelector.add(3, new PasserineEntity.FindCoverGoal(1.25D));
        this.goalSelector.add(4, new PasserineEntity.SleepGoal());
        this.preenGoal = new PasserineEntity.PreenGoal();
        this.goalSelector.add(5, this.preenGoal);
        this.goalSelector.add(6, new PasserineEntity.PasserineRandomFlyingGoal(1.0D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.goalSelector.add(8, new PasserineEntity.PasserineFollowMobGoal(1.0D, 3.0F, 7.0F));
    }

    public static DefaultAttributeContainer.Builder createPasserineAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0D).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.8F).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.16F);}

    public Vec3d getLeashOffset() {
        return new Vec3d(0.0D, 0.5F * this.getStandingEyeHeight(), this.getWidth() * 0.3F);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5F;
    }

    /*
     * Data Methods
     */

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DATA_VARIANT_ID, 0);
        this.dataTracker.startTracking(DATA_SLEEPING, false);
    }

    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("Sleeping", this.isAsleep());
    }

    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setSleeping(compound.getBoolean("Sleeping"));
    }

    public void setVariant(int id) {
        this.dataTracker.set(DATA_VARIANT_ID, id);
    }

    public int getVariant() {
        return MathHelper.clamp(this.dataTracker.get(DATA_VARIANT_ID), 0, 9);
    }

    public void setSleeping(boolean isSleeping) {
        this.dataTracker.set(DATA_SLEEPING, isSleeping);
    }

    public void sleep() {
        this.setSleeping(true);
    }

    public void wakeUp() {
        this.setSleeping(false);
    }

    public boolean isAsleep() {
        return this.dataTracker.get(DATA_SLEEPING);
    }

    /*
     * AI Methods
     */

    protected void mobTick() {
        this.preenAnim = this.preenGoal.getRemainingPreeningTicks();
        super.mobTick();
    }

    public void tickMovement() {
        super.tickMovement();
        this.calculateFlapping();

        if (this.isAsleep() || this.isImmobile()) {
            this.jumping = false;
            this.sidewaysSpeed = 0.0F;
            this.forwardSpeed = 0.0F;
        }

        if (this.world.isClient && this.preenAnim > 0)
            this.preenAnim--;
    }

    public void tick() {
        super.tick();

        if (!this.world.isClient()) {
            if (this.isAsleep() && (this.isInAir() || this.world.isDay() || this.inPowderSnow || this.isUnsafeAt(this.getBlockPos()) || !this.canPerch()))
                this.wakeUp();
            else if (this.isPreening() && this.inPowderSnow)
                this.preenGoal.stopPreening();
        }
    }

    public boolean isPreening() {
        return this.preenAnim > 0;
    }

    public int getRemainingPreeningTicks() {
        return this.preenAnim;
    }

    private boolean isNotBusy() {
        return !this.isAsleep() && !this.isPreening();
    }

    private boolean isUnsafeAt(BlockPos pos) {
        if (this.isGoldfish() || !this.world.isRaining() || !this.world.isSkyVisible(pos))
            return false;
        else if (this.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos).getY() > pos.getY())
            return false;
        else
            return this.world.getBiome(pos).getPrecipitation() != Biome.Precipitation.NONE;
    }

    private boolean isActive() {
        return this.world.isDay() && !this.world.isRaining();
    }

    private boolean canPerch() {
        return this.world.getBlockState(this.getLandingPos()).isIn(HabitatBlockTags.PASSERINE_PERCHABLE_ON);
    }

    /*
     * Flying Methods
     */

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation flyingpathnavigation = new BirdNavigation(this, world);
        flyingpathnavigation.setCanPathThroughDoors(true);
        flyingpathnavigation.setCanSwim(true);
        flyingpathnavigation.setCanEnterOpenDoors(false);
        return flyingpathnavigation;
    }



    private void calculateFlapping() {
        this.initialFlap = this.flap;
        this.initialFlapSpeed = this.flapSpeed;
        this.flapSpeed = (float) ((double) this.flapSpeed + (double) (!this.onGround && !this.hasVehicle() ? 4 : -1) * 0.3D);
        this.flapSpeed = MathHelper.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround && this.flapping < 1.0F)
            this.flapping = 1.0F;

        this.flapping = (float) ((double) this.flapping * 0.9D);
        Vec3d vec3d = this.getVelocity();
        if (!this.onGround && vec3d.y < 0.0D)
            this.setVelocity(vec3d.multiply(1.0D, 0.6D, 1.0D));

        this.flap += this.flapping * 2.0F;
    }

    protected boolean hasWings() {
        return this.speed > this.nextFlap;
    }

    protected void addFlapEffects() {
        this.playSound(HabitatSoundEvents.PASSERINE_FLAP, 0.1F, 1.0F);
        this.nextFlap = this.speed + this.flapSpeed / 2.0F;

        if (!this.isEasterEgg() && random.nextInt(30) == 0)
            this.world.sendEntityStatus(this, (byte) 11);
    }

    public boolean isInAir() {
        return !this.onGround;
    }

    /*
     * Interaction Methods
     */

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (stack.isIn(HabitatItemTags.PASSERINE_FOOD) && this.isNotBusy()) {
            if (!this.world.isClient()) {
                this.heal(1.0F);
                this.eat(player, hand, stack);
                this.world.sendEntityStatus(this, (byte) 13);
                this.emitGameEvent(GameEvent.MOB_INTERACT, this.getCameraBlockPos());
                HabitatCriterionTriggers.FEED_PASSERINE.trigger((ServerPlayerEntity) player);
                this.playSound(HabitatSoundEvents.PASSERINE_AMBIENT, 1.0F, this.getVoicePitch());
            }

            return ActionResult.success(this.world.isClient());
        }

        return super.interactMob(player, hand);
    }

    /*
     * Spawn Methods
     */

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        int i = this.getVariantByBiome(world);
        if (entityData instanceof PasserineEntity.PasserineGroupData data)
            i = data.variant;
        else
            entityData = new PasserineEntity.PasserineGroupData(i);

        this.setVariant(i);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    public int getVariantByBiome(ServerWorldAccess worldIn) {
        Biome biome = worldIn.getBiome(this.getBlockPos());
        Optional<RegistryKey<Biome>> optional = worldIn.getBiomeKey(this.getBlockPos());

        if (Objects.equals(optional, Optional.of(BiomeKeys.FLOWER_FOREST))) // All variants
            return this.random.nextInt(10);
        else if (biome.getCategory() == Biome.Category.JUNGLE) // Jungle variants
            return this.random.nextBoolean() ? 1 : 8;
        else if (biome.getTemperature() >= 1.0F) // Hot biomes
            return this.random.nextBoolean() ? 3 : 6;
        else if (biome.getTemperature() < 0.5F) // Cold biomes
            return new int[]{2, 3, 5, 7}[this.random.nextInt(4)];
        else if (biome.getTemperature() <= 0.6F) // Birch Forests, etc.
            return new int[]{0, 2, 3, 4, 5, 7}[this.random.nextInt(6)];
        else
            return new int[]{0, 3, 5, 6, 7, 9}[this.random.nextInt(6)];
    }

    public static boolean checkPasserineSpawnRules(EntityType<PasserineEntity> type, ServerWorldAccess worldIn, SpawnReason spawnReason, BlockPos pos, Random random) {
        return worldIn.getBlockState(pos.down()).isIn(HabitatBlockTags.PASSERINE_SPAWNABLE_ON) && isLightLevelValidForNaturalSpawn(worldIn, pos);
    }

    /*
     * Hurt Method
     */

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source))
            return false;
        else {
            if (!this.world.isClient()) {
                if (source.getSource() != null && !this.isEasterEgg())
                    this.world.sendEntityStatus(this, (byte) 12);

                if (this.isAsleep())
                    this.wakeUp();
                else if (this.isPreening())
                    this.preenGoal.stopPreening();
            }

            return super.damage(source, amount);
        }
    }

    /*
     * Sound Methods
     */

    public void playAmbientSound() {
        if (!this.isPreening() && this.isActive()) {
            super.playAmbientSound();

            if (!this.world.isClient())
                this.world.sendEntityStatus(this, (byte) 13);
        }
    }

    public SoundEvent getAmbientSound() {
        return HabitatSoundEvents.PASSERINE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return HabitatSoundEvents.PASSERINE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return HabitatSoundEvents.PASSERINE_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(HabitatSoundEvents.PASSERINE_STEP, 0.1F, 1.0F);
    }

    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    /*
     * Entity Event Methods
     */

    public void handleStatus(byte id) {
        switch (id) {
            case 11 -> spawnFeathers(this.getFeather(), 1);
            case 12 -> spawnFeathers(this.getFeather(), 2);
            case 13 -> this.world.addParticle(this.getNote(), this.getParticleX(0.5D), 0.6D + this.getY(), this.getParticleZ(0.5D), this.random.nextDouble(), 0.0D, 0.0D);
            case 14 -> this.preenAnim = 40;
            case 15 -> this.preenAnim = 0;
            default -> super.handleStatus(id);
        }
    }

    protected void spawnFeathers(ColorableParticleEffect feather, int number) {
        for (int i = 0; i < number; i++)
            this.world.addParticle(feather, this.getParticleX(0.5D), this.getBodyY(this.random.nextDouble() * 0.75D), this.getParticleZ(0.5D), this.random.nextGaussian() * 0.01D, 0.0D, this.random.nextGaussian() * 0.01D);
    }

    private ColorableParticleEffect getFeather() {
        int color = PasserineEntity.Variant.getFeatherColorByVariant(this.getVariant());
        return new ColorableParticleEffect(HabitatParticleTypes.FEATHER, new Vec3f(Vec3d.unpackRgb(color)));
    }

    private ColorableParticleEffect getNote() {
        int color = PasserineEntity.Variant.getNoteColorByVariant(this.getVariant());
        return new ColorableParticleEffect(HabitatParticleTypes.NOTE, new Vec3f(Vec3d.unpackRgb(color)));
    }

    /*
     * Easter Egg Methods
     */

    public boolean isEasterEgg() {
        return this.isBerdly() || this.isGoldfish() || this.isTurkey();
    }

    public boolean isBerdly() {
        return this.getName().getString().equalsIgnoreCase("berdly");
    }

    public boolean isGoldfish() {
        return this.getVariant() == 0 && this.getName().getString().equalsIgnoreCase("goldfish");
    }

    public boolean isTurkey() {
        return this.getName().getString().equalsIgnoreCase("turkey");
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);

        if (!this.world.isClient() && this.dead && this.isBerdly() && source == DamageSource.FREEZE && this.world.getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES)) {
            this.getDamageTracker().onDamage(HabitatDamageSources.SNOWGRAVE, this.getHealth(), this.getMaxHealth());
            ((ServerWorld) this.world).getServer().getPlayerManager().broadcast(this.getDamageTracker().getDeathMessage(), MessageType.SYSTEM, Util.NIL_UUID);
        }
    }

    /*
     * Breeding Methods
     */

    @Override
    public boolean canBreedWith(AnimalEntity animal) {
        return false;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }
    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    /*
     * Fall Damage Method
     */

    @Override
    public boolean handleFallDamage(float f, float f1, DamageSource source) {
        return false;
    }

    @Override
    protected void fall(double y, boolean onGround, BlockState state, BlockPos pps) {
    }

    /*
     * Data
     */

    public static class PasserineGroupData extends PassiveEntity.PassiveData {
        public final int variant;

        public PasserineGroupData(int variantId) {
            super(false);
            this.variant = variantId;
        }
    }

    /*
     * Variant
     */

    public enum Variant {
        AMERICAN_GOLDFINCH(16052497, 16775680),
        BALI_MYNA(16777215, 8703),
        BLUE_JAY(4815308, 24063),
        COMMON_SPARROW(7488818, 16730112),
        EASTERN_BLUEBIRD(5012138, 16744192),
        EURASIAN_BULLFINCH(796479, 16711726),
        FLAME_ROBIN(6248013, 16739840),
        NORTHERN_CARDINAL(13183262, 16714752),
        RED_THROATED_PARROTFINCH(4487992, 16713728),
        VIOLET_BACKED_STARLING(6435209, 9175295);

        private static final Variant[] VARIANTS = Variant.values();
        private final int featherColor;
        private final int noteColor;

        Variant(int featherColor, int noteColor) {
            this.featherColor = featherColor;
            this.noteColor = noteColor;
        }

        public static int getFeatherColorByVariant(int id) {
            return getVariantById(id).featherColor;
        }

        public static int getNoteColorByVariant(int id) {
            return getVariantById(id).noteColor;
        }

        private static Variant getVariantById(int id) {
            return VARIANTS[MathHelper.clamp(id, 0, 9)];
        }
    }

    /*
     * Controllers
     */

    public class PasserineMoveControl extends FlightMoveControl {
        public PasserineMoveControl(int maxTurns, boolean hoversInPlace) {
            super(PasserineEntity.this, maxTurns, hoversInPlace);
        }

        public void tick() {
            if (PasserineEntity.this.isNotBusy())
                super.tick();
        }
    }

    public class PasserineLookControl extends LookControl {
        public PasserineLookControl() {
            super(PasserineEntity.this);
        }

        public void tick() {
            if (PasserineEntity.this.isNotBusy())
                super.tick();
        }
    }

    /*
     * AI Goals
     */

    class PasserineTemptGoal extends TemptGoal {
        public PasserineTemptGoal(double speedModifier, Ingredient items, boolean canScare) {
            super(PasserineEntity.this, speedModifier, items, canScare);
        }

        public boolean canStart() {
            return !PasserineEntity.this.isAsleep() && super.canStart();
        }
    }

    class FindCoverGoal extends EscapeSunlightGoal {
        public FindCoverGoal(double speedModifier) {
            super(PasserineEntity.this, speedModifier);
        }

        public boolean canStart() {
            return PasserineEntity.this.isUnsafeAt(PasserineEntity.this.getBlockPos()) && !this.isTargetPosDry() && this.targetShadedPos();
        }

        private boolean isTargetPosDry() {
            return PasserineEntity.this.getNavigation().isFollowingPath() && !PasserineEntity.this.isUnsafeAt(getNavigation().getTargetPos());
        }
    }

    class SleepGoal extends Goal {
        private int countdown = PasserineEntity.this.random.nextInt(140);

        public SleepGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Goal.Control.JUMP));
        }

        public boolean canStart() {
            return PasserineEntity.this.sidewaysSpeed == 0.0F && PasserineEntity.this.upwardSpeed == 0.0F && PasserineEntity.this.forwardSpeed == 0.0F && (this.canSleep() || PasserineEntity.this.isAsleep());
        }

        public boolean canContinue() {
            return PasserineEntity.this.isAsleep() && this.canSleep();
        }

        private boolean canSleep() {
            if (this.countdown > 0) {
                this.countdown--;
                return false;
            } else {
                if (PasserineEntity.this.isInAir() || PasserineEntity.this.isPreening() || PasserineEntity.this.world.isDay() || PasserineEntity.this.inPowderSnow)
                    return false;
                else
                    return PasserineEntity.this.canPerch();
            }
        }

        public void start() {
            PasserineEntity.this.sleep();
            PasserineEntity.this.getNavigation().stop();
        }

        public void stop() {
            PasserineEntity.this.wakeUp();
            this.countdown = PasserineEntity.this.random.nextInt(140);
        }
    }

    class PreenGoal extends Goal {
        private int countdown = 2800 + PasserineEntity.this.random.nextInt(2800);
        private int preenAnim;

        public PreenGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Goal.Control.JUMP));
        }

        public boolean canStart() {
            if (this.countdown > 0) {
                this.countdown--;
                return false;
            } else
                return PasserineEntity.this.sidewaysSpeed == 0.0F && PasserineEntity.this.upwardSpeed == 0.0F && PasserineEntity.this.forwardSpeed == 0.0F && (this.canPreen() || PasserineEntity.this.isPreening());
        }

        public boolean canContinue() {
            return this.preenAnim > 0 && this.canPreen();
        }

        private boolean canPreen() {
            return !PasserineEntity.this.isTurkey() && !PasserineEntity.this.isInAir() && !PasserineEntity.this.isAsleep() && !PasserineEntity.this.inPowderSnow;
        }

        public void start() {
            this.preenAnim = 40;
            PasserineEntity.this.getNavigation().stop();
            PasserineEntity.this.world.sendEntityStatus(PasserineEntity.this, (byte) 14);
        }

        public void stop() {
            this.preenAnim = 0;
            this.countdown = 2800 + PasserineEntity.this.random.nextInt(2800);
            PasserineEntity.this.world.sendEntityStatus(PasserineEntity.this, (byte) 15);
        }

        public void tick() {
            if (this.preenAnim > 0) {
                this.preenAnim--;

                if (this.preenAnim == 20)
                    PasserineEntity.this.world.sendEntityStatus(PasserineEntity.this, (byte) 11);
            }
        }

        public int getRemainingPreeningTicks() {
            return this.preenAnim;
        }

        public void stopPreening() {
            this.preenAnim = 0;
        }
    }

    class PasserineRandomFlyingGoal extends WanderAroundFarGoal {
        public PasserineRandomFlyingGoal(double speedModifier) {
            super(PasserineEntity.this, speedModifier);
        }

        @Nullable
        protected Vec3d getWanderTarget() {
            if (PasserineEntity.this.isTouchingWater() || PasserineEntity.this.isUnsafeAt(PasserineEntity.this.getBlockPos()))
                return FuzzyTargeting.find(PasserineEntity.this, 15, 15);

            float probability = PasserineEntity.this.world.isDay() ? this.probability : 0.0F;
            Vec3d vec3d = PasserineEntity.this.getRandom().nextFloat() >= probability ? this.getPerchablePos() : null;
            vec3d = vec3d == null ? super.getWanderTarget() : vec3d;
            return vec3d != null && !PasserineEntity.this.isUnsafeAt(new BlockPos(vec3d)) ? vec3d : null;
        }

        @Nullable
        private Vec3d getPerchablePos() {
            BlockPos blockpos = PasserineEntity.this.getBlockPos();
            BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();
            BlockPos.Mutable blockpos$mutableblockpos1 = new BlockPos.Mutable();

            for (BlockPos blockpos1 : BlockPos.iterate(MathHelper.floor(PasserineEntity.this.getX() - 3.0D), MathHelper.floor(PasserineEntity.this.getY() - 6.0D), MathHelper.floor(PasserineEntity.this.getZ() - 3.0D), MathHelper.floor(PasserineEntity.this.getX() + 3.0D), MathHelper.floor(PasserineEntity.this.getY() + 6.0D), MathHelper.floor(PasserineEntity.this.getZ() + 3.0D))) {
                if (!blockpos.equals(blockpos1)) {
                    BlockState state = PasserineEntity.this.world.getBlockState(blockpos$mutableblockpos1.set(blockpos1, Direction.DOWN));
                    boolean flag = state.isIn(HabitatBlockTags.PASSERINE_PERCHABLE_ON);

                    if (flag && PasserineEntity.this.world.isAir(blockpos1) && PasserineEntity.this.world.isAir(blockpos$mutableblockpos.set(blockpos1, Direction.UP)))
                        return Vec3d.ofBottomCenter(blockpos1);
                }
            }

            return null;
        }
    }

    class PasserineFollowMobGoal extends FollowMobGoal {
        public PasserineFollowMobGoal(double speedModifier, float stopDistance, float areaSize) {
            super(PasserineEntity.this, speedModifier, stopDistance, areaSize);
        }

        public boolean canUse() {
            return PasserineEntity.this.isActive() && super.canStart();
        }
    }
}