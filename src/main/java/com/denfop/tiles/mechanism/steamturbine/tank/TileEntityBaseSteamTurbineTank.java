package com.denfop.tiles.mechanism.steamturbine.tank;

import com.denfop.blocks.FluidName;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerSteamTurbineTank;
import com.denfop.gui.GuiSteamTurbineTank;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.tank.DataFluid;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.steamturbine.ITank;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class TileEntityBaseSteamTurbineTank extends TileEntityMultiBlockElement implements ITank {

    private final int level;
    private final Fluids fluids;
    private final Fluids.InternalFluidTank tank;
    @SideOnly(Side.CLIENT)
    public DataFluid dataFluid;
    private int amount;

    public TileEntityBaseSteamTurbineTank(int level) {
        this.level = level;
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = this.fluids.addTankInsert("tank", 10000 * (level + 1));
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (amount != tank.getFluidAmount()) {
            amount = tank.getFluidAmount();
            new PacketUpdateFieldTile(this, "fluidTank", tank);
        }

    }


    @Override
    public boolean hasOwnInventory() {
        return this.getMain() != null;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("fluidTank")) {
            try {
                this.tank.setFluid(((FluidTank) DecoderHandler.decode(is)).getFluid());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
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
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiSteamTurbineTank(getGuiContainer(var1));
    }

    @Override
    public ContainerSteamTurbineTank getGuiContainer(final EntityPlayer var1) {
        return new ContainerSteamTurbineTank(this, var1);
    }

    @Override
    public int getBlockLevel() {
        return -1;
    }

    @Override
    public FluidTank getTank() {
        return tank;
    }

    @Override
    public void setWaterTank() {
        this.tank.setAcceptedFluids(Fluids.fluidPredicate(FluidRegistry.WATER));
        this.tank.setTypeItemSlot(InvSlot.TypeItemSlot.OUTPUT);
    }

    @Override
    public void setSteamTank() {

        this.tank.setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidsteam.getInstance()));
        this.tank.setTypeItemSlot(InvSlot.TypeItemSlot.INPUT);
    }

    @Override
    public void clear(final boolean steam) {
        if (steam) {
            if (this.tank.getFluid() != null && this.tank.getFluid().getFluid() == FluidRegistry.WATER) {
                this.tank.drain(this.tank.getFluidAmount(), true);
            }
        } else {
            if (this.tank.getFluid() != null && this.tank.getFluid().getFluid() == FluidName.fluidsteam.getInstance()) {
                this.tank.drain(this.tank.getFluidAmount(), true);
            }
        }
    }

}
