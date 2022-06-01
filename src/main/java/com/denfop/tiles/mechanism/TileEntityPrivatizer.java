package com.denfop.tiles.mechanism;


import com.denfop.IUCore;
import com.denfop.container.ContainerPrivatizer;
import com.denfop.gui.GuiPrivatizer;
import com.denfop.invslot.InvSlotPrivatizer;
import com.denfop.tiles.base.TileEntityElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityPrivatizer extends TileEntityElectricMachine
        implements IHasGui, INetworkTileEntityEventListener, INetworkClientTileEntityEventListener {


    public final InvSlotPrivatizer inputslot;
    public final InvSlotPrivatizer inputslotA;


    public TileEntityPrivatizer() {
        super(0, 10, 1);


        this.inputslot = new InvSlotPrivatizer(this, "input", 0, 9);
        this.inputslotA = new InvSlotPrivatizer(this, "input2", 1, 1);
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
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiPrivatizer(new ContainerPrivatizer(entityPlayer, this));
    }

    public ContainerBase<? extends TileEntityPrivatizer> getGuiContainer(EntityPlayer entityPlayer) {
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
    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, getStartSoundFile());
        }
        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (getInterruptSoundFile() != null) {
                        IUCore.audioManager.playOnce(this, getInterruptSoundFile());
                    }
                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
                break;
        }
    }

    @Override
    public void onGuiClosed(EntityPlayer arg0) {
    }

    @Override
    public String getInventoryName() {
        return Localization.translate("iu.blockModuleMachine.name");
    }


    @Override
    public void onNetworkEvent(EntityPlayer player, int event) {
        if (!this.inputslotA.isEmpty()) {
            initiate(1);
            NBTTagCompound nbt = ModUtils.nbt(this.inputslotA.get());
            for (int i = 0; i < this.inputslot.size(); i++) {
                if (this.inputslot.get(i) != null) {
                    NBTTagCompound nbt1 = ModUtils.nbt(this.inputslot.get(i));
                    nbt.setString("player_" + i, nbt1.getString("name"));
                }
            }
        }


    }

}
