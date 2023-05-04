package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.api.gui.IType;
import com.denfop.api.inv.IHasGui;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.audio.AudioSource;
import com.denfop.audio.PositionSpec;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerPump;
import com.denfop.gui.GuiPump;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotConsumableLiquid;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityElectricLiquidTankInventory;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityPump extends TileEntityElectricLiquidTankInventory implements IHasGui, IUpgradableBlock, IType {

    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final InvSlotConsumableLiquid containerSlot;
    public final InvSlotOutput outputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public double energyConsume;
    public int operationsPerTick;
    public short progress = 0;
    public int operationLength;
    public float guiProgress;
    private AudioSource audioSource;

    public TileEntityPump(int size, int operationLength) {
        super(20, 1, size);
        this.containerSlot = new InvSlotConsumableLiquid(
                this,
                "containerSlot",
                InvSlot.Access.I,
                1,
                InvSlot.InvSide.ANY,
                InvSlotConsumableLiquid.OpType.Fill
        );
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, "upgrade", 4);
        this.defaultEnergyConsume = this.energyConsume = 1;
        this.defaultOperationLength = this.operationLength = operationLength;
        this.defaultTier = 1;
        this.defaultEnergyStorage = this.operationLength;

    }

    private static int applyModifier(int base, int extra, double multiplier) {
        double ret = (double) Math.round(((double) base + (double) extra) * multiplier);
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    private static double applyModifier(double base, double extra, double multiplier) {
        return (double) Math.round((base + extra) * multiplier);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.defaultEnergyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.defaultOperationLength);
        }
        super.addInformation(stack, tooltip, advanced);

    }

    public void onUnloaded() {
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }

        super.onUnloaded();
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.energy.canUseEnergy((this.energyConsume * this.operationLength))) {

            if (this.progress < this.operationLength) {
                ++this.progress;
                this.energy.useEnergy(energyConsume);
            } else {
                if (this.canoperate()) {
                    this.progress = 0;
                }
            }
        }

        MutableObject<ItemStack> output = new MutableObject<>();
        if (this.containerSlot.transferFromTank(
                this.fluidTank,
                output,
                true
        ) && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
            this.containerSlot.transferFromTank(this.fluidTank, output, false);
            if (output.getValue() != null) {
                this.outputSlot.add(output.getValue());
            }
        }


        this.guiProgress = (float) this.progress / (float) this.operationLength;
        this.upgradeSlot.tickNoMark();


    }


    public boolean canoperate() {
        return this.operate(false);
    }

    public boolean operate(boolean sim) {
        FluidStack liquid;
        List<FluidStack> liquid_list = new ArrayList<>();
        for (int i = this.pos.getX() - 3; i < this.pos.getX() + 3; i++) {
            for (int j = this.pos.getZ() - 3; j < this.pos.getZ() + 3; j++) {
                for (int k = this.pos.getY() - 3; k < this.pos.getY() + 3; k++) {
                    for (EnumFacing dir : EnumFacing.values()) {
                        liquid = this.pump(new BlockPos(i + dir.getFrontOffsetX(), k + dir.getFrontOffsetY(),
                                j + dir.getFrontOffsetZ()
                        ), sim);
                        if (liquid != null) {
                            liquid_list.add(liquid);
                        }
                    }
                }
            }
        }

        boolean canoperate = false;
        for (FluidStack stack : liquid_list) {

            if (!sim) {
                if (this.getFluidTank().fill(stack, false) > 0) {
                    this.getFluidTank().fill(stack, true);
                    canoperate = true;
                }
            } else if (this.getFluidTank().fill(stack, false) > 0) {
                return true;
            }


        }
        return canoperate;
    }

    public FluidStack pump(BlockPos pos, boolean sim) {
        FluidStack ret = null;
        int freespace = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();

        if (freespace >= 1000) {
            IBlockState block = this.getWorld().getBlockState(pos);
            if (block.getMaterial().isLiquid()) {


                if (block.getBlock() instanceof IFluidBlock) {
                    IFluidBlock liquid = (IFluidBlock) block.getBlock();
                    if (liquid.canDrain(this.getWorld(), pos)) {
                        if (!sim) {
                            ret = liquid.drain(this.getWorld(), pos, true);
                            this.getWorld().setBlockToAir(pos);
                        } else {
                            ret = new FluidStack(liquid.getFluid(), 1000);
                        }
                    }
                } else {
                    if (block.getBlock().getMetaFromState(block) != 0) {
                        return null;
                    }

                    ret = new FluidStack(FluidRegistry.getFluid(block.getBlock().getUnlocalizedName().substring(5)), 1000);


                    if (!sim) {
                        this.getWorld().setBlockToAir(pos);
                    }
                }
            }
        }


        return ret;
    }


    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            this.setUpgradestat();
        }

    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            this.setUpgradestat();
        }

    }

    public void setUpgradestat() {
        double previousProgress = (double) this.progress / (double) this.operationLength;
        double stackOpLen = ((double) this.defaultOperationLength + (double) this.upgradeSlot.extraProcessTime) * 64.0D * this.upgradeSlot.processTimeMultiplier;
        this.operationsPerTick = (int) Math.min(Math.ceil(64.0D / stackOpLen), 2.147483647E9D);
        this.operationLength = (int) Math.round(stackOpLen * (double) this.operationsPerTick / 64.0D);
        this.energyConsume = applyModifier(this.defaultEnergyConsume, this.upgradeSlot.extraEnergyDemand, 1);
        this.energy.setSinkTier(applyModifier(this.defaultTier, this.upgradeSlot.extraTier, 1.0D));
        this.energy.setCapacity(applyModifier(
                this.defaultEnergyStorage,
                this.upgradeSlot.extraEnergyStorage + this.operationLength * this.energyConsume,
                this.upgradeSlot.energyStorageMultiplier
        ));
        if (this.operationLength < 1) {
            this.operationLength = 1;
        }

        this.progress = (short) ((int) Math.floor(previousProgress * (double) this.operationLength + 0.1D));
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energy.canUseEnergy(amount)) {
            this.energy.useEnergy(amount);
            return true;
        } else {
            return false;
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("progress", this.progress);
        return nbttagcompound;
    }

    public ContainerPump getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerPump(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiPump getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiPump(new ContainerPump(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {
    }

    public void onNetworkUpdate(String field) {
        if (field.equals("active")) {
            if (this.audioSource == null) {
                this.audioSource = IUCore.audioManager.createSource(
                        this,
                        PositionSpec.Center,
                        "Machines/PumpOp.ogg",
                        true,
                        false,
                        IC2.audioManager.getDefaultVolume()
                );
            }

            if (this.getActive()) {
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
            } else if (this.audioSource != null) {
                this.audioSource.stop();
            }
        }

        super.onNetworkUpdate(field);
    }

    public boolean canFill(Fluid fluid) {
        return false;
    }

    public boolean canDrain(Fluid fluid) {
        return true;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing,
                UpgradableProperty.FluidProducing
        );
    }


    @Override
    public EnumTypeStyle getStyle() {
        return null;
    }

}
