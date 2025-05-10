package com.denfop.tiles.mechanism.steamturbine.tank;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.FluidName;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSteamTurbineTank;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSteamTurbineTank;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.steamturbine.ITank;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;

public class TileEntityBaseSteamTurbineTank extends TileEntityMultiBlockElement implements ITank {

    private final int blockLevel;
    private final Fluids fluids;
    private final Fluids.InternalFluidTank tank;

    private int amount;

    public TileEntityBaseSteamTurbineTank(int blockLevel, IMultiTileBlock tileBlock, BlockPos pos, BlockState state) {
        super(tileBlock,pos,state);
        this.blockLevel = blockLevel;
        this.fluids = this.addComponent(new Fluids(this));
        this.tank = this.fluids.addTankInsert("tank", 10000 * (blockLevel + 1));
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
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (!this.getWorld().isClientSide && FluidHandlerFix.getFluidHandler(player.getItemInHand(hand)) != null && this.getMain() != null) {

            return ModUtils.interactWithFluidHandler(player, hand,
                    fluids.getCapability(ForgeCapabilities.FLUID_HANDLER, side)
            );
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }



    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiSteamTurbineTank((ContainerSteamTurbineTank) menu);
    }

    @Override
    public ContainerSteamTurbineTank getGuiContainer(final Player var1) {
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
        this.tank.setAcceptedFluids(Fluids.fluidPredicate(net.minecraft.world.level.material.Fluids.WATER));
        this.tank.setTypeItemSlot(InvSlot.TypeItemSlot.OUTPUT);
    }

    @Override
    public void setSteamTank() {

        this.tank.setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidsteam.getInstance().get()));
        this.tank.setTypeItemSlot(InvSlot.TypeItemSlot.INPUT);
    }

    @Override
    public void clear(final boolean steam) {
        if (steam) {
            if (!this.tank.getFluid().isEmpty() && this.tank.getFluid().getFluid() == net.minecraft.world.level.material.Fluids.WATER) {
                this.tank.drain(this.tank.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
            }
        } else {
            if (!this.tank.getFluid().isEmpty() && this.tank.getFluid().getFluid() == FluidName.fluidsteam.getInstance().get()) {
                this.tank.drain(this.tank.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

}
