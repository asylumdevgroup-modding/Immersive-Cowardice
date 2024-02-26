package com.asylumdev.immersivecowardice;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityArcFurnace;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ArcFurnaceHandler {
    @SubscribeEvent
    public void OnAttachCapability(AttachCapabilitiesEvent<TileEntity> event) {
        if(!CommonConfig.arcFurnaceEnable)
            return;
        TileEntity tile = event.getObject();
        if(tile instanceof TileEntityArcFurnace) {
            event.addCapability(new ResourceLocation(ImmersiveCowardice.MODID,"arc_furnace_electrode_handler"), new ArcFurnaceElectrodeHandler((TileEntityArcFurnace) tile));
        }
    }

    public static class ArcFurnaceElectrodeHandler implements ICapabilityProvider, IItemHandler {
        private static final BlockPos ELECTRODE_POS = new BlockPos(2, 4, 2);


        private TileEntityArcFurnace tile;
        private TileEntityArcFurnace master;
        private IEInventoryHandler handler;

        public ArcFurnaceElectrodeHandler(TileEntityArcFurnace tile) {
            this.tile = tile;
        }

        private void setupHandler() {
            TileEntityArcFurnace currentMaster = tile.master();
            if(this.master != currentMaster) {
                this.handler = new IEInventoryHandler(3, currentMaster, 23, true, false);
                this.master = currentMaster;
            }
        }

        @Override
        public int getSlots() {
            return 3;
        }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot) {
            setupHandler();
            return handler.getStackInSlot(slot);
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            setupHandler();
            ItemStack remainder = handler.insertItem(slot, stack, simulate);
            if(remainder.getCount() < stack.getCount() && !simulate) { //Something changed, update the arc furnace.
                IBlockState state = master.getWorld().getBlockState(master.getPos());
                master.getWorld().notifyBlockUpdate(master.getPos(), state, state, 2);
            }

            return remainder;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            setupHandler();
            return handler.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            setupHandler();
            return handler.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return !stack.isEmpty() && IEContent.itemGraphiteElectrode.equals(stack.getItem()); //yeah, ok, i guess you could have used tags
        }

        @Override
        public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
        }

        @Nullable
        @Override
        public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
            return tile.master() != null && tile.getOrigin().equals(ELECTRODE_POS)
                    ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getDefaultInstance())
                    : null;
        }
    }
}
