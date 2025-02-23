package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerAmpereGenerator;
import com.denfop.gui.GuiAmpereGenerator;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAmpereGenerator extends TileElectricMachine implements IUpdatableTileEvent {


    public final ComponentBaseEnergy pressure;
    public final Energy energy;

    public TileEntityAmpereGenerator() {
        super(0, 0, 1);


        this.energy = this.addComponent(Energy.asBasicSink(this, 4000,14));
        this.pressure = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.AMPERE, this, 2000));


    }




    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.ampere_generator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
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
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {

    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;

    }


    public void updateEntityServer() {
        super.updateEntityServer();


            if (this.energy.getEnergy() >= 2 && this.pressure.getEnergy() + 1 <= this.pressure.getCapacity()) {
                this.pressure.addEnergy(1);
                this.energy.useEnergy(2);
                this.setActive(true);
            } else {
                setActive(false);
            }



    }




    @Override
    public ContainerAmpereGenerator getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerAmpereGenerator(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiAmpereGenerator(getGuiContainer(entityPlayer), b);
    }


    @Override
    public SoundEvent getSound() {
        return null;
    }

}
