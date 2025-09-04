package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachineEntity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuNeutronGenerator;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryFluid;
import com.denfop.inventory.InventoryFluidByList;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenNeutronGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

;

public class BlockEntityNeutronGenerator extends BlockEntityElectricMachine implements BlockEntityUpgrade,
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

    public BlockEntityNeutronGenerator(BlockPos pos, BlockState state) {
        super((int) (16250000.0D * 128), 14, 1, BlockBaseMachineEntity.neutron_generator, pos, state);

        this.energycost = (float) 16250000.0D / 100;
        this.outputSlot = new InventoryOutput(this, 1);
        this.containerslot = new InventoryFluidByList(
                this,
                Inventory.TypeItemSlot.INPUT,
                1,
                InventoryFluid.TypeFluidSlot.OUTPUT,
                FluidName.fluidneutron.getInstance().get()
        );
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 9 * 1000,
                Fluids.fluidPredicate(FluidName.fluidneutron.getInstance().get()), Inventory.TypeItemSlot.OUTPUT
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

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachineEntity.neutron_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock());
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            this.setUpgradestat();
            energy.setReceivingEnabled(work);
        }

    }

    public void onUnloaded() {


        super.onUnloaded();
    }

    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);
        this.work = nbt.getBoolean("work");
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putBoolean("work", this.work);
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
        this.fluidTank.fill(new FluidStack(FluidName.fluidneutron.getInstance().get(), Math.min(m, k)), IFluidHandler.FluidAction.EXECUTE);
        this.energy.useEnergy(this.energycost * Math.min(m, k));
        return true;
    }

    public String getProgressAsString() {
        int p = Math.min((int) (this.energy.getEnergy() * 100.0D / this.energycost), 100);
        return "" + p + "%";
    }

    public ContainerMenuNeutronGenerator getGuiContainer(Player entityPlayer) {
        return new ContainerMenuNeutronGenerator(entityPlayer, this);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenNeutronGenerator((ContainerMenuNeutronGenerator) menu);
    }


    public void setUpgradestat() {
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
    }

    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(
                EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.FluidExtract,

                EnumBlockEntityUpgrade.ItemExtract
        );
    }


    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
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
