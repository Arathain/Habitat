package mod.schnappdragon.habitat.common.entity.item;

import mod.schnappdragon.habitat.core.Habitat;
import mod.schnappdragon.habitat.core.registry.HabitatEntityTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class HabitatBoatEntity extends Boat {
    private static final EntityDataAccessor<Integer> BOAT_TYPE = SynchedEntityData.defineId(HabitatBoatEntity.class, EntityDataSerializers.INT);

    public HabitatBoatEntity(EntityType<? extends Boat> typeIn, Level worldIn) {
        super(typeIn, worldIn);
        this.blocksBuilding = true;
    }

    public HabitatBoatEntity(Level worldIn, double x, double y, double z) {
        this(HabitatEntityTypes.BOAT.get(), worldIn);
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    public HabitatBoatEntity.Type getHabitatBoatType() {
        return HabitatBoatEntity.Type.byId(this.entityData.get(BOAT_TYPE));
    }

    @Override
    public Item getDropItem() {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(Habitat.MODID, this.getHabitatBoatType().getName() + "_boat"));
    }

    public void setBoatType(HabitatBoatEntity.Type boatType) {
        this.entityData.set(BOAT_TYPE, boatType.ordinal());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BOAT_TYPE, Type.FAIRY_RING_MUSHROOM.ordinal());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putString("Type", this.getHabitatBoatType().getName());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("Type", 8))
            this.setBoatType(Type.getTypeFromString(compound.getString("Type")));
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        this.lastYd = this.getDeltaMovement().y;
        if (!this.isPassenger()) {
            if (onGroundIn) {
                if (this.fallDistance > 3.0F) {
                    if (this.status != Boat.Status.ON_LAND) {
                        this.fallDistance = 0.0F;
                        return;
                    }

                    this.causeFallDamage(this.fallDistance, 1.0F);
                    if (!this.level.isClientSide && !this.removed) {
                        this.remove();
                        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            Item planks = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Habitat.MODID, this.getHabitatBoatType().getName() + "_planks"));

                            for (int i = 0; i < 3; ++i)
                                this.spawnAtLocation(planks);

                            for (int j = 0; j < 2; ++j)
                                this.spawnAtLocation(Items.STICK);
                        }
                    }
                }

                this.fallDistance = 0.0F;
            }
            else if (!this.level.getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && y < 0.0D)
                this.fallDistance = (float) ((double) this.fallDistance - y);
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public enum Type {
        FAIRY_RING_MUSHROOM("fairy_ring_mushroom");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static HabitatBoatEntity.Type byId(int id) {
            HabitatBoatEntity.Type[] types = values();
            if (id < 0 || id >= types.length)
                id = 0;

            return types[id];
        }

        public static HabitatBoatEntity.Type getTypeFromString(String nameIn) {
            HabitatBoatEntity.Type[] types = values();

            for (Type type : types) {
                if (type.getName().equals(nameIn))
                    return type;
            }

            return types[0];
        }
    }
}