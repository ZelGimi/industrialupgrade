package com.denfop.blockentity.mechanism.steamturbine;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamTurbineEntity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSteamTurbineControllerRod;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSteamTurbineControllerRod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class BlockEntitySteamTurbineControllerRod extends BlockEntityMultiBlockElement implements IControllerRod,
        IUpdatableTileEvent {

    List<IRod> list = new ArrayList<>();

    public BlockEntitySteamTurbineControllerRod(BlockPos pos, BlockState state) {
        super(BlockSteamTurbineEntity.steam_turbine_controller_rod, pos, state);
    }


    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSteamTurbineEntity.steam_turbine_controller_rod;
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

        BlockEntityMultiBlockElement tileMultiBlockBase =
                (BlockEntityMultiBlockElement) this.getWorld().getBlockEntity(list.get((int) var2).getBlockPos());
        if (tileMultiBlockBase != null && tileMultiBlockBase.getMain() != null && tileMultiBlockBase
                .getMain()
                .isFull() && !tileMultiBlockBase.isRemoved()) {
            tileMultiBlockBase.onActivated(var1, var1.getUsedItemHand(), Direction.NORTH, new Vec3(0, 0, 0));

        }

    }

    @Override
    public ContainerMenuSteamTurbineControllerRod getGuiContainer(final Player var1) {
        return new ContainerMenuSteamTurbineControllerRod(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSteamTurbineControllerRod((ContainerMenuSteamTurbineControllerRod) menu);
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
