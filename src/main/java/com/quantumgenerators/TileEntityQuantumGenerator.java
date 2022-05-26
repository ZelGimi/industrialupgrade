package com.quantumgenerators;

import com.denfop.componets.QEComponent;
import ic2.api.energy.EnergyNet;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.comp.Energy;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityQuantumGenerator extends TileEntityInventory implements IHasGui, INetworkClientTileEntityEventListener {

    public final String texture;
    public double gen;
    public final QEComponent energy;
    public final double genmax;

    public TileEntityQuantumGenerator(int tier,String texture) {
        this.gen = 5 * Math.pow(4, (tier - 1)) / 16;
        this.genmax = 5 * Math.pow(4, (tier - 1)) / 16;
        this.energy = this.addComponent(QEComponent.asBasicSource(this, gen * 32,
               tier)
        );
        this.texture = texture;

    }
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.gen = nbttagcompound.getDouble("gen");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);

        nbttagcompound.setDouble("gen", this.gen);
        return nbttagcompound;
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        this.energy.setSendingEnabled(true);
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    protected boolean isNormalCube() {
        return false;
    }
    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    protected void updateEntityServer() {
        super.updateEntityServer();

        this.energy.addEnergy(this.gen);
    }
    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        if (i == 0) {
            if (this.gen + 1000 <= this.genmax) {
                this.gen += 1000;
            }else {
                this.gen = this.genmax;
            }
            this.energy.setCapacity(gen * 32);
        } else if (i == 1) {
            if (this.gen + 10000 <= this.genmax) {
                this.gen += 10000;
            }else {
                this.gen = this.genmax;
            }
            this.energy.setCapacity(gen * 32);
        } else if (i == 2) {
            if (this.gen + 100000 <= this.genmax) {
                this.gen += 100000;
            }else {
                this.gen = this.genmax;
            }
            this.energy.setCapacity(gen * 32);
        } else if (i == 3) {
            if (this.gen + 1000000 <= this.genmax) {
                this.gen += 1000000;
            }else {
                this.gen = this.genmax;
            }
            this.energy.setCapacity(gen * 32);
        }
        if (i == 4) {
            if (this.gen - 1000 >= 0) {
                this.gen -= 1000;
            }else {
                this.gen = 0;
            }
            this.energy.setCapacity(gen * 32);
        } else if (i == 5) {
            if (this.gen - 10000 >= 0) {
                this.gen -= 10000;
            }else {
                this.gen = 0;
            }
            this.energy.setCapacity(gen * 32);
        } else if (i == 6) {
            if (this.gen - 100000 >= 0) {
                this.gen -= 100000;
            }else {
                this.gen = 0;
            }
            this.energy.setCapacity(gen * 32);
        } else if (i == 7) {
            if (this.gen - 1000000 >= 0) {
                this.gen -= 1000000;
            }else {
                this.gen = 0;
            }
            this.energy.setCapacity(gen * 32);
        }

        this.energy.setSourceTier(EnergyNet.instance.getTierFromPower( this.gen));
    }

    @Override
    public ContainerQG getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerQG(entityPlayer,this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiQG(getGuiContainer(entityPlayer));
    }

    @Override
    public void onGuiClosed(final EntityPlayer entityPlayer) {

    }



}
