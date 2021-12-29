package com.denfop.tiles.mechanism;

import com.denfop.container.ContainerModuleMachine;
import com.denfop.gui.GUIModuleMachine;
import com.denfop.invslot.InvSlotModule;
import com.denfop.tiles.base.TileEntityElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityModuleMachine extends TileEntityElectricMachine
        implements IHasGui, INetworkTileEntityEventListener, INetworkClientTileEntityEventListener {


    public AudioSource audioSource;

    public final InvSlotModule inputslot;
    public final InvSlotModule inputslotA;

    public TileEntityModuleMachine() {
        super(null, 0, 10, 0);


        this.inputslot = new InvSlotModule(this, "input", 0, 9);
        this.inputslotA = new InvSlotModule(this, "input2", 1, 1);
    }


    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIModuleMachine(new ContainerModuleMachine(entityPlayer, this));
    }

    public ContainerBase<? extends TileEntityModuleMachine> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerModuleMachine(entityPlayer, this);
    }


    public void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    @Override
    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null) {
            this.audioSource = IC2.audioManager.createSource(this, getStartSoundFile());
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
                        IC2.audioManager.playOnce(this, getInterruptSoundFile());
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
            for (int i = 0; i < this.inputslot.size(); i++) {
                if (this.inputslot.get(i) != null && !this.inputslot.get(i).getItem().equals(Items.AIR)) {


                    int id = OreDictionary.getOreIDs(this.inputslot.get(i))[0];
                    String ore = OreDictionary.getOreName(id);

                    boolean existore = false;
                    for (int j = 0; j < this.inputslot.size(); j++) {
                        if (this.inputslot.get(i) != null) {
                            String l = "number_" + j;
                            String temp = ModUtils.NBTGetString(inputslotA.get(), l);
                            if (temp.contains(ore)) {
                                existore = true;
                                break;
                            }

                        }
                    }
                    if (!existore) {

                        String l = "number_" + i;

                        ModUtils.NBTSetString(inputslotA.get(), l, ore);
                    }


                } else {
                    String l = "number_" + i;
                    ModUtils.NBTSetString(inputslotA.get(), l, "");
                }

            }
        }


    }

}
