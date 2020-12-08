package com.Denfop.ssp.tiles;


import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.init.Localization;
import ic2.core.network.GuiSynced;
import java.util.List;

import com.Denfop.ssp.gui.BackgroundlessDynamicGUI;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntityMoonPanel extends TileEntityInventory implements IEnergySource, IHasGui, IGuiValueProvider {
  public final int maxStorage;
  

  
  protected final int nightPower;
  
  protected final int tier;
  
  protected final InvSlotMultiCharge chargeSlots;
  
  private final double tierPower;
  
  @GuiSynced
  public int storage;
  
  public enum GenerationState {
    NONE, NIGHT;
  }
  
  public static final class SolarConfig {
   
    
    public final int nightPower;
    
    final int storage;
    
    final int tier;

   
    


    
    public SolarConfig( int nightPower, int storage, int tier) {
     
      this.nightPower = nightPower;
      this.storage = storage;
      this.tier = tier;
    }
  }
  
  @GuiSynced
  protected GenerationState active = GenerationState.NONE;
  
  protected int ticker;
  
  protected boolean canRain;
  
  protected boolean hasSky;
  
  private boolean addedToEnet;

  
  public TileEntityMoonPanel(SolarConfig config) {
    this( config.nightPower, config.storage, config.tier);
  }
  
  public TileEntityMoonPanel( int nightPower, int storage, int tier) {
   
    this.nightPower = nightPower;
    this.maxStorage = storage;
    this.tier = tier;
    this.tierPower = EnergyNet.instance.getPowerFromTier(tier);
    this.chargeSlots = new InvSlotMultiCharge(this, tier, 4, InvSlot.Access.IO);
    
  }
  
  protected void onLoaded() {
    super.onLoaded();
    if (!this.world.isRemote) {
      this.addedToEnet = !MinecraftForge.EVENT_BUS.post((Event)new EnergyTileLoadEvent((IEnergyTile)this));
      this.canRain = (this.world.getBiome(this.pos).canRain() || this.world.getBiome(this.pos).getRainfall() > 0.0F);
      this.hasSky = !this.world.provider.isNether();
    } 
  }
  
  protected void onUnloaded() {
    super.onUnloaded();
    if (this.addedToEnet)
      this.addedToEnet = MinecraftForge.EVENT_BUS.post((Event)new EnergyTileUnloadEvent((IEnergyTile)this));
    
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
  
  protected void updateEntityServer() {
    super.updateEntityServer();
    if (this.ticker++ % tickRate() == 0)
      checkTheSky(); 

    	switch (this.active) {
  
      case NIGHT:
        tryGenerateEnergy(this.nightPower);
        break;
   
    }
    	 if (this.storage > 0)
    	      this.storage = (int)(this.storage - this.chargeSlots.charge(this.storage)); 
  }
  
  protected int tickRate() {
    return 128;
  }
  
  public void checkTheSky() {
    if (this.hasSky && this.world.canBlockSeeSky(this.pos.up())) {
      if (this.world.isDaytime() && (!this.canRain || (!this.world.isRaining() && !this.world.isThundering()))) {
        this.active = GenerationState.NONE;
      } else {
        this.active = GenerationState.NIGHT;
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
  
  public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
    return true;
  }
  
  public int getSourceTier() {
    return this.tier;
  }
  
  public double getOfferedEnergy() {
    return (this.storage < this.tierPower) ? this.storage : this.tierPower;
  }
  
  public void drawEnergy(double amount) {
    this.storage = (int)(this.storage - amount);
  }
  
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
    super.addInformation(stack, tooltip, advanced);
    tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", new Object[] { Integer.valueOf(this.tier) }));
  }
  
  public ContainerBase<? extends TileEntityMoonPanel> getGuiContainer(EntityPlayer player) {
    return (ContainerBase<? extends TileEntityMoonPanel>)DynamicContainer.create(this, player, GuiParser.parse(this.teBlock));
  }
  
  @SideOnly(Side.CLIENT)
  public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
    return (GuiScreen)BackgroundlessDynamicGUI.create((IInventory)this, player, GuiParser.parse(this.teBlock));
  }
  
  public void onGuiClosed(EntityPlayer player) {}
  
  public double getGuiValue(String name) {
    if ("progress".equals(name))
      return this.storage / this.maxStorage; 
    throw new IllegalArgumentException("Unexpected GUI value requested: " + name);
  }
  
  public boolean getGuiState(String name) {
    
    if ("moonlight".equals(name))
      return (this.active == GenerationState.NIGHT); 
    return super.getGuiState(name);
  }
  
  public String getMaxOutput() {
    return String.format("%s %.0f %s", new Object[] { Localization.translate("super_solar_panels.gui.maxOutput"), Double.valueOf(EnergyNet.instance.getPowerFromTier(this.tier+1)), Localization.translate("ic2.generic.text.EUt") });
  }
  
  public String getOutput() {
    switch (this.active) {
     
      case NIGHT:
        return String.format("%s %d %s", new Object[] { Localization.translate("super_solar_panels.gui.generating"), Integer.valueOf(this.nightPower), Localization.translate("ic2.generic.text.EUt") });
    } 
    return String.format("%s 0 %s", new Object[] { Localization.translate("super_solar_panels.gui.generating"), Localization.translate("ic2.generic.text.EUt") });
  }
}
