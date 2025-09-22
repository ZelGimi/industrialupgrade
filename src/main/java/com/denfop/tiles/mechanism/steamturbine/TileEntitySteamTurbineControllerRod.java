package com.denfop.tiles.mechanism.steamturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import com.denfop.container.ContainerSteamTurbineControllerRod;
import com.denfop.gui.GuiSteamTurbineControllerRod;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntitySteamTurbineControllerRod extends TileEntityMultiBlockElement implements IControllerRod,
        IUpdatableTileEvent {

    List<IRod> list = new ArrayList<>();

    public TileEntitySteamTurbineControllerRod() {

    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_controller_rod;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine;
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeInt(list.size());
        for (IRod rod : list) {
            customPacketBuffer.writeBlockPos(rod.getBlockPos());
        }
        return customPacketBuffer;
    }

    @Override
    public int getBlockLevel() {
        return -1;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        int size = customPacketBuffer.readInt();
        list.clear();
        for (int i = 0; i < size; i++) {
            list.add((IRod) world.getTileEntity(customPacketBuffer.readBlockPos()));
        }
    }

    public BlockPos getBlockPos() {
        return this.pos;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {

        TileEntityMultiBlockElement tileMultiBlockBase =
                (TileEntityMultiBlockElement) this.getWorld().getTileEntity(list.get((int) var2).getBlockPos());
        if (tileMultiBlockBase != null && tileMultiBlockBase.getMain() != null && tileMultiBlockBase
                .getMain()
                .isFull() && !tileMultiBlockBase.isInvalid()) {
            tileMultiBlockBase.onActivated(var1, var1.getActiveHand(), EnumFacing.NORTH, 0, 0, 0);

        }

    }

    @Override
    public ContainerSteamTurbineControllerRod getGuiContainer(final EntityPlayer var1) {
        return new ContainerSteamTurbineControllerRod(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiSteamTurbineControllerRod getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiSteamTurbineControllerRod(getGuiContainer(var1));
    }

    @Override
    public List<IRod> getRods() {
        return list;
    }

    @Override
    public boolean hasOwnInventory() {
        return this.getMain() != null;
    }

    @Override
    public void setList(final List<IRod> rods) {
        list = rods;
    }

}
