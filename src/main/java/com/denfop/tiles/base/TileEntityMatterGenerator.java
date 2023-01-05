package com.denfop.tiles.base;

import com.denfop.Config;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerSolidMatter;
import com.denfop.gui.GuiSolidMatter;
import com.denfop.invslot.InvSlotUpgrade;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class TileEntityMatterGenerator extends TileEntityInventory implements IHasGui,
        IUpgradableBlock {

    public final InvSlotOutput outputSlot;
    public final ItemStack itemstack;
    public final InvSlotUpgrade upgradeSlot;
    private final AdvEnergy energy;
    private final String name;
    public AudioSource audioSource;
    private double progress;

    public TileEntityMatterGenerator(ItemStack itemstack, String name) {
        this.itemstack = itemstack;
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, "upgrade", 4);
        this.progress = 0;
        this.name = name;
        this.energy = this.addComponent(AdvEnergy.asBasicSink(this, Config.SolidMatterStorage, 10));

    }

    private static int applyModifier(int extra) {
        double ret = (double) Math.round(((double) 10 + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.matter_solid_work_info") + (int) Config.SolidMatterStorage);
        }
        if (this.hasComponent(AdvEnergy.class)) {
            AdvEnergy energy = this.getComponent(AdvEnergy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }

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


        if (this.upgradeSlot.tickNoMark()) {
            setUpgradestat();
        }

    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiSolidMatter(new ContainerSolidMatter(entityPlayer, this));
    }

    public ContainerBase<? extends TileEntityMatterGenerator> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSolidMatter(entityPlayer, this);
    }

    public void setUpgradestat() {
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
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
