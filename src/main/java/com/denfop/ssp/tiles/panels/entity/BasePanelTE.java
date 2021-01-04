package com.denfop.ssp.tiles.panels.entity;

import com.denfop.ssp.common.Constants;
import com.denfop.ssp.common.Utils;
import com.denfop.ssp.gui.BackgroundlessDynamicGUI;
import com.denfop.ssp.tiles.InvSlotMultiCharge;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IWrenchable;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.init.Localization;
import ic2.core.network.GuiSynced;
import ic2.core.util.Util;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class BasePanelTE extends TileEntityInventory implements IEnergySource, IHasGui, IGuiValueProvider, IWrenchable {

	protected final int maxStorage;

	protected final int tier;

	protected final InvSlotMultiCharge chargeSlots;

	protected final double tierPower;

	@GuiSynced
	protected int storage;
	@GuiSynced
	protected GenerationState active = GenerationState.NONE;
	protected int ticker;
	protected boolean canRain;
	protected boolean hasSky;
	protected boolean addedToEnet;

	public BasePanelTE(SolarConfig config) {
		this.storage = 0;
		this.maxStorage = config.maxStorage;
		this.tier = config.tier;
		this.chargeSlots = new InvSlotMultiCharge(this, tier, 4, InvSlot.Access.IO);
		this.tierPower = EnergyNet.instance.getPowerFromTier(tier);
	}

	protected boolean canSeeSky(BlockPos up) {
		return this.hasSky && this.world.canBlockSeeSky(up) &&
				(this.world.getBlockState(up).getMaterial().getMaterialMapColor() ==
						MapColor.AIR);
	}

	public void tryGenerateEnergy(int amount) {
		if (this.storage + amount <= this.maxStorage) {
			this.storage += amount;
		} else {
			this.storage = this.maxStorage;
		}
	}

	protected void onLoaded() {
		super.onLoaded();
		if (!this.world.isRemote) {
			this.addedToEnet = !MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			this.canRain = (this.world.getBiome(this.pos).canRain() || this.world.getBiome(this.pos).getRainfall() > 0.0F);
			this.hasSky = !this.world.provider.isNether();
		}
	}

	protected void onUnloaded() {
		super.onUnloaded();
		if (this.addedToEnet)
			this.addedToEnet = MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, tooltip, advanced);
		tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", this.tier));
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.storage = nbt.getInteger("storage");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("storage", this.storage);
		return nbt;
	}

	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return true;
	}

	public double getOfferedEnergy() {
		return (this.storage < this.tierPower) ? this.storage : this.tierPower;
	}

	public void drawEnergy(double amount) {
		this.storage = (int) (this.storage - amount);
	}

	public int getSourceTier() {
		return this.tier;
	}

	@Override
	public List<ItemStack> getWrenchDrops(World world, BlockPos blockPos, IBlockState iBlockState, TileEntity tileEntity, EntityPlayer entityPlayer, int i) {
		List<ItemStack> list = new ArrayList<>();
		chargeSlots.forEach(list::add);
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

	public String getStorage() {
		return Util.toSiString(this.storage, 3) + "/" + Util.toSiString(this.maxStorage, 3) + " EU";
	}

	public double getGuiValue(String name) {
		if ("progress".equals(name))
			return (double) this.storage / this.maxStorage;
		throw new IllegalArgumentException("Unexpected GUI value requested: " + name);
	}

	protected int tickRate() {
		return 128;
	}

	public String getMaxOutput() {
		return String.format("%s %s %s", Localization.translate(Constants.MOD_ID + ".gui.maxOutput"), Util.toSiString(EnergyNet.instance.getPowerFromTier(this.tier + 1), 3), Localization.translate("ic2.generic.text.EUt"));
	}

	public void onGuiClosed(EntityPlayer player) {
	}

	@Nonnull
	protected abstract String getGuiDef();

	public ContainerBase<? extends BasePanelTE> getGuiContainer(EntityPlayer player) {
		return DynamicContainer.create(this, player, Utils.parse(this.getGuiDef()));
	}

	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
		return BackgroundlessDynamicGUI.create((IInventory) this, player, Utils.parse(this.getGuiDef()));
	}

	protected void updateEntityServer() {
		super.updateEntityServer();
		if (this.ticker++ % tickRate() == 0)
			checkTheSky();
	}

	public abstract void checkTheSky();

	public abstract String getOutput();

	public enum GenerationState {
		NONE, NIGHT, DAY, RAIN
	}

	public static class SolarConfig {
		final int maxStorage;
		final int tier;

		public SolarConfig(int maxStorage, int tier) {
			this.maxStorage = maxStorage;
			this.tier = tier;
		}

	}
}
