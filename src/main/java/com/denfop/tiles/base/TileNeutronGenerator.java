package com.denfop.tiles.base;

import com.denfop.Config;
import com.denfop.IUItem;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerNeutronGenerator;
import com.denfop.gui.GuiNeutronGenerator;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
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

    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotOutput outputSlot;
    public final InvSlotFluid containerslot;
    public final FluidTank fluidTank;
    protected final Fluids fluids;
    private final float energycost;
    public boolean work = true;

    public TileNeutronGenerator() {
        super((int) (Config.energy * 128), 14, 1);

        this.energycost = (float) Config.energy / 100;
        this.outputSlot = new InvSlotOutput(this, 1);
        this.containerslot = new InvSlotFluidByList(
                this,
                InvSlot.TypeItemSlot.INPUT,
                1,
                InvSlotFluid.TypeFluidSlot.OUTPUT,
                FluidName.fluidNeutron.getInstance()
        );
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 9 * 1000,
                Fluids.fluidPredicate(FluidName.fluidNeutron.getInstance())
        );

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
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing,
                UpgradableProperty.FluidProducing
        );
    }


    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        if (i != 10) {
            this.work = !this.work;
        } else {
            super.updateTileServer(entityPlayer, i);
        }
    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

}
