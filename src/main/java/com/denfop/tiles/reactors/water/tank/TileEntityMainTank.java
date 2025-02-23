package com.denfop.tiles.reactors.water.tank;

import com.denfop.componets.Fluids;
import com.denfop.container.ContainerWaterTank;
import com.denfop.gui.GuiMainTank;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.ITank;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class TileEntityMainTank extends TileEntityMultiBlockElement implements ITank {

    public final Fluids fluids;
    public final Fluids.InternalFluidTank tank;
    public int level = 0;

    public TileEntityMainTank(int col) {
        this.fluids = this.addComponent(new Fluids(this));
        tank = this.fluids.addTank("fluidTank", col);
        tank.setCanAccept(false);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        tank.setCanAccept(this.getMain() != null && this.getMain().isFull());
        int level = (int) (3 * (this.tank.getFluidAmount() / (this.tank.getCapacity() * 1D)));
        if (level != this.level) {
            this.level = level;
            new PacketUpdateFieldTile(this, "level", this.level);
            if (level != 0) {
                this.setActive(String.valueOf(this.level));
            } else {
                this.setActive("");
            }
        }
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
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("level")) {
            try {
                this.level = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiMainTank(getGuiContainer(var1));
    }

    @Override
    public ContainerWaterTank getGuiContainer(final EntityPlayer var1) {
        return new ContainerWaterTank(this, var1);
    }

    public Fluids getFluids() {
        return fluids;
    }

    public Fluids.InternalFluidTank getTank() {
        return tank;
    }

    @Override
    public void setFluid(final Fluid fluid) {
        tank.setAcceptedFluids(Fluids.fluidPredicate(fluid));
    }

}
