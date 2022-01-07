package mod.schnappdragon.habitat.common.registry;

import mod.schnappdragon.habitat.Habitat;
import mod.schnappdragon.habitat.common.entity.animal.PasserineEntity;
import mod.schnappdragon.habitat.common.entity.monster.PookaEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class HabitatEntities {
    private static final Map<EntityType<?>, Identifier> ENTITY_TYPES = new LinkedHashMap<>();
    private static final Map<BlockEntityType<?>, Identifier> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

    public static final EntityType<PasserineEntity> PASSERINE = createEntity("passerine", PasserineEntity.createPasserineAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PasserineEntity::new).dimensions(EntityDimensions.fixed(0.5F, 0.5F)).trackRangeChunks(8).build());
    public static final EntityType<PookaEntity> POOKA = createEntity("pooka", PookaEntity.registerPookaAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, PookaEntity::new).dimensions(EntityDimensions.fixed(0.4F, 0.5F)).trackRangeChunks(8).build());


    private static <T extends Entity> EntityType<T> createEntity(String name, EntityType<T> type) {
        ENTITY_TYPES.put(type, new Identifier(Habitat.MOD_ID, name));
        return type;
    }
    private static <T extends BlockEntity> BlockEntityType<T> createBlockEntity(String name, BlockEntityType<T> type) {
        BLOCK_ENTITY_TYPES.put(type, new Identifier(Habitat.MOD_ID, name));
        return type;
    }

    private static <T extends LivingEntity> EntityType<T> createEntity(String name, DefaultAttributeContainer.Builder attributes, EntityType<T> type) {
        FabricDefaultAttributeRegistry.register(type, attributes);
        ENTITY_TYPES.put(type, new Identifier(Habitat.MOD_ID, name));
        return type;
    }

    public static void init() {
        ENTITY_TYPES.keySet().forEach(entityType -> Registry.register(Registry.ENTITY_TYPE, ENTITY_TYPES.get(entityType), entityType));
        BLOCK_ENTITY_TYPES.keySet().forEach(entityType -> Registry.register(Registry.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(entityType), entityType));
    }
}
