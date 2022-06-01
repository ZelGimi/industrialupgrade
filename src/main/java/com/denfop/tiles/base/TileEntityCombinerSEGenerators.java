package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.componets.SEComponent;
import com.denfop.container.ContainerCombinerSE;
import com.denfop.gui.GuiCombinerSE;
import com.denfop.invslot.InvSlotCombinerSEG;
import com.denfop.invslot.InvSlotGenCombinerSunarrium;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityCombinerSEGenerators extends TileEntityInventory implements IHasGui, INetworkTileEntityEventListener,
        IUpgradableBlock {


    public final InvSlotCombinerSEG inputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotOutput outputSlot;
    public final SEComponent sunenergy;
    public final InvSlotGenCombinerSunarrium input;
    public final ItemStack itemstack = new ItemStack(IUItem.sunnarium, 1, 4);
    public int count;
    public List<Double> lst;
    public int coef = 0;

    public TileEntityCombinerSEGenerators() {
        this.inputSlot = new InvSlotCombinerSEG(this);
        this.input = new InvSlotGenCombinerSunarrium(this);

        this.outputSlot = new InvSlotOutput(this, "output", 9);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.sunenergy = this.addComponent(SEComponent
                .asBasicSource(this, 0, 1));
        this.lst = new ArrayList<>();
        this.lst.add(0D);
        this.lst.add(0D);
        this.lst.add(0D);
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


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }


    protected void onLoaded() {
        super.onLoaded();
        this.inputSlot.update();
        this.lst = this.input.coefday();
    }


    public boolean onUpdateUpgrade() {
        for (int i = 0; i < this.upgradeSlot.size(); i++) {
            ItemStack stack = this.upgradeSlot.get(i);
            if (!stack.isEmpty()) {
                return true;
            }
        }

        return false;
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

            this.sunenergy.addEnergy(k * this.coef * (1 + lst.get(0)));
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

            this.sunenergy.addEnergy(k * this.coef * (lst.get(2) - 1) * (1 + lst.get(1)));

        }

    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean update = onUpdateUpgrade();
        long tick = this.getWorld().provider.getWorldTime() % 24000L;
        energy(tick);
        while (this.outputSlot.canAdd(itemstack) && this.sunenergy.getEnergy() >= 2500) {
            this.outputSlot.add(itemstack);
            this.sunenergy.addEnergy(-2500);
        }

        if (update) {
            markDirty();
        }


    }

    protected void onUnloaded() {
        super.onUnloaded();


    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
        }
    }

    public void setOverclockRates() {
        this.upgradeSlot.onChanged();

    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiCombinerSE(new ContainerCombinerSE(entityPlayer, this));
    }

    public ContainerBase<? extends TileEntityCombinerSEGenerators> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerCombinerSE(entityPlayer, this);
    }


    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }


    public double getEnergy() {
        return 0;
    }

    public boolean useEnergy(double amount) {
        return false;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.ItemProducing
        );
    }

    public void onGuiClosed(EntityPlayer player) {
    }


    public String getInventoryName() {
        return null;
    }


    @Override
    public void onNetworkEvent(final int i) {

    }

}
