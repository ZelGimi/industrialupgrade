package com.denfop.ssp.tiles.mfsu;

import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.common.Constants;
import com.denfop.ssp.gui.BackgroundlessDynamicGUI;
import com.denfop.ssp.tiles.InvSlotMultiCharge;
import ic2.api.tile.IEnergyStorage;
import ic2.api.tile.IWrenchable;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.comp.Energy;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotDischarge;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.ref.TeBlock;
import ic2.core.util.ConfigUtil;
import ic2.core.util.StackUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public abstract class BaseMFSU extends TileEntityInventory implements IEnergyStorage, IHasGui, IWrenchable {
	private final int output;
	private final Energy energy;
	private static final int SLOTS = 4;
	protected final InvSlotMultiCharge chargeSlots;
	protected final InvSlotDischarge dischargeSlot;

	public BaseMFSU(int output, int tier, double capacity) {
		this.output = output;
		this.chargeSlots = new InvSlotMultiCharge(this, tier, SLOTS, InvSlot.Access.IO);
		this.dischargeSlot = new InvSlotDischarge(this, InvSlot.Access.IO, tier, InvSlot.InvSide.BOTTOM);
		this.energy = addComponent((new Energy(this, capacity, EnumSet.complementOf(EnumSet.of(EnumFacing.DOWN)),
				EnumSet.of(EnumFacing.DOWN), tier)).addManagedSlot(dischargeSlot));
	}

	protected void updateEntityServer() {
		super.updateEntityServer();
		if (this.energy.getEnergy() > this.energy.getCapacity())
			this.energy.addEnergy(this.energy.getEnergy() - this.energy.getCapacity());
		if (this.energy.getEnergy() > 0)
			this.energy.useEnergy(this.chargeSlots.charge(this.energy.getEnergy()));
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

	@Override
	public List<ItemStack> getWrenchDrops(World world, BlockPos blockPos, IBlockState iBlockState, TileEntity tileEntity, EntityPlayer entityPlayer, int i) {
		List<ItemStack> list = new ArrayList<>();
		chargeSlots.forEach(list::add);
		dischargeSlot.forEach(list::add);
		return list;
	}


	@Override
	public boolean canSetFacing(World world, BlockPos pos, EnumFacing enumFacing, EntityPlayer player) {
		if (!this.teBlock.allowWrenchRotating()) {
			return false;
		} else if (enumFacing == this.getFacing()) {
			return false;
		} else {
			return this.getSupportedFacings().contains(enumFacing);
		}
	}

	@Override
	public EnumFacing getFacing(World world, BlockPos blockPos) {
		return this.getFacing();
	}

	@Override
	public boolean setFacing(World world, BlockPos blockPos, EnumFacing enumFacing, EntityPlayer entityPlayer) {
		if (!this.canSetFacingWrench(enumFacing, entityPlayer)) {
			return false;
		} else {
			this.setFacing(enumFacing);
			return true;
		}
	}

	@Override
	public boolean wrenchCanRemove(World world, BlockPos blockPos, EntityPlayer entityPlayer) {
		return true;
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
			return DynamicContainer.create(this, player, GuiParser.parse(SuperSolarPanels.getIdentifier("guidef/advmfsu.xml"), this.teBlock.getTeClass()));
		} catch (Exception exception) {
			return null;
		}
	}

	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(EntityPlayer player, boolean b) {
		try {
			return BackgroundlessDynamicGUI.create((IInventory) this, player, GuiParser.parse(SuperSolarPanels.getIdentifier("guidef/advmfsu.xml"), this.teBlock.getTeClass()));
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
		return String.format("%s %d / %d", Localization.translate(Constants.MOD_ID + ".gui.storage"), (long) this.energy.getEnergy(), (long) this.energy.getCapacity());
	}

	public String getOutputText() {
		return String.format("%s %d %s", Localization.translate(Constants.MOD_ID + ".gui.maxOutput"), (long) this.output, Localization.translate("ic2.generic.text.EUt"));
	}
}
