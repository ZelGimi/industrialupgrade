package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.componets.SEComponent;
import com.denfop.container.ContainerSolarGeneratorEnergy;
import com.denfop.gui.GuiSolarGeneratorEnergy;
import com.denfop.invslot.InvSlotGenSunarrium;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotOutput;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntitySolarGeneratorEnergy extends TileEntityInventory implements IHasGui,
        INetworkClientTileEntityEventListener {

    public final InvSlotGenSunarrium input;
    public final InvSlotOutput outputSlot;
    public final ItemStack itemstack = new ItemStack(IUItem.sunnarium, 1, 4);
    public final double maxSunEnergy;
    public final double cof;
    public boolean work;
    public SEComponent sunenergy;
    public List<Double> lst;

    public TileEntitySolarGeneratorEnergy(double cof) {

        this.maxSunEnergy = 2500;
        this.cof = cof;
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.input = new InvSlotGenSunarrium(this);
        this.work = true;
        this.lst = new ArrayList<>();
        this.lst.add(0D);
        this.lst.add(0D);
        this.lst.add(0D);
        this.sunenergy = this.addComponent(SEComponent
                .asBasicSource(this, 10000, 1));
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("sunenergy");

        return ret;

    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        this.lst = this.input.coefday();
    }

    public void updateEntityServer() {

        super.updateEntityServer();

        long tick = this.getWorld().provider.getWorldTime() % 24000L;
        if (this.world.canBlockSeeSky(this.pos.up()) &&
                (this.world.getBlockState(this.pos.up()).getMaterial().getMaterialMapColor() ==
                        MapColor.AIR)) {
            energy(tick);
            if (this.sunenergy.getEnergy() >= 2500 && this.work) {
                if (this.outputSlot.get(0).stackSize < 64) {
                    if (this.outputSlot.canAdd(itemstack)) {
                        this.outputSlot.add(itemstack);
                        this.sunenergy.addEnergy(-2500);
                    }
                }
            }
        }

    }

    public void energy(long tick) {
        double k = 0;
        if (this.getWorld().provider.isDaytime()) {
            if (tick <= 1000L) {
                k = 5;
            }
            if (tick > 1000L && tick <= 4000L) {
                k = 10;
            }
            if (tick > 4000L && tick <= 8000L) {
                k = 30;
            }
            if (tick > 8000L && tick <= 11000L) {
                k = 10;
            }
            if (tick > 11000L) {
                k = 5;
            }
            this.sunenergy.addEnergy(k * this.cof * (1 + lst.get(0)));
        }

        if (lst.get(2) > 0 && !this.getWorld().provider.isDaytime()) {
            double tick1 = tick - 12000;
            if (tick1 <= 1000L) {
                k = 5;
            }
            if (tick1 > 1000L && tick1 <= 4000L) {
                k = 10;
            }
            if (tick1 > 4000L && tick1 <= 8000L) {
                k = 30;
            }
            if (tick1 > 8000L && tick1 <= 11000L) {
                k = 10;
            }
            if (tick1 > 11000L) {
                k = 5;
            }
            this.sunenergy.addEnergy(k * this.cof * (lst.get(2) - 1) * (1 + lst.get(1)));

        }

    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.work = nbttagcompound.getBoolean("work");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("work", this.work);
        return nbttagcompound;
    }


    @Override
    public void onGuiClosed(EntityPlayer arg0) {
    }


    public ContainerBase<? extends TileEntitySolarGeneratorEnergy> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSolarGeneratorEnergy(entityPlayer, this);
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiSolarGeneratorEnergy(new ContainerSolarGeneratorEnergy(entityPlayer, this));
    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        this.work = !this.work;
    }

}
