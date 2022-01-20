package mod.schnappdragon.habitat.core.registry;

import mod.schnappdragon.habitat.common.entity.animal.PasserineEntity;
import mod.schnappdragon.habitat.common.entity.monster.PookaEntity;
import mod.schnappdragon.habitat.common.entity.projectile.ThrownKabloomFruitEntity;
import mod.schnappdragon.habitat.common.entity.vehicle.HabitatBoat;
import mod.schnappdragon.habitat.Habitat;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Habitat.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HabitatEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Habitat.MOD_ID);

    public static final RegistryObject<EntityType<ThrownKabloomFruitEntity>> KABLOOM_FRUIT = ENTITY_TYPES.register("kabloom_fruit", () -> EntityType.Builder.<ThrownKabloomFruitEntity>of(ThrownKabloomFruitEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build("habitat:kabloom_fruit"));
    public static final RegistryObject<EntityType<HabitatBoat>> BOAT = ENTITY_TYPES.register("boat", () -> EntityType.Builder.<HabitatBoat>of(HabitatBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("habitat:boat"));
    public static final RegistryObject<EntityType<PookaEntity>> POOKA = ENTITY_TYPES.register("pooka", () -> EntityType.Builder.of(PookaEntity::new, MobCategory.MONSTER).sized(0.4F, 0.5F).clientTrackingRange(8).build("habitat:pooka"));

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(POOKA.get(), PookaEntity.registerAttributes().build());
        event.put(PASSERINE.get(), PasserineEntity.registerAttributes().build());
    }
}