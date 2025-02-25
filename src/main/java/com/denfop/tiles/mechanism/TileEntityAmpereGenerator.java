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

import java.util.List;

public class TileEntityAmpereGenerator extends TileElectricMachine implements IUpdatableTileEvent, IManufacturerBlock {


    public final ComponentBaseEnergy pressure;
    public final Energy energy;
    public int level;
    public TileEntityAmpereGenerator() {
        super(0, 0, 1);


        this.energy = this.addComponent(Energy.asBasicSink(this, 4000,14));
        this.pressure = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.AMPERE, this, 2000));


    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final int level) {
        this.level = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.level -= level;
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
        if (level < 10) {
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation)) {
                return super.onActivated(player, hand, side, hitX, hitY, hitZ);
            } else {
                stack.shrink(1);
                this.level++;
                return true;
            }
        } else {

            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.level != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation, this.level));
            this.level = 0;
        }
        return ret;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.level = nbttagcompound.getInteger("level");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("level", this.level);
        return nbttagcompound;
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





    public void updateEntityServer() {
        super.updateEntityServer();


            if (this.energy.getEnergy() >= 2 && this.pressure.getEnergy() + 1 <= this.pressure.getCapacity()) {
                int max = (int) Math.min(level+1, energy.getEnergy()/((level+1)*2));
                max = (int) Math.min(max, (this.pressure.getCapacity()-pressure.getEnergy())/((level+1)));
                this.pressure.addEnergy(max);
                this.energy.useEnergy(max*2);
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
