package com.denfop.ssp.tiles.panels.entity;


import com.denfop.ssp.common.Constants;
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
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.init.Localization;
import ic2.core.network.GuiSynced;
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

import java.util.ArrayList;
import java.util.List;

public abstract class TileEntityRainPanel extends TileEntityInventory implements IEnergySource, IHasGui, IGuiValueProvider, IWrenchable {
	public final int maxStorage;

	protected final InvSlotMultiCharge chargeSlots;

	protected final int rainPower;

	protected final int tier;


	private final double tierPower;

	@GuiSynced
	public int storage;
	@GuiSynced
	protected GenerationState active = GenerationState.NONE;
	protected int ticker;
	protected boolean canRain;
	protected boolean hasSky;
	private boolean addedToEnet;

	public TileEntityRainPanel(SolarConfig config) {
		this(config.rainPower, config.storage, config.tier);
	}

	public TileEntityRainPanel(int rainPower, int storage, int tier) {
		this.rainPower = rainPower;

		this.maxStorage = storage;
		this.tier = tier;
		this.tierPower = EnergyNet.instance.getPowerFromTier(tier);
		this.chargeSlots = new InvSlotMultiCharge(this, tier, 4, InvSlot.Access.IO);
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

	protected void updateEntityServer() {
		super.updateEntityServer();
		if (this.ticker++ % tickRate() == 0)
			checkTheSky();

		switch (this.active) {
			case DAY:
				tryGenerateEnergy(0);
				break;
			case RAIN:
				tryGenerateEnergy(this.rainPower);
				break;

		}
		if (this.storage > 0)
			this.storage = (int) (this.storage - this.chargeSlots.charge(this.storage));
	}

	protected int tickRate() {
		return 128;
	}

	public void checkTheSky() {
		if (this.world.isDaytime() || this.hasSky && this.world.canBlockSeeSky(this.pos.up())) {
			if (!this.canRain || (!this.world.isRaining() && !this.world.isThundering())) {
				this.active = GenerationState.NONE;
			} else {
				this.active = GenerationState.RAIN;
			}
		} else {
			this.active = GenerationState.NONE;
		}
	}

	public void tryGenerateEnergy(int amount) {
		if (this.storage + amount <= this.maxStorage) {
			this.storage += amount;
		} else {
			this.storage = this.maxStorage;
		}
	}

	@Override
	public List<ItemStack> getWrenchDrops(World world, BlockPos blockPos, IBlockState iBlockState, TileEntity tileEntity, EntityPlayer entityPlayer, int i) {
		List<ItemStack> list = new ArrayList<>();
		for (ItemStack chargeSlot : chargeSlots) {
			list.add(chargeSlot);
		}
		return list;
	}

	@Override
	public boolean canSetFacing(World world, BlockPos pos, EnumFacing newDirection, EntityPlayer player) {
		return false;
	}

	@Override
	public EnumFacing getFacing(World world, BlockPos blockPos) {
		return null;
	}

	@Override
	public boolean setFacing(World world, BlockPos blockPos, EnumFacing enumFacing, EntityPlayer entityPlayer) {
		return false;
	}

	@Override
	public boolean wrenchCanRemove(World world, BlockPos blockPos, EntityPlayer entityPlayer) {
		return true;
	}

	public boolean getGuiState(String name) {
		if ("sunlight".equals(name))
			return (this.active == GenerationState.RAIN);
		if ("moonlight".equals(name))
			return (this.active == GenerationState.RAIN);
		return super.getGuiState(name);
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

	public ContainerBase<? extends TileEntityRainPanel> getGuiContainer(EntityPlayer player) {
		return DynamicContainer.create(this, player, GuiParser.parse(this.teBlock));
	}

	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
		return BackgroundlessDynamicGUI.create((IInventory) this, player, GuiParser.parse(this.teBlock));
	}

	public void onGuiClosed(EntityPlayer player) {
	}

	public double getGuiValue(String name) {
		if ("progress".equals(name))
			return this.storage / this.maxStorage;
		throw new IllegalArgumentException("Unexpected GUI value requested: " + name);
	}

	public String getMaxOutput() {
		return String.format("%s %.0f %s", Localization.translate(Constants.MOD_ID + ".gui.maxOutput"), EnergyNet.instance.getPowerFromTier(this.tier + 1), Localization.translate("ic2.generic.text.EUt"));
	}

	public String getOutput() {
		if (this.active == GenerationState.RAIN) {
			return String.format("%s %d %s", Localization.translate(Constants.MOD_ID + ".gui.generating"), this.rainPower, Localization.translate("ic2.generic.text.EUt"));
		}
		return String.format("%s 0 %s", Localization.translate(Constants.MOD_ID + ".gui.generating"), Localization.translate("ic2.generic.text.EUt"));
	}

	public enum GenerationState {
		NONE, RAIN, DAY, NIGHT
	}

	public static final class SolarConfig {

		public final int rainPower;

		final int storage;

		final int tier;


		public SolarConfig(int rainPower, int storage, int tier) {
			this.rainPower = rainPower;
			this.storage = storage;
			this.tier = tier;
		}
	}
}
