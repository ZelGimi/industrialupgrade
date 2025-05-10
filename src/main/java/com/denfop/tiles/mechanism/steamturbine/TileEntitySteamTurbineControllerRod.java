package com.denfop.tiles.mechanism.steamturbine;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbine;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSteamTurbineControllerRod;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSteamTurbineControllerRod;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class TileEntitySteamTurbineControllerRod extends TileEntityMultiBlockElement implements IControllerRod,
        IUpdatableTileEvent {

    List<IRod> list = new ArrayList<>();

    public TileEntitySteamTurbineControllerRod(BlockPos pos, BlockState state) {
        super(BlockSteamTurbine.steam_turbine_controller_rod, pos, state);
    }


    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSteamTurbine.steam_turbine_controller_rod;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_turbine.getBlock(getTeBlock());
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
            list.add((IRod) level.getBlockEntity(customPacketBuffer.readBlockPos()));
        }
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {

        TileEntityMultiBlockElement tileMultiBlockBase =
                (TileEntityMultiBlockElement) this.getWorld().getBlockEntity(list.get((int) var2).getBlockPos());
        if (tileMultiBlockBase != null && tileMultiBlockBase.getMain() != null && tileMultiBlockBase
                .getMain()
                .isFull() && !tileMultiBlockBase.isRemoved()) {
            tileMultiBlockBase.onActivated(var1, var1.getUsedItemHand(), Direction.NORTH, new Vec3(0, 0, 0));

        }

    }

    @Override
    public ContainerSteamTurbineControllerRod getGuiContainer(final Player var1) {
        return new ContainerSteamTurbineControllerRod(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiSteamTurbineControllerRod((ContainerSteamTurbineControllerRod) menu);
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
