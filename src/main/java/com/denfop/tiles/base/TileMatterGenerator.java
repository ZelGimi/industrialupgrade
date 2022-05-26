package com.denfop.tiles.base;

import com.denfop.Config;
import com.denfop.container.ContainerSolidMatter;
import com.denfop.gui.GUISolidMatter;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.comp.Energy;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotUpgrade;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public abstract class TileMatterGenerator extends TileEntityInventory implements IHasGui,
        IUpgradableBlock {

    public final InvSlotOutput outputSlot;
    public final ItemStack itemstack;
    public final InvSlotUpgrade upgradeSlot;
    private final Energy energy;
    private final int defaultTier;
    private final String name;
    public AudioSource audioSource;
    private double progress;

    public TileMatterGenerator(ItemStack itemstack, String name) {
        this.itemstack = itemstack;
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.progress = 0;
        defaultTier = 10;
        this.name = name;
        this.energy = this.addComponent(Energy.asBasicSink(this, Config.SolidMatterStorage, 10));

    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getDouble("progress");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("progress", this.progress);
        return nbttagcompound;
    }

    protected void initiate(int soundEvent) {
        IC2.network.get(true).initiateTileEntityEvent(this, soundEvent, true);
    }


    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate = false;
        if (this.energy.getEnergy() > 0) {
            this.progress = this.energy.getEnergy() / this.energy.getCapacity();
            if (this.energy.getEnergy() >= this.energy.getCapacity()) {
                if (this.outputSlot.canAdd(itemstack)) {
                    this.outputSlot.add(itemstack);
                    this.energy.useEnergy(this.energy.getCapacity());
                    this.progress = 0;
                }
            }

        }
        for (int i = 0; i < this.upgradeSlot.size(); i++) {
            ItemStack stack = this.upgradeSlot.get(i);
            if (stack != null && stack.getItem() instanceof IUpgradeItem && (
                    (IUpgradeItem) stack.getItem()).onTick(stack, this)) {
                needsInvUpdate = true;
            }
        }

        if (needsInvUpdate && this.getWorld().provider.getWorldTime() % 10 == 0) {
            markDirty();
        }

    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUISolidMatter(new ContainerSolidMatter(entityPlayer, this));
    }

    public ContainerBase<? extends TileMatterGenerator> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSolidMatter(entityPlayer, this);
    }


    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }


    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        return this.energy.useEnergy(amount);
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.ItemProducing
        );
    }

    public void onGuiClosed(EntityPlayer player) {
    }


    public final float getChargeLevel() {
        return Math.min((float) this.energy.getEnergy() / (float) this.energy.getCapacity(), 1);
    }

    public double getProgress() {
        return this.progress;
    }

    public int getMode() {
        return 0;
    }

    public String getInventoryName() {
        return this.name;
    }


}
