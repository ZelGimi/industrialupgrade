package com.denfop.tiles.base;

import com.denfop.componets.CoolComponent;
import com.denfop.container.ContainerCoolMachine;
import com.denfop.gui.GuiCoolMachine;
import ic2.api.network.INetworkClientTileEntityEventListener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityCooling extends TileEntityElectricMachine implements INetworkClientTileEntityEventListener {


    public final CoolComponent cold;
    public int max;

    public TileEntityCooling() {
        super(10000D, 14, 1);
        this.cold = this.addComponent(CoolComponent.asBasicSource(this, 4, tier));
        this.max = 4;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.max = nbttagcompound.getInteger("max");
        this.cold.setCapacity(this.max);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("max", this.max);
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
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.world.provider.getWorldTime() % 20 == 0) {
            this.cold.addEnergy(-1);
        }
        if (this.energy.getEnergy() >= 50 && this.cold.getEnergy() < this.cold.getCapacity()) {
            this.cold.addEnergy(1);
            this.energy.useEnergy(50);
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
