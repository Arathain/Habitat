package mod.schnappdragon.habitat.common.entity.projectile;

import mod.schnappdragon.habitat.common.registry.HabitatEntities;
import mod.schnappdragon.habitat.common.registry.HabitatSoundEvents;
import mod.schnappdragon.habitat.common.registry.HabitatDamageSources;
import mod.schnappdragon.habitat.core.registry.HabitatItems;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class ThrownKabloomFruitEntity extends ThrownItemEntity {
    public ThrownKabloomFruitEntity(World worldIn, LivingEntity throwerIn) {
        super(HabitatEntities.KABLOOM_FRUIT, throwerIn, worldIn);
    }

    public ThrownKabloomFruitEntity(World worldIn, double x, double y, double z) {
        super(HabitatEntities.KABLOOM_FRUIT, x, y, z, worldIn);
    }

    public ThrownKabloomFruitEntity(EntityType<? extends ThrownKabloomFruitEntity> entityEntityType, World world) {
        super(HabitatEntities.KABLOOM_FRUIT, world);
    }


    @Override
    public void tick() {
        super.tick();
        if (this.isOnFire())
            explode(this.getPos());
    }

    @Override
    public void handleStatus(byte id) {
        if (id == 3) {
            this.world.playSound(this.getX(), this.getY(), this.getZ(), HabitatSoundEvents.KABLOOM_FRUIT_EXPLODE, SoundCategory.NEUTRAL, 1.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F), true);

            for (int i = 0; i < 8; ++i)
                this.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, this.getStack()), this.getX(), this.getY(), this.getZ(), (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.8D, (this.random.nextFloat() - 0.5D) * 0.08D);

            this.world.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1.0D, 0.0D, 0.0D);
        }
    }


    @Override
    protected void onCollision(HitResult result) {
        super.onCollision(result);
        explode(result.getPos());
    }

    @Override
    protected Item getDefaultItem() {
        return HabitatItems.KABLOOM_FRUIT;
    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(HabitatItems.KABLOOM_FRUIT);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected Vec3d adjustMovementForPiston(Vec3d pos) {
        explode(this.getPos());
        return Vec3d.ZERO;
    }

    private void explode(Vec3d vector3d) {
        if (!this.world.isClient) {
            this.emitGameEvent(GameEvent.EXPLODE, this.getOwner(), this.getBlockPos());

            for (Entity entity : this.world.getOtherEntities(null, this.getBoundingBox().expand(0.8D))) {
                boolean flag = false;

                for (int i = 0; i < 2; ++i) {
                    HitResult raytraceresult = this.world.raycast(new RaycastContext(vector3d, new Vec3d(entity.getX(), entity.getBodyY(0.5D * (double) i), entity.getZ()), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
                    if (raytraceresult.getType() == HitResult.Type.MISS) {
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    float dmg = 0;
                    if (!entity.isImmuneToExplosion()) {
                        double dx = entity.getX() - this.getX();
                        double dy = entity.getEyeY() - this.getY();
                        double dz = entity.getZ() - this.getZ();
                        double dres = MathHelper.sqrt((float) (dx * dx + dy * dy + dz * dz));
                        if (dres != 0.0D) {
                            dx = dx / dres;
                            dy = dy / dres;
                            dz = dz / dres;
                            double df = this.distanceTo(entity) > 1.0F ? 0.25D : 0.5D;
                            dmg = 4.0F + 4.0F * (float) df;
                            double dred = df;
                            if (entity instanceof LivingEntity livingEntity)
                                dred = ProtectionEnchantment.transformExplosionKnockback(livingEntity, df) * (1.0D - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));

                            Vec3d motionVec = entity.getVelocity().add(dx * dred, dy * dred, dz * dred);
                            entity.setVelocity(motionVec);
                            if (entity instanceof ServerPlayerEntity player) {
                                if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying))
                                    player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player.getId(), motionVec));
                            }
                        }
                    }

                    if (entity instanceof LivingEntity)
                        entity.damage(HabitatDamageSources.causeKabloomDamage(this, this.getOwner(), true), dmg);
                    else if (entity.isAttackable())
                        entity.damage(HabitatDamageSources.causeKabloomDamage(this, this.getOwner(), false), dmg);

                    if (this.isOnFire() && !entity.isFireImmune())
                        entity.setOnFireFor(1);
                }
            }

            if (this.world.getGameRules().get(GameRules.DO_ENTITY_DROPS).get()) {
                ItemEntity item = new ItemEntity(this.world, vector3d.getX() + this.random.nextDouble() * (this.random.nextBoolean() ? 1 : -1) * 0.5F, vector3d.getY() + this.random.nextDouble() / 2, vector3d.getZ() + this.random.nextDouble() * (this.random.nextBoolean() ? 1 : -1) * 0.5F, new ItemStack(HabitatItems.KABLOOM_PULP));
                item.setToDefaultPickupDelay();
                if (this.isOnFire() && !item.isFireImmune())
                    item.setOnFireFor(1);
                this.world.spawnEntity(item);
            }

            if (!this.world.isClient) {
                this.world.sendEntityStatus(this, (byte) 3);
                this.discard();
            }
        }
    }
}