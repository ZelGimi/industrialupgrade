package com.denfop.tiles.mechanism.steamturbine.coolant;

import com.denfop.blocks.FluidName;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerSteamTurbineCoolant;
import com.denfop.gui.GuiSteamTurbineCoolant;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.steamturbine.ICoolant;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntityBaseSteamTurbineCoolant extends TileEntityMultiBlockElement implements ICoolant, IUpdatableTileEvent {

    private final Fluids fluids;
    private final int level;
    private final Fluids.InternalFluidTank tank;
    double power = 0;
    Fluid currentStack = null;
    private int x = 1;

    public TileEntityBaseSteamTurbineCoolant(int level) {
        this.level = level;
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = this.fluids.addTankInsert("tank", 10000);
        this.getFluidsFromLevel();
    }

    private void getFluidsFromLevel() {
        List<Fluid> fluidList = new ArrayList<>();
        switch (level) {
            default:
                fluidList.add(FluidName.fluidhyd.getInstance());
                break;
            case 1:
                fluidList.add(FluidName.fluidhyd.getInstance());
                fluidList.add(FluidName.fluidazot.getInstance());
                fluidList.add(FluidName.fluidcoolant.getInstance());
                break;
            case 2:
                fluidList.add(FluidName.fluidhyd.getInstance());
                fluidList.add(FluidName.fluidazot.getInstance());
                fluidList.add(FluidName.fluidcoolant.getInstance());
                fluidList.add(FluidName.fluidHelium.getInstance());
                break;
            case 3:
                fluidList.add(FluidName.fluidhyd.getInstance());
                fluidList.add(FluidName.fluidazot.getInstance());
                fluidList.add(FluidName.fluidcoolant.getInstance());
                fluidList.add(FluidName.fluidHelium.getInstance());
                fluidList.add(FluidName.fluidcryogen.getInstance());
                break;


        }
        this.tank.setAcceptedFluids(Fluids.fluidPredicate(fluidList));
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
                .hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) && this.getMain() != null) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
            );
        } else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeInt(this.x);
        customPacketBuffer.writeDouble(this.power);
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.x = customPacketBuffer.readInt();
        this.power = customPacketBuffer.readDouble();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiSteamTurbineCoolant(getGuiContainer(var1));
    }


    @Override
    public ContainerSteamTurbineCoolant getGuiContainer(final EntityPlayer var1) {
        return new ContainerSteamTurbineCoolant(this, var1);
    }

    @Override
    public boolean hasOwnInventory() {
        return this.getMain() != null;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        x = nbtTagCompound.getInteger("coolant_x");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setInteger("coolant_x", x);
        return nbtTagCompound;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (this.getMain() == null) {
            return;
        }
        if (var2 == 0) {
            this.x = Math.min(this.x + 1, this.level + 2);
        } else {
            this.x = Math.max(0, this.x - 1);
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.tank.getFluid() != null) {
            currentStack = this.tank.getFluid().getFluid();
            power = getPowerFromFluid();
        } else {
            currentStack = null;
            power = 0;
        }
    }

    private double getPowerFromFluid() {
        Fluid hyd = FluidName.fluidhyd.getInstance();
        Fluid hel = FluidName.fluidHelium.getInstance();
        Fluid coolant = FluidName.fluidcoolant.getInstance();
        Fluid nitrogen = FluidName.fluidazot.getInstance();
        Fluid cryogen = FluidName.fluidcryogen.getInstance();
        if (this.currentStack == hyd) {
            this.power = 1;
        } else if (this.currentStack == nitrogen) {
            this.power = 1.5;
        } else if (this.currentStack == coolant) {
            this.power = 2.5;
        } else if (this.currentStack == hel) {
            this.power = 4;
        } else if (this.currentStack == cryogen) {
            this.power = 8;
        }
        return this.power;
    }

    @Override
    public FluidTank getCoolant() {
        return tank;
    }

    @Override
    public double getPower() {
        return this.power * this.x;
    }

    @Override
    public int getPressure() {
        return this.x;
    }

    @Override
    public int getBlockLevel() {
        return -1;
    }

}
