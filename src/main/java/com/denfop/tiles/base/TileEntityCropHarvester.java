package com.denfop.tiles.base;

import com.denfop.api.inv.IHasGui;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerCropHarvester;
import com.denfop.gui.GuiCropHarvester;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotUpgrade;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.crop.TileEntityCrop;
import ic2.core.profile.NotClassic;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@NotClassic
public class TileEntityCropHarvester extends TileEntityElectricMachine implements IHasGui, IUpgradableBlock {

    public final InvSlot contentSlot;
    public final InvSlotUpgrade upgradeSlot;
    public int scanX = -4;
    public int scanY = -1;
    public int scanZ = -4;

    public TileEntityCropHarvester() {
        super(10000, 1, 0);
        this.contentSlot = new InvSlot(this, "content", InvSlot.Access.IO, 15);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        this.upgradeSlot.tickNoMark();
        if (this.world.getTotalWorldTime() % 10L == 0L && this.energy.getEnergy() >= 21.0) {
            this.scan();
        }

    }

    public void scan() {
        ++this.scanX;
        if (this.scanX > 4) {
            this.scanX = -4;
            ++this.scanZ;
            if (this.scanZ > 4) {
                this.scanZ = -4;
                ++this.scanY;
                if (this.scanY > 1) {
                    this.scanY = -1;
                }
            }
        }

        this.energy.useEnergy(1.0);
        World world = this.getWorld();
        TileEntity tileEntity = world.getTileEntity(this.pos.add(this.scanX, this.scanY, this.scanZ));
        if (tileEntity instanceof TileEntityCrop && !this.isInvFull()) {
            TileEntityCrop crop = (TileEntityCrop) tileEntity;
            if (crop.getCrop() != null) {
                List<ItemStack> drops = null;
                if (crop.getCurrentSize() == crop.getCrop().getOptimalHarvestSize(crop)) {
                    drops = crop.performHarvest();
                } else if (crop.getCurrentSize() == crop.getCrop().getMaxSize()) {
                    drops = crop.performHarvest();
                }

                if (drops != null) {
                    drops.forEach((drop) -> {
                        if (StackUtil.putInInventory(this, EnumFacing.WEST, drop, true) == 0) {
                            StackUtil.dropAsEntity(world, this.pos, drop);
                        } else {
                            StackUtil.putInInventory(this, EnumFacing.WEST, drop, false);
                        }

                        this.energy.useEnergy(20.0);
                    });
                }
            }
        }

    }

    private boolean isInvFull() {
        for (int i = 0; i < this.contentSlot.size(); ++i) {
            ItemStack stack = this.contentSlot.get(i);
            if (StackUtil.isEmpty(stack) || StackUtil.getSize(stack) < Math.min(
                    stack.getMaxStackSize(),
                    this.contentSlot.getStackSizeLimit()
            )) {
                return false;
            }
        }

        return true;
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        return this.energy.useEnergy(amount);
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer, UpgradableProperty.EnergyStorage, UpgradableProperty.ItemProducing);
    }

    public ContainerBase<TileEntityCropHarvester> getGuiContainer(EntityPlayer player) {
        return new ContainerCropHarvester(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiCropHarvester(new ContainerCropHarvester(player, this));
    }

    public void onGuiClosed(EntityPlayer player) {
    }

}
