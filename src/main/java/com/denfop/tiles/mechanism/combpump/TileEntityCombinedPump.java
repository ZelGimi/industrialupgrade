package com.denfop.tiles.mechanism.combpump;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerCombPump;
import com.denfop.gui.GuiCompPump;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricLiquidTankInventory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityCombinedPump extends TileElectricLiquidTankInventory implements IUpgradableBlock, IType {

    public final int defaultTier;
    public final double defaultEnergyStorage;
    public final double defaultEnergyConsume;
    public final int defaultOperationLength;
    public final InvSlotFluid containerSlot;
    public final InvSlotOutput outputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final ComponentBaseEnergy energyQe;
    public final EnumTypePump typePump;
    public double energyConsume;
    public int operationsPerTick;
    public short progress = 0;
    public int operationLength;
    public float guiProgress;
    public int x;
    public int z;
    public int y;
    boolean canWork = true;

    public TileEntityCombinedPump(int size, int operationLength, EnumTypePump typePump) {
        super(0, 0, size);
        this.containerSlot = new InvSlotFluid(
                this,
                InvSlot.TypeItemSlot.INPUT,
                1,
                InvSlotFluid.TypeFluidSlot.OUTPUT
        );
        this.typePump = typePump;
        this.energyQe = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 100));
        this.outputSlot = new InvSlotOutput(this, 1);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.defaultEnergyConsume = this.energyConsume = (this.getStyle().ordinal() + 1);
        this.defaultOperationLength = this.operationLength = operationLength;
        this.defaultTier = 1;
        this.defaultEnergyStorage = this.operationLength;
        this.fluidTank.setTypeItemSlot(InvSlot.TypeItemSlot.OUTPUT);

    }

    private static int applyModifier(int base, int extra, double multiplier) {
        double ret = (double) Math.round(((double) base + (double) extra) * multiplier);
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    private static double applyModifier(double base, double extra, double multiplier) {
        return (double) Math.round((base + extra) * multiplier);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            guiProgress = (float) DecoderHandler.decode(customPacketBuffer);
            x = customPacketBuffer.readInt();
            y = customPacketBuffer.readInt();
            z = customPacketBuffer.readInt();
            canWork = customPacketBuffer.readBoolean();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ContainerCombPump getGuiContainer(final EntityPlayer var1) {
        return new ContainerCombPump(var1, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiCompPump(getGuiContainer(var1));
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, guiProgress);
            packet.writeInt(this.x);
            packet.writeInt(this.y);
            packet.writeInt(this.z);
            packet.writeBoolean(this.canWork);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void updateEntityServer() {
        super.updateEntityServer();
        if (canWork && this.energyQe.canUseEnergy((this.energyConsume))) {

            if (this.progress < this.operationLength) {
                ++this.progress;
                this.energyQe.useEnergy(energyConsume);
            } else {
                for (int i = 0; i < operationsPerTick; i++) {
                    if (x < this.getPos().getX() + this.typePump.getXz()) {
                        x++;
                    } else if (z < this.getPos().getZ() + this.typePump.getXz()) {
                        z++;
                        x = this.getPos().getX();
                    } else if (y >= this.getPos().getY() - this.typePump.getY()) {
                        z = this.getPos().getZ();
                        x = this.getPos().getX();
                        y--;
                    } else {
                        canWork = false;
                    }
                    if (this.operate(x, y, z)) {
                        this.progress = 0;
                    }
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
        if (this.upgradeSlot.tickNoMark()) {
            setUpgradestat();
        }


    }

    public boolean operate(int x, int y, int z) {
        if (this.fluidTank.getFluidAmount() >= this.fluidTank.getCapacity()) {
            return false;
        }
        FluidStack liquid;
        boolean canOperate = false;


        if (this.fluidTank.getFluidAmount() >= this.fluidTank.getCapacity()) {
            return false;
        }
        liquid = this.pump(new BlockPos(x, y, z
        ), false);
        if (this.getFluidTank().fill(liquid, false) > 0) {
            this.getFluidTank().fill(liquid, true);
            canOperate = true;


        }


        return canOperate;
    }

    public FluidStack pump(BlockPos pos, boolean sim) {
        FluidStack ret = null;
        int freespace = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();

        if (freespace >= 1000) {
            IBlockState block = this.getWorld().getBlockState(pos);
            if (block.getMaterial().isLiquid()) {


                if (block.getBlock() instanceof IFluidBlock) {
                    IFluidBlock liquid = (IFluidBlock) block.getBlock();
                    if ((this.fluidTank.getFluid() == null || this.fluidTank
                            .getFluid()
                            .getFluid() == liquid.getFluid()) && liquid.canDrain(
                            this.getWorld(),
                            pos
                    )) {
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
                    if (this.fluidTank.getFluid() == null || this.fluidTank
                            .getFluid()
                            .getFluid() == ret.getFluid()) {
                        if (!sim) {
                            this.getWorld().setBlockToAir(pos);
                        }
                    }
                }
            }
        }


        return ret;
    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            this.setUpgradestat();
            x = this.pos.getX();
            y = this.pos.getY() - 1;
            z = this.pos.getZ();
        }

    }

    public void setUpgradestat() {
        double previousProgress = (double) this.progress / (double) this.operationLength;
        double stackOpLen = ((double) this.defaultOperationLength + (double) this.upgradeSlot.extraProcessTime) * 64.0D * this.upgradeSlot.processTimeMultiplier;
        this.operationsPerTick = (int) Math.min(Math.ceil(64.0D / stackOpLen), 2.147483647E9D);
        this.operationLength = (int) Math.round(stackOpLen * (double) this.operationsPerTick / 64.0D);
        this.energyConsume = applyModifier(this.defaultEnergyConsume, this.upgradeSlot.extraEnergyDemand, 1);
        this.energyQe.setSinkTier(applyModifier(this.defaultTier, this.upgradeSlot.extraTier, 1.0D));
        this.energyQe.setCapacity(applyModifier(
                this.defaultEnergyStorage,
                this.upgradeSlot.extraEnergyStorage + this.operationLength * this.energyConsume,
                this.upgradeSlot.energyStorageMultiplier
        ));
        if (this.operationLength < 1) {
            this.operationLength = 1;
        }

        this.progress = (short) ((int) Math.floor(previousProgress * (double) this.operationLength + 0.1D));
    }


    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.defaultEnergyConsume + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.defaultOperationLength);
        }
        super.addInformation(stack, tooltip);

    }

    public double getEnergy() {
        return this.energyQe.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energyQe.canUseEnergy(amount)) {
            this.energyQe.useEnergy(amount);
            return true;
        } else {
            return false;
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");
        this.x = nbttagcompound.getInteger("x_pump");
        this.y = nbttagcompound.getInteger("y_pump");
        this.z = nbttagcompound.getInteger("z_pump");
        this.canWork = nbttagcompound.getBoolean("canWork");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("progress", this.progress);
        nbttagcompound.setInteger("x_pump", this.x);
        nbttagcompound.setInteger("y_pump", this.y);
        nbttagcompound.setInteger("z_pump", this.z);
        nbttagcompound.setBoolean("canWork", this.canWork);
        return nbttagcompound;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.FluidExtract
        );
    }


    @Override
    public EnumTypeStyle getStyle() {
        return null;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.PumpOp.getSoundEvent();
    }

}
