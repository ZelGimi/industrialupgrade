package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerAmpereStorage;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiAmpereStorage;
import com.denfop.gui.GuiCore;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;

public class TileEntityAmpereStorage extends TileElectricMachine implements IUpdatableTileEvent {


    public final ComponentBaseEnergy pressure;

    public TileEntityAmpereStorage(BlockPos pos, BlockState state) {
        super(0, 0, 1,BlockBaseMachine3.ampere_storage,pos,state);

        this.pressure = this.addComponent((new ComponentBaseEnergy(EnergyType.AMPERE, this, 100000,

                Arrays.asList(Direction.values()),
                Arrays.asList(Direction.values()),
                EnergyNetGlobal.instance.getTierFromPower(14),
                EnergyNetGlobal.instance.getTierFromPower(14)
        )));

    }


    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.ampere_storage;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        return packet;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {

    }


    public void updateEntityServer() {
        super.updateEntityServer();


    }


    @Override
    public ContainerAmpereStorage getGuiContainer(final Player entityPlayer) {
        return new ContainerAmpereStorage(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiAmpereStorage((ContainerAmpereStorage) menu);
    }


    @Override
    public SoundEvent getSound() {
        return null;
    }

}
