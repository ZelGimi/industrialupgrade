package com.denfop.tiles.mechanism.cooling;

import com.denfop.componets.CoolComponent;
import com.denfop.container.ContainerCoolMachine;
import com.denfop.gui.GuiCoolMachine;
import com.denfop.tiles.base.TileEntityElectricMachine;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class TileEntityCooling extends TileEntityElectricMachine implements INetworkClientTileEntityEventListener {


    public final CoolComponent cold;
    public int max;
    public boolean work;
    private int coef;

    public TileEntityCooling() {
        super(10000D, 14, 1);
        this.cold = this.addComponent(CoolComponent.asBasicSource(this, 4, tier));
        this.max = 4;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.max = nbttagcompound.getInteger("max");
        this.work = nbttagcompound.getBoolean("work");
        this.cold.setCapacity(this.max);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("max", this.max);
        nbttagcompound.setBoolean("work", this.work);
        return nbttagcompound;

    }


    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        if (i == 0) {
            this.cold.setCapacity(this.max + 4);
            if (this.cold.getCapacity() > 64) {
                this.cold.setCapacity(64);

            }
            this.max = (int) this.cold.getCapacity();
        }
        if (i == 1) {
            this.cold.setCapacity(this.max - 4);
            if (this.cold.getCapacity() < 4) {
                this.cold.setCapacity(4);

            }
            this.max = (int) this.cold.getCapacity();
        }
        if (i == 2) {
            this.work = !this.work;
        }
        if (i == 10) {
            super.onNetworkEvent(entityPlayer, i);
        }
    }

    public String getStartSoundFile() {
        return "Machines/cooling.ogg";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 30 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip, advanced);
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        this.coef = (int) Math.max(Math.ceil(this.cold.storage / 16), 1);
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.cold.allow || work) {
            if (this.energy.getEnergy() >= 30 * this.coef && this.cold.getEnergy() < this.cold.getCapacity()) {
                this.cold.addEnergy(1);
                this.energy.useEnergy(30 * this.coef);
                initiate(0);

            }
            if (this.world.provider.getWorldTime() % 400 == 0) {
                initiate(2);
            }

            if (this.energy.getEnergy() < 30 * this.coef) {
                initiate(2);
            } else {
                initiate(0);
            }

        } else {
            initiate(2);
        }
        if (this.world.provider.getWorldTime() % 20 == 0 && this.cold.getEnergy() >= 1) {
            this.cold.addEnergy(-1);
        }
    }


    @Override
    public ContainerCoolMachine getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerCoolMachine(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiCoolMachine(getGuiContainer(entityPlayer));
    }


}
