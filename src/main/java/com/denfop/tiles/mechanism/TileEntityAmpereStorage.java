package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Energy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerAmpereGenerator;
import com.denfop.container.ContainerAmpereStorage;
import com.denfop.gui.GuiAmpereGenerator;
import com.denfop.gui.GuiAmpereStorage;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class TileEntityAmpereStorage extends TileElectricMachine implements IUpdatableTileEvent {


    public final ComponentBaseEnergy pressure;
    public TileEntityAmpereStorage() {
        super(0, 0, 1);

        this.pressure = this.addComponent((new ComponentBaseEnergy(EnergyType.AMPERE, this, 100000,

                Arrays.asList(EnumFacing.values()),
                Arrays.asList(EnumFacing.values()),
                EnergyNetGlobal.instance.getTierFromPower(14),
                EnergyNetGlobal.instance.getTierFromPower(14), false
        )));

    }



    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.ampere_storage;
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





    public void updateEntityServer() {
        super.updateEntityServer();





    }




    @Override
    public ContainerAmpereStorage getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerAmpereStorage(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiAmpereStorage(getGuiContainer(entityPlayer), b);
    }


    @Override
    public SoundEvent getSound() {
        return null;
    }

}
