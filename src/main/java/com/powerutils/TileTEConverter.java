package com.powerutils;

import com.denfop.Config;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.NodeStats;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.componets.Energy;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import com.powerutils.handler.TeslaHelper;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileTEConverter extends TileEntityInventory implements
        IUpdatableTileEvent,
        IEnergyStorage, ITeslaConsumer, ITeslaProducer, IUpgradableBlock {


    public final Energy energy;
    public final InvSlotUpgrade upgradeSlot;
    public final int defaultEnergyRFStorage;
    public final int defaultEnergyStorage;
    public double capacity;
    public double maxStorage2;
    public double energy2;
    public boolean rf;
    public double differenceenergy = 0;
    public double perenergy = 0;
    public double differenceenergy1 = 0;
    public int tier = 5;
    public List<EntityPlayer> list = new ArrayList<>();
    private long tick;

    public TileTEConverter() {
        this.energy2 = 0.0D;
        this.maxStorage2 = 400000;
        this.rf = true;
        this.energy = this.addComponent((new Energy(this, 40000, ModUtils.allFacings,
                ModUtils.allFacings,
                5,
                5, false
        )));
        this.energy.setDirections(ModUtils.allFacings, ModUtils.allFacings);
        this.upgradeSlot = new InvSlotUpgrade(this, 4);
        this.defaultEnergyStorage = 40000;
        this.defaultEnergyRFStorage = 400000;
        this.capacity = this.energy.capacity;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockPowerConverter.power_utilities_te;
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

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            this.setOverclockRates();
        }

    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            energy2 = (double) DecoderHandler.decode(customPacketBuffer);
            capacity = (double) DecoderHandler.decode(customPacketBuffer);
            maxStorage2 = (double) DecoderHandler.decode(customPacketBuffer);
            perenergy = (double) DecoderHandler.decode(customPacketBuffer);
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
            EncoderHandler.encode(packet, energy2);
            EncoderHandler.encode(packet, capacity);
            EncoderHandler.encode(packet, maxStorage2);
            EncoderHandler.encode(packet, perenergy);
            EncoderHandler.encode(packet, rf);
            EncoderHandler.encode(packet, tier);
            EncoderHandler.encode(packet, differenceenergy1);
            EncoderHandler.encode(packet, differenceenergy);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public boolean hasCapability(@Nonnull Capability<?> cap, @Nullable EnumFacing side) {
        return cap == CapabilityEnergy.ENERGY || super.hasCapability(cap, side);
    }

    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing side) {
        if (TeslaHelper.isTeslaCapability(capability)) {
            final Capability<ITeslaProducer> mjConnector = TeslaHelper.TESLA_PRODUCER;
            final Capability<ITeslaConsumer> teslaConsumer = TeslaHelper.TESLA_CONSUMER;
            if (capability == mjConnector && canExtract()) {
                return mjConnector.cast(this);
            }

            if (capability == teslaConsumer && canReceive()) {
                return teslaConsumer.cast(this);
            }

            return super.getCapability(capability, side);
        }
        return capability == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(this) : super.getCapability(capability, side);
    }

    public boolean canExtract() {
        return extractEnergy(1, true) != 0;
    }

    public boolean canReceive() {
        return receiveEnergy(1, true) != 0;
    }

    public void setOverclockRates() {
        int tier = this.upgradeSlot.getTier(5);
        this.energy.setSinkTier(tier);
        this.energy.setSourceTier(tier);
        this.energy.setCapacity(this.upgradeSlot.extraEnergyStorage +
                this.defaultEnergyStorage
        );
        this.maxStorage2 = this.defaultEnergyRFStorage + this.upgradeSlot.extraEnergyStorage * Config.coefficientrf;
        this.tier = tier;
        this.capacity = this.energy.capacity;
    }

    public void markDirty() {
        super.markDirty();
        if (IUCore.proxy.isSimulating()) {
            this.setOverclockRates();
        }

    }

    public int receiveEnergy(int paramInt, boolean paramBoolean) {
        if (this.rf) {
            return 0;
        }
        int i = (int) Math.min(
                this.maxStorage2 - this.energy2,
                paramInt
        );
        if (!paramBoolean) {
            this.energy2 += i;
            this.differenceenergy1 = i;
            if (this.tick != this.getWorld().getWorldTime()) {
                this.tick = this.getWorld().getWorldTime();
                this.perenergy = i;
            } else {
                this.perenergy += i;
            }
        }
        return i;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        this.energy.setReceivingEnabled(!this.shouldEmitEnergy());
        this.energy.setSendingEnabled(this.shouldEmitEnergy());
        this.differenceenergy = 0;
        this.differenceenergy1 = 0;
        if (this.tick != this.getWorld().getWorldTime()) {
            this.perenergy = 0;
        }
        if (this.rf) {
            if (energy.getEnergy() > 0 && energy2 < maxStorage2) {
                double add = Math.min(maxStorage2 - energy2, energy.getEnergy() * Config.coefficientrf);
                add = Math.max(add, 0);
                energy2 += add;
                energy.useEnergy(add / Config.coefficientrf);
            }
        } else {

            if (energy2 > 0 && energy.getEnergy() < energy.getCapacity()) {
                double k = energy.addEnergy(energy2 / Config.coefficientrf) * Config.coefficientrf;
                energy2 -= k;
            }

        }
        if (!this.list.isEmpty()) {
            NodeStats stats = EnergyNetGlobal.instance.getNodeStats(this.energy.getDelegate());
            if (this.rf) {
                if (stats != null) {
                    this.differenceenergy1 = stats.getEnergyIn();
                }
            } else {
                if (stats != null) {
                    this.differenceenergy = stats.getEnergyOut();
                }

            }
        }
        int transfer = (int) Math.min(this.energy2, EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSourceTier()));
        this.energy2 -= transfer;
        this.energy2 += this.transmitEnergy(transfer);

        final boolean needsInvUpdate = this.upgradeSlot.tickNoMark();
        if (needsInvUpdate) {
            setOverclockRates();
        }
        this.energy2 = Math.min(this.energy2, this.maxStorage2);

    }

    private int transmitEnergy(int energy) {
        EnumFacing[] var2 = EnumFacing.values();

        for (EnumFacing e : var2) {
            BlockPos neighbor = this.getBlockPos().offset(e);
            if (this.world.isBlockLoaded(neighbor)) {
                TileEntity te = this.world.getTileEntity(neighbor);
                if (te != null) {
                    net.minecraftforge.energy.IEnergyStorage storage = null;
                    if (te.hasCapability(CapabilityEnergy.ENERGY, e.getOpposite())) {
                        storage = te.getCapability(
                                CapabilityEnergy.ENERGY,
                                e.getOpposite()
                        );
                    } else if (te.hasCapability(CapabilityEnergy.ENERGY, null)) {
                        storage = te.getCapability(
                                CapabilityEnergy.ENERGY,
                                null
                        );
                    }

                    if (storage != null) {
                        energy -= storage.receiveEnergy(energy, false);
                        if (energy <= 0) {
                            return 0;
                        }
                    }
                }
            }
        }

        return energy;
    }

    protected boolean shouldEmitEnergy() {

        return !this.rf;

    }

    public int extractEnergy(int paramInt, boolean paramBoolean) {
        if (!this.rf) {
            return 0;
        }
        int i = (int) Math.min(this.energy2, paramInt);
        if (!paramBoolean) {
            this.energy2 -= i;
            this.differenceenergy += i;
        }
        return i;
    }


    public boolean canConnectEnergy() {
        return true;
    }

    public int getEnergyStored() {
        return (int) this.energy2;
    }

    public int getMaxEnergyStored() {
        return (int) this.maxStorage2;
    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.energy.setDirections(ModUtils.allFacings, ModUtils.allFacings);
        this.tier = nbttagcompound.getInteger("tier");

        this.energy2 = ModUtils.limit(nbttagcompound.getDouble("energy2"), 0.0D,
                this.maxStorage2
        );
        this.rf = nbttagcompound.getBoolean("rf");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (energy2 > 0) {
            nbttagcompound.setDouble("energy2", this.energy2);
        }
        nbttagcompound.setInteger("tier", this.tier);
        nbttagcompound.setBoolean("rf", this.rf);
        return nbttagcompound;
    }


    public int getOutput() {
        return (int) EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSourceTier());
    }


    public int addEnergy(int amount) {
        this.energy.addEnergy(amount);
        return amount;
    }


    public ContainerTEConverter getGuiContainer(EntityPlayer player) {
        list.add(player);
        return new ContainerTEConverter(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiTEConverter(getGuiContainer(entityPlayer));
    }


    public void updateTileServer(EntityPlayer player, double event) {
        this.rf = !this.rf;

    }

    public int gaugeICEnergyScaled(int i) {
        return (int) Math.min(this.energy.getEnergy() * i / this.energy.getCapacity(), i);
    }

    public int gaugeTEEnergyScaled(int i) {
        this.maxStorage2 = this.defaultEnergyRFStorage + this.upgradeSlot.extraEnergyStorage * Config.coefficientrf;
        return (int) Math.min(this.energy2 * i / this.maxStorage2, i);
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage
        );
    }


    @Override
    public long givePower(final long l, final boolean b) {
        return receiveEnergy((int) l, b);
    }

    @Override
    public long takePower(final long l, final boolean b) {
        return extractEnergy((int) l, b);
    }

}
