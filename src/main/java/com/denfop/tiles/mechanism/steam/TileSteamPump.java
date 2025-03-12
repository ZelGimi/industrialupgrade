package com.denfop.tiles.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.componets.PressureComponent;
import com.denfop.container.ContainerSteamPump;
import com.denfop.gui.GuiSteamPump;
import com.denfop.invslot.InvSlot;
import com.denfop.network.packet.CustomPacketBuffer;
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
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class TileSteamPump extends TileElectricLiquidTankInventory {

    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final PressureComponent pressure;
    public final ComponentSteamEnergy steam;
    private final Fluids.InternalFluidTank fluidTank1;
    public double energyConsume;
    public int operationsPerTick;
    public int operationLength;
    public ComponentProgress componentProgress;

    public TileSteamPump() {
        super(0, 1, 10);
        this.defaultEnergyConsume = this.energyConsume = 2;
        this.defaultOperationLength = this.operationLength = 25;
        this.defaultTier = 1;
        this.defaultEnergyStorage = this.operationLength;
        componentProgress = this.addComponent(new ComponentProgress(this, 1, (short) operationLength));

        this.fluidTank.setTypeItemSlot(InvSlot.TypeItemSlot.OUTPUT);
        this.fluidTank1 = fluids.addTank("fluidTank2", 4000, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance()
        ), InvSlot.TypeItemSlot.NONE);
        this.pressure = this.addComponent(PressureComponent.asBasicSink(this, 1));
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 4000));
        this.steam.setFluidTank(fluidTank1);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.steam_pump;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }


    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);


    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();

        return packet;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.defaultEnergyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.defaultOperationLength);
        }
        super.addInformation(stack, tooltip);

    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.steam.canUseEnergy((this.energyConsume))) {

            if (componentProgress.getProgress() < componentProgress.getMaxValue()) {
                componentProgress.addProgress(0);
                this.steam.useEnergy(energyConsume);
            } else {
                if (this.canoperate()) {
                    componentProgress.setProgress((short) 0);
                }
            }
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
                for (int k = this.pos.getY() - 3; k <= this.pos.getY() - 1; k++) {

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

    public ContainerSteamPump getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSteamPump(entityPlayer, this);
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

    @SideOnly(Side.CLIENT)
    public GuiSteamPump getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiSteamPump(getGuiContainer(entityPlayer));
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.PumpOp.getSoundEvent();
    }

}
