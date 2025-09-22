package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockPrimalPump;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerPump;
import com.denfop.gui.GuiPump;
import com.denfop.invslot.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileElectricLiquidTankInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

public class TileEntityPrimalPump extends TileElectricLiquidTankInventory {


    public ComponentProgress componentProgress;
    private int prevAmount;

    public TileEntityPrimalPump() {
        super(0, 1, 4);
        componentProgress = this.addComponent(new ComponentProgress(this, 1, (short) 25));
        this.fluidTank.setTypeItemSlot(Inventory.TypeItemSlot.OUTPUT);
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockPrimalPump.primal_pump;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.primal_pump;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);


    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            if (fluidTank1 != null) {
                this.fluidTank.readFromNBT(fluidTank1.writeToNBT(new NBTTagCompound()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("fluidtank")) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank1 != null) {
                    this.fluidTank.readFromNBT(fluidTank1.writeToNBT(new NBTTagCompound()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public boolean onSneakingActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (!this.getWorld().isRemote && this.fluidTank.getFluidAmount() + 1000 <= this.fluidTank.getCapacity()) {
            if (componentProgress.getProgress() < componentProgress.getMaxValue()) {
                componentProgress.addProgress(0, (short) 4);
            } else {
                if (this.canoperate()) {
                    componentProgress.setProgress((short) 0);
                }
            }
            this.setActive(true);
        } else {
            this.setActive(false);
        }
        return super.onSneakingActivated(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (!this.getWorld().isRemote && player
                .getHeldItem(hand)
                .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        } else {

            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();

        return packet;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        for (int i = 1; i < 6; i++) {
            tooltip.add(Localization.translate("primitive_pump.info" + i));
        }
    }


    public CustomPacketBuffer writePacket() {
        CustomPacketBuffer customPacketBuffer = super.writePacket();
        try {
            EncoderHandler.encode(customPacketBuffer, fluidTank);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.prevAmount != this.fluidTank.getFluidAmount()) {
            this.prevAmount = this.fluidTank.getFluidAmount();
            new PacketUpdateFieldTile(this, "fluidtank", this.fluidTank);
        }
    }

    public boolean canoperate() {
        return this.operate(true);
    }

    public boolean operate(boolean sim) {
        if (this.fluidTank.getFluidAmount() >= this.fluidTank.getCapacity()) {
            return false;
        }
        FluidStack liquid;
        boolean canOperate = false;
        for (int i = this.pos.getX() - 1; i <= this.pos.getX() + 1; i++) {
            for (int j = this.pos.getZ() - 1; j <= this.pos.getZ() + 1; j++) {
                for (int k = this.pos.getY() - 1; k <= this.pos.getY() - 1; k++) {

                    if (this.fluidTank.getFluidAmount() >= this.fluidTank.getCapacity()) {
                        return false;
                    }
                    liquid = this.pump(new BlockPos(i, k,
                            j
                    ), false);
                    if (this.getFluidTank().fill(liquid, false) > 0) {
                        this.getFluidTank().fill(liquid, true);
                        canOperate = true;
                    }
                }

            }
        }


        return canOperate;
    }

    public FluidStack pump(BlockPos pos, boolean sim) {
        FluidStack ret = null;
        int freespace = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();

        if (freespace >= 1000) {
            IBlockState block = this.getWorld().getBlockState(pos);
            if (block.getMaterial().isLiquid()) {


                if (block.getBlock() instanceof IFluidBlock) {
                    IFluidBlock liquid = (IFluidBlock) block.getBlock();
                    if ((this.fluidTank.getFluid() == null || this.fluidTank
                            .getFluid()
                            .getFluid() == liquid.getFluid()) && liquid.canDrain(
                            this.getWorld(),
                            pos
                    )) {
                        if (!sim) {
                            ret = liquid.drain(this.getWorld(), pos, true);
                            this.getWorld().setBlockToAir(pos);
                        } else {
                            ret = new FluidStack(liquid.getFluid(), 1000);
                        }
                    }
                } else {
                    if (block.getBlock().getMetaFromState(block) != 0) {
                        return null;
                    }

                    ret = new FluidStack(FluidRegistry.getFluid(block.getBlock().getUnlocalizedName().substring(5)), 1000);
                    if (this.fluidTank.getFluid() == null || this.fluidTank
                            .getFluid()
                            .getFluid() == ret.getFluid()) {
                        if (!sim) {
                            this.getWorld().setBlockToAir(pos);
                        }
                    }
                }
            }
        }


        return ret;
    }


    public void onLoaded() {
        super.onLoaded();


    }


    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energy.canUseEnergy(amount)) {
            this.energy.useEnergy(amount);
            return true;
        } else {
            return false;
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    public ContainerPump getGuiContainer(EntityPlayer entityPlayer) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public GuiPump getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return null;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.PumpOp.getSoundEvent();
    }

}
