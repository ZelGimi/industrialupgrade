package com.denfop.ssp.tiles.panels.entity;


import com.denfop.ssp.common.Constants;
import com.denfop.ssp.gui.BackgroundlessDynamicGUI;
import com.denfop.ssp.tiles.InvSlotMultiCharge;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.init.Localization;
import ic2.core.network.GuiSynced;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class TileEntitySunPanel extends TileEntityInventory implements IEnergySource, IHasGui, IGuiValueProvider {
	public final int maxStorage;

	protected final int dayPower;

	protected final InvSlotMultiCharge chargeSlots;

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

	public TileEntitySunPanel(SolarConfig config) {
		this(config.dayPower, config.storage, config.tier);
	}

	public TileEntitySunPanel(int dayPower, int storage, int tier) {
		this.dayPower = dayPower;
		this.chargeSlots = new InvSlotMultiCharge(this, tier, 4, InvSlot.Access.IO);
		this.maxStorage = storage;
		this.tier = tier;
		this.tierPower = EnergyNet.instance.getPowerFromTier(tier);

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

		if (this.active == GenerationState.DAY) {
			tryGenerateEnergy(this.dayPower);
		}
		if (this.storage > 0)
			this.storage = (int) (this.storage - this.chargeSlots.charge(this.storage));
	}

	protected int tickRate() {
		return 128;
	}

	public void checkTheSky() {
		if (this.hasSky && this.world.canBlockSeeSky(this.pos.up())) {
			if (this.world.isDaytime() && (!this.canRain || (!this.world.isRaining() && !this.world.isThundering()))) {
				this.active = GenerationState.DAY;
			} else {
				this.active = GenerationState.NONE;
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

	public boolean getGuiState(String name) {
		if ("sunlight".equals(name))
			return (this.active == GenerationState.DAY);

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

	public ContainerBase<? extends TileEntitySunPanel> getGuiContainer(EntityPlayer player) {
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
		if (this.active == GenerationState.DAY) {
			return String.format("%s %d %s", Localization.translate(Constants.MOD_ID + ".gui.generating"), this.dayPower, Localization.translate("ic2.generic.text.EUt"));
		}
		return String.format("%s 0 %s", Localization.translate(Constants.MOD_ID + ".gui.generating"), Localization.translate("ic2.generic.text.EUt"));
	}

	public enum GenerationState {
		NONE, DAY
	}

	public static final class SolarConfig {
		public final int dayPower;


		final int storage;

		final int tier;


		public SolarConfig(int dayPower, int storage, int tier) {
			this.dayPower = dayPower;

			this.storage = storage;
			this.tier = tier;
		}
	}
}
