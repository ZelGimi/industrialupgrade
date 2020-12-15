package com.denfop.ssp.tiles;

import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.common.Configs;
import ic2.api.tile.IEnergyStorage;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.comp.Energy;
import ic2.core.block.comp.TileEntityComponent;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotCharge;
import ic2.core.block.invslot.InvSlotDischarge;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.DynamicGui;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.ref.TeBlock;
import ic2.core.util.ConfigUtil;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;

public class QuantumMFSU extends TileEntityInventory implements IEnergyStorage, IHasGui {
	protected final InvSlotCharge chargeSlot = new InvSlotCharge(this, 5);
	protected final InvSlotDischarge dischargeSlot = new InvSlotDischarge(this, InvSlot.Access.IO, 5, InvSlot.InvSide.BOTTOM);
	private final int output = Configs.quantummfsu * 4;
	private final Energy energy = (Energy) addComponent((TileEntityComponent) (new Energy(this, Configs.quantummfsu2, EnumSet.complementOf(EnumSet.of(EnumFacing.DOWN)),
			EnumSet.of(EnumFacing.DOWN), Configs.quantummfsu1, Configs.quantummfsu1, false)).addManagedSlot(this.chargeSlot).addManagedSlot(this.dischargeSlot));

	protected void updateEntityServer() {
		super.updateEntityServer();
		if (this.energy.getEnergy() > this.energy.getCapacity())
			this.energy.addEnergy(this.energy.getEnergy() - this.energy.getCapacity());
	}

	public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
		super.onPlaced(stack, placer, facing);
		if (!(getWorld()).isRemote) {
			NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
			this.energy.addEnergy(nbt.getDouble("energy"));
		}
	}

	protected ItemStack adjustDrop(ItemStack drop, boolean wrench) {
		drop = super.adjustDrop(drop, wrench);
		if (wrench || this.teBlock.getDefaultDrop() == TeBlock.DefaultDrop.Self) {
			double retainedRatio = ConfigUtil.getDouble(MainConfig.get(), "balance/energyRetainedInStorageBlockDrops");
			double totalEnergy = this.energy.getEnergy();
			if (retainedRatio > 0.0D && totalEnergy > 0.0D) {
				NBTTagCompound nbt = StackUtil.getOrCreateNbtData(drop);
				nbt.setDouble("energy", Math.round(totalEnergy * retainedRatio));
			}
		}
		return drop;
	}

	public void setFacing(EnumFacing facing) {
		super.setFacing(facing);
		this.energy.setDirections(EnumSet.complementOf(EnumSet.of(getFacing())), EnumSet.of(getFacing()));
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, tooltip, advanced);
		tooltip.add(String.format("%s %s %s %s %s %s", Localization.translate("ic2.item.tooltip.Output"), (long) this.output,
				Localization.translate("ic2.generic.text.EUt"),
				Localization.translate("ic2.item.tooltip.Capacity"), (long) this.energy.getCapacity(),
				Localization.translate("ic2.generic.text.EU")));
		tooltip.add(Localization.translate("ic2.item.tooltip.Store") + " " +
				StackUtil.getOrCreateNbtData(stack).getDouble("energy") + " " +
				Localization.translate("ic2.generic.text.EU"));
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.energy.setDirections(EnumSet.complementOf(EnumSet.of(getFacing())), EnumSet.of(getFacing()));
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		return nbt;
	}

	public ContainerBase<?> getGuiContainer(EntityPlayer player) {
		try {
			return DynamicContainer.create((IInventory) this, player, GuiParser.parse(SuperSolarPanels.getIdentifier("guidef/QuantumMFSU.xml"), this.teBlock.getTeClass()));
		} catch (Exception exception) {
			return null;
		}
	}

	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(EntityPlayer player, boolean b) {
		try {
			return DynamicGui.create((IInventory) this, player, GuiParser.parse(SuperSolarPanels.getIdentifier("guidef/QuantumMFSU.xml"), this.teBlock.getTeClass()));
		} catch (Exception exception) {
			return null;
		}
	}

	public void onGuiClosed(EntityPlayer player) {
	}

	public int getStored() {
		return (int) this.energy.getEnergy();
	}

	public void setStored(int energy) {
	}

	public int addEnergy(int amount) {
		this.energy.addEnergy(amount);
		return amount;
	}

	public int getCapacity() {
		return (int) this.energy.getCapacity();
	}

	public int getOutput() {
		return this.output;
	}

	public double getOutputEnergyUnitsPerTick() {
		return this.output;
	}

	public boolean isTeleporterCompatible(EnumFacing side) {
		return true;
	}

	public String getStorageText() {
		return Localization.translate("gui.text.maxStorage", (long) this.energy.getEnergy());
	}

	public String getCapacityText() {
		return Localization.translate("gui.text.currentStorage", (long) this.energy.getCapacity());
	}

	public String getOutputText() {
		return Localization.translate("gui.text.output", this.output);
	}
}
