package mod.schnappdragon.bloom_and_gloom.core.registry;

import mod.schnappdragon.bloom_and_gloom.core.BloomAndGloom;
import mod.schnappdragon.bloom_and_gloom.common.tileentity.RafflesiaTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BGTileEntityTypes {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BloomAndGloom.MOD_ID);

    public static final RegistryObject<TileEntityType<RafflesiaTile>> RAFFLESIA = TILE_ENTITY_TYPES.register("rafflesia",
            () -> TileEntityType.Builder.create(RafflesiaTile::new, BGBlocks.RAFFLESIA.get()).build(null));
}