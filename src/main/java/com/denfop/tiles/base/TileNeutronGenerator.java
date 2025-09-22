package com.denfop.tiles.base;

import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.Redstone;
import com.denfop.componets.RedstoneHandler;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerNeutronGenerator;
import com.denfop.gui.GuiNeutronGenerator;
import com.denfop.invslot.*;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.invslot.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

public class TileNeutronGenerator extends TileElectricMachine implements IUpgradableBlock,
        IUpdatableTileEvent {

    public final InventoryUpgrade upgradeSlot;
    public final InventoryOutput outputSlot;
    public final InventoryFluid containerslot;
    public final FluidTank fluidTank;
    protected final Fluids fluids;
    private final float energycost;
    private final Redstone redstone;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public boolean work = true;

    public TileNeutronGenerator() {
        super((int) (Config.energy * 128), 14, 1);

        this.energycost = (float) Config.energy / 100;
        this.outputSlot = new InventoryOutput(this, 1);
        this.containerslot = new InventoryFluidByList(
                this,
                Inventory.TypeItemSlot.INPUT,
                1,
                InventoryFluid.TypeFluidSlot.OUTPUT,
                FluidName.fluidNeutron.getInstance()
        );
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 9 * 1000,
                Fluids.fluidPredicate(FluidName.fluidNeutron.getInstance()), Inventory.TypeItemSlot.OUTPUT
        );
        this.redstone = this.addComponent(new Redstone(this));
        this.redstone.subscribe(new RedstoneHandler() {
                                    @Override
                                    public void action(final int input) {
                                        energy.setEnabled(input == 0);
                                        work = input != 0;
                                        energy.setReceivingEnabled(work);
                                    }
                                }
        );
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.005));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.05));
        this.upgradeSlot = new InventoryUpgrade(this, 4);
    }

    private static int applyModifier(int extra) {
        double ret = (double) Math.round(((double) 14 + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            work = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, work);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.neutron_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            this.setUpgradestat();
            energy.setReceivingEnabled(work);
        }

    }

    public void onUnloaded() {


        super.onUnloaded();
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.work = nbt.getBoolean("work");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("work", this.work);
        return nbt;
    }

    public void updateEntityServer() {
        super.updateEntityServer();

        boolean needsInvUpdate;
        if (!this.containerslot.isEmpty()) {
            this.containerslot.processFromTank(this.fluidTank, this.outputSlot);
        }
        if (this.work && this.energy.getEnergy() >= this.energycost) {

            if (!this.getActive()) {
                this.setActive(true);
            }

            needsInvUpdate = this.attemptGeneration();


            if (needsInvUpdate && this.upgradeSlot.tickNoMark()) {
                setUpgradestat();
            }
        } else {
            if (this.getActive()) {
                this.setActive(false);
            }
        }
        this.upgradeSlot.tickNoMark();

    }

    public boolean attemptGeneration() {
        int k = (int) (this.energy.getEnergy() / this.energycost);
        int m;

        if (this.fluidTank.getFluidAmount() + 1 > this.fluidTank.getCapacity()) {
            return false;
        }
        m = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();
        this.fluidTank.fillInternal(new FluidStack(FluidName.fluidNeutron.getInstance(), Math.min(m, k)), true);
        this.energy.useEnergy(this.energycost * Math.min(m, k));
        return true;
    }

    public String getProgressAsString() {
        int p = Math.min((int) (this.energy.getEnergy() * 100.0D / this.energycost), 100);
        return "" + p + "%";
    }

    public ContainerNeutronGenerator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerNeutronGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiNeutronGenerator getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiNeutronGenerator(new ContainerNeutronGenerator(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer player) {
    }


    public void setUpgradestat() {
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.FluidExtract,

                UpgradableProperty.ItemExtract
        );
    }


    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        if (i != 10) {
            this.work = !this.work;
            energy.setReceivingEnabled(work);
        } else {
            super.updateTileServer(entityPlayer, i);
        }
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

}
