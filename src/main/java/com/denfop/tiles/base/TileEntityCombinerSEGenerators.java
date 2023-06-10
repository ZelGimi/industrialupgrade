package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.inv.IHasGui;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerCombinerSE;
import com.denfop.gui.GuiCombinerSE;
import com.denfop.invslot.InvSlotCombinerSEG;
import com.denfop.invslot.InvSlotGenCombinerSunarrium;
import com.denfop.invslot.InvSlotUpgrade;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
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
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityCombinerSEGenerators extends TileEntityInventory implements IHasGui,
        IUpgradableBlock {


    public final InvSlotCombinerSEG inputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotOutput outputSlot;
    public final ComponentBaseEnergy sunenergy;
    public final InvSlotGenCombinerSunarrium input;
    public final ItemStack itemstack = new ItemStack(IUItem.sunnarium, 1, 4);
    public double coef_day;
    public double coef_night;
    public double update_night;
    public int count;
    public List<Double> lst;
    public int coef = 0;
    public double generation;
    private boolean noSunWorld;
    private boolean skyIsVisible;
    private boolean sunIsUp;

    public TileEntityCombinerSEGenerators() {
        this.inputSlot = new InvSlotCombinerSEG(this);
        this.input = new InvSlotGenCombinerSunarrium(this);

        this.outputSlot = new InvSlotOutput(this, "output", 9);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, "upgrade", 4);
        this.sunenergy = this.addComponent(ComponentBaseEnergy
                .asBasicSource(EnergyType.SOLARIUM, this, 0, 1));
        this.lst = new ArrayList<>();
        this.lst.add(0D);
        this.lst.add(0D);
        this.lst.add(0D);
        this.coef_day = 0;
        this.coef_night = 0;
        this.update_night = 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return 4;
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
        this.coef_day = this.lst.get(0);
        this.coef_night = this.lst.get(1);
        this.update_night = this.lst.get(2);
        this.noSunWorld = this.world.provider.isNether();
        updateVisibility();
    }

    public void updateVisibility() {
        this.skyIsVisible = this.world.canBlockSeeSky(this.pos.up()) &&
                (this.world.getBlockState(this.pos.up()).getMaterial().getMaterialMapColor() ==
                        MapColor.AIR) && !this.noSunWorld;
        this.sunIsUp = this.world.isDaytime();
    }

    public void energy(long tick) {
        double k = 0;
        if (this.sunIsUp) {
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
            generation = k * this.coef * (1 + coef_day);
            this.sunenergy.addEnergy(generation);
        }

        if (this.update_night > 0 && !this.sunIsUp) {
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
            generation = k * this.coef * (this.update_night - 1) * (1 + this.coef_night);
            this.sunenergy.addEnergy(generation);

        }

    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        if (this.world.provider.getWorldTime() % 80 == 0) {
            updateVisibility();
        }
        long tick = this.getWorld().provider.getWorldTime() % 24000L;
        generation = 0;
        if (this.skyIsVisible) {
            energy(tick);
            while (this.sunenergy.getEnergy() >= 2500 && this.outputSlot.add(itemstack)) {
                this.sunenergy.addEnergy(-2500);
            }
        }
        this.upgradeSlot.tickNoMark();
    }

    protected void onUnloaded() {
        super.onUnloaded();


    }



    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiCombinerSE(new ContainerCombinerSE(entityPlayer, this));
    }

    public ContainerCombinerSE getGuiContainer(EntityPlayer entityPlayer) {
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


}
