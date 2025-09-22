package com.denfop.tiles.mechanism;


import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerPrivatizer;
import com.denfop.gui.GuiPrivatizer;
import com.denfop.invslot.InventoryPrivatizer;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TilePrivatizer extends TileElectricMachine
        implements IUpdatableTileEvent {


    public final InventoryPrivatizer inputslot;
    public final InventoryPrivatizer inputslotA;
    public List<String> listItems = new ArrayList<>();

    public TilePrivatizer() {
        super(0, 10, 1);


        this.inputslot = new InventoryPrivatizer(this, 0, 9);
        this.inputslotA = new InventoryPrivatizer(this, 1, 1);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.privatizer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;

    }


    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public GuiPrivatizer getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiPrivatizer(new ContainerPrivatizer(entityPlayer, this));
    }

    public ContainerPrivatizer getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerPrivatizer(entityPlayer, this);
    }


    public String getStartSoundFile() {
        return "Machines/pen.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/pen.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.inputslot.update();
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.pen.getSoundEvent();
    }


    @Override
    public void updateTileServer(EntityPlayer player, double event) {
        if (!this.inputslotA.isEmpty()) {
            initiate(1);
            NBTTagCompound nbt = ModUtils.nbt(this.inputslotA.get());
            for (int i = 0; i < this.listItems.size(); i++) {
                nbt.setString("player_" + i, this.listItems.get(i));

            }
            nbt.setInteger("size", this.listItems.size());
        }


    }

}
