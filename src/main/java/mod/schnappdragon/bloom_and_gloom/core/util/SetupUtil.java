package mod.schnappdragon.bloom_and_gloom.core.util;

import mod.schnappdragon.bloom_and_gloom.common.tileentity.RafflesiaTileEntity;
import mod.schnappdragon.bloom_and_gloom.core.registry.BGBlocks;
import mod.schnappdragon.bloom_and_gloom.core.registry.BGItems;
import mod.schnappdragon.bloom_and_gloom.core.registry.BGSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static mod.schnappdragon.bloom_and_gloom.common.block.RafflesiaBlock.*;

public class SetupUtil {

    /*
     * Common Setup Helper
     */

    public static void registerComposterChances() {
        ComposterBlock.CHANCES.put(BGItems.RAFFLESIA_SEED.get(), 0.3F);
    }

    public static void registerDispenserBehaviour() {
        DispenserBlock.registerDispenseBehavior(Items.SUSPICIOUS_STEW, new OptionalDispenseBehavior() {
            /**
             * Dispense the specified stack, play the dispense sound and spawn particles.
             */
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                World worldIn = source.getWorld();
                BlockPos pos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
                BlockState state = worldIn.getBlockState(pos);
                if (!worldIn.isRemote && state.isIn(BGBlocks.RAFFLESIA_BLOCK.get()) && state.get(AGE) == 2) {
                    TileEntity tile = worldIn.getTileEntity(pos);
                    if (tile instanceof RafflesiaTileEntity && !state.get(STEW) && !state.get(POLLINATED)) {
                        RafflesiaTileEntity rafflesia = (RafflesiaTileEntity) tile;
                        CompoundNBT tag = stack.getTag();
                        if (tag != null && tag.contains("Effects", 9)) {
                            rafflesia.Effects = tag.getList("Effects", 10);
                        }
                        worldIn.setBlockState(pos, state.with(STEW, true));
                        rafflesia.onChange(worldIn, worldIn.getBlockState(pos));
                        worldIn.playSound(null, pos, BGSoundEvents.RAFFLESIA_SLURP.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                        stack = new ItemStack(Items.BOWL, 1);
                        this.setSuccessful(true);
                    }
                }
                else
                    this.setSuccessful(false);
                return stack;
            }
        });
    }

    /*
     * Client Setup Helper
     */

    public static void registerRenderLayers() {
        RenderTypeLookup.setRenderLayer(BGBlocks.RAFFLESIA_BLOCK.get(), RenderType.getCutout());
    }
}
