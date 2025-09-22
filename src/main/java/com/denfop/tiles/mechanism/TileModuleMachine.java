package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.container.ContainerModuleMachine;
import com.denfop.gui.GuiModuleMachine;
import com.denfop.invslot.InventoryModule;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileModuleMachine extends TileElectricMachine
        implements IUpdatableTileEvent {


    public final InventoryModule inputslot;
    public final InventoryModule inputslotA;
    public List<String> listItems = new ArrayList<>();

    public TileModuleMachine() {
        super(0, 10, 0);


        this.inputslot = new InventoryModule(this, 0, 27);
        inputslot.setInventoryStackLimit(1);
        this.inputslotA = new InventoryModule(this, 1, 1);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.modulator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.inputslot.update();
    }

    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiModuleMachine(new ContainerModuleMachine(entityPlayer, this));
    }

    public ContainerModuleMachine getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerModuleMachine(entityPlayer, this);
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.pen.getSoundEvent();
    }


    @Override
    public void updateTileServer(EntityPlayer player, double event) {
        if (!this.inputslotA.isEmpty()) {
            initiate(2);
            initiate(0);
            this.inputslotA.get(0).setTagCompound(new NBTTagCompound());
            for (int i = 0; i < this.inputslot.size(); i++) {
                String l = "number_" + i;
                if (i < this.listItems.size()) {
                    ModUtils.NBTSetString(inputslotA.get(), l, this.listItems.get(i));
                }

            }
            final NBTTagCompound nbt = this.inputslotA.get(0).getTagCompound();
            nbt.setInteger("size", this.listItems.size());

        }


    }

}
