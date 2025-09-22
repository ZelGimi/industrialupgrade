package com.powerutils;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.NodeStats;
import com.denfop.api.sytem.EnergyBase;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.componets.Energy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileQEConverter extends TileEntityInventory implements
        IUpdatableTileEvent, IUpgradableBlock {


    public final Energy energy;
    public final InventoryUpgrade upgradeSlot;
    public final int defaultEnergyRFStorage;
    public final int defaultEnergyStorage;
    public double capacity;
    public double capacity2;
    public double maxStorage2;
    public ComponentBaseEnergy energy2;
    public boolean rf;
    public double differenceenergy = 0;
    public double perenergy1 = 0;
    public double differenceenergy1 = 0;
    public int tier = 5;
    public List<EntityPlayer> list = new ArrayList<>();

    public TileQEConverter() {

        this.rf = true;
        this.energy = this.addComponent((new Energy(this, 40000, ModUtils.allFacings,
                ModUtils.allFacings,
                5,
                5, false
        )));
        this.energy.setDirections(ModUtils.allFacings, ModUtils.allFacings);
        this.energy2 = this.addComponent(new ComponentBaseEnergy(EnergyType.QUANTUM, this, 40000D / 16, ModUtils.allFacings,
                ModUtils.allFacings,
                5,
                5, false
        ));
        this.energy2.setDirections(ModUtils.allFacings, ModUtils.allFacings);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.defaultEnergyStorage = 40000;
        this.defaultEnergyRFStorage = 40000 / 16;
        this.capacity = this.energy.getCapacity();
        this.capacity2 = this.energy2.getCapacity();
    }

    public IMultiTileBlock getTeBlock() {
        return BlockPowerConverter.power_utilities_qe;
    }

    public BlockTileEntity getBlock() {
        return PowerUtils.itemPowerConverter;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (this.hasComp(Energy.class)) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            rf = (boolean) DecoderHandler.decode(customPacketBuffer);
            tier = (int) DecoderHandler.decode(customPacketBuffer);
            differenceenergy1 = (double) DecoderHandler.decode(customPacketBuffer);
            differenceenergy = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, rf);
            EncoderHandler.encode(packet, tier);
            EncoderHandler.encode(packet, differenceenergy1);
            EncoderHandler.encode(packet, differenceenergy);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            this.setOverclockRates();
            this.energy.setDirections(ModUtils.allFacings, ModUtils.allFacings);
            this.energy2.setDirections(ModUtils.allFacings, ModUtils.allFacings);
        }


    }

    public void setOverclockRates() {
        int tier = this.upgradeSlot.getTier(5);
        this.energy.setSinkTier(tier);
        this.energy.setSourceTier(tier);
        this.energy.setCapacity(this.upgradeSlot.extraEnergyStorage +
                this.defaultEnergyStorage
        );
        this.energy2.setSinkTier(tier);
        this.energy2.setSourceTier(tier);
        this.energy2.setCapacity((this.upgradeSlot.extraEnergyStorage +
                this.defaultEnergyStorage) / 16D
        );
        this.tier = tier;
        this.capacity = this.energy.capacity;
        this.capacity2 = this.energy2.capacity;
    }

    public void markDirty() {
        super.markDirty();
        if (IUCore.proxy.isSimulating()) {
            this.setOverclockRates();
        }

    }


    public void updateEntityServer() {
        super.updateEntityServer();
        this.differenceenergy = 0;
        this.differenceenergy1 = 0;
        this.energy.setReceivingEnabled(!this.shouldEmitEnergy());
        this.energy.setSendingEnabled(this.shouldEmitEnergy());
        this.energy2.setReceivingEnabled(this.shouldEmitEnergy());
        this.energy2.setSendingEnabled(!this.shouldEmitEnergy());
        if (this.rf) {
            if (energy.getEnergy() > 0 && energy2.getEnergy() < energy2.getCapacity()) {
                double add = Math.min(energy2.getFreeEnergy(), energy.getEnergy() / 16);
                add = Math.max(add, 0);
                energy2.addEnergy(add);
                energy.useEnergy(add * 16);
            }
        } else {

            if (energy2.getEnergy() > 0 && energy.getEnergy() < energy.getCapacity()) {
                double add = Math.min(energy.getFreeEnergy(), energy2.getEnergy() * 10);
                energy.addEnergy(add);
                energy2.useEnergy(add / 10);
            }

        }
        if (this.energy2.getEnergy() > 0) {
            if (!this.list.isEmpty()) {
                if (this.rf) {
                    NodeStats stats = EnergyNetGlobal.instance.getNodeStats(this.energy.getDelegate());
                    NodeStats stats1 = EnergyBase.QE.getNodeStats(this.energy2.getDelegate(), this.world);

                    if (stats != null) {
                        this.differenceenergy1 = stats.getEnergyIn();
                    }
                    if (stats1 != null) {
                        this.differenceenergy = stats1.getEnergyOut();
                    }

                } else {
                    this.perenergy1 = this.energy.getEnergy();
                    NodeStats stats = EnergyNetGlobal.instance.getNodeStats(this.energy.getDelegate());
                    NodeStats stats1 = EnergyBase.QE.getNodeStats(this.energy2.getDelegate(), this.world);

                    if (stats != null) {
                        this.differenceenergy = stats.getEnergyOut();
                    }
                    if (stats1 != null) {
                        this.differenceenergy1 = stats1.getEnergyIn();
                    }

                }
            }
        }


        final boolean needsInvUpdate = this.upgradeSlot.tickNoMark();
        if (needsInvUpdate) {
            setOverclockRates();
        }

    }

    protected boolean shouldEmitEnergy() {

        return !this.rf;

    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

        this.tier = nbttagcompound.getInteger("tier");

        this.rf = nbttagcompound.getBoolean("rf");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);

        nbttagcompound.setInteger("tier", this.tier);
        nbttagcompound.setBoolean("rf", this.rf);
        return nbttagcompound;
    }

    public int getCapacity() {
        return (int) this.energy.getCapacity();
    }


    public int getOutput() {
        return (int) EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSourceTier());
    }

    public double getOutputEnergyUnitsPerTick() {
        return EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSourceTier());
    }


    public void setStored(int energy1) {

    }

    public int addEnergy(int amount) {
        this.energy.addEnergy(amount);
        return amount;
    }


    public boolean isTeleporterCompatible(EnumFacing side) {
        return true;
    }

    public ContainerQEConverter getGuiContainer(EntityPlayer player) {
        list.add(player);
        return new ContainerQEConverter(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiQEConverter(getGuiContainer(entityPlayer));
    }


    public void updateTileServer(EntityPlayer player, double event) {
        this.rf = !this.rf;

    }

    public int gaugeICEnergyScaled(int i) {
        return (int) Math.min(this.energy.getEnergy() * i / this.energy.getCapacity(), i);
    }

    public int gaugeTEEnergyScaled(int i) {
        return (int) Math.min(this.energy2.getEnergy() * i / this.energy2.getCapacity(), i);
    }


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage
        );
    }


}
