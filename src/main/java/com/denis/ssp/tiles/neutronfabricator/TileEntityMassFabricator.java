package com.Denfop.ssp.tiles.neutronfabricator;

import ic2.api.energy.tile.IExplosionPowerOverride;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipeResult;
import ic2.api.recipe.Recipes;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.audio.PositionSpec;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.Fluids;
import ic2.core.block.comp.Redstone;
import ic2.core.block.comp.TileEntityComponent;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByList;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.block.invslot.InvSlotUpgrade;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import ic2.core.init.MainConfig;
import ic2.core.item.type.CraftingItemType;
import ic2.core.network.GuiSynced;
import ic2.core.network.NetworkManager;
import ic2.core.profile.NotClassic;
import ic2.core.recipe.MatterAmplifierRecipeManager;
import ic2.core.util.ConfigUtil;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.Denfop.ssp.Configs;
import com.Denfop.ssp.fluid.Neutron.FluidRegister;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@NotClassic
public class TileEntityMassFabricator extends TileEntityElectricMachine implements IHasGui, IUpgradableBlock {
  public TileEntityMassFabricator() {
    super(Math.round(Configs.Neutronfabricator), Configs.Neutronfabricator1);
    this.scrap = 0;
    this.state = 0;
    this.prevState = 0;
    this.redstonePowered = false;
    this.amplifierSlot = new InvSlotProcessable<IRecipeInput, Integer, ItemStack>(this, "scrap", 1, Recipes.matterAmplifier) {
        protected ItemStack getInput(ItemStack stack) {
          return stack;
        }
        
        protected void setInput(ItemStack input) {
          put(input);
        }
      };
    this.outputSlot = new InvSlotOutput(this, "output", 1);
    this.containerslot = (InvSlotConsumableLiquid)new InvSlotConsumableLiquidByList(this, "container", InvSlot.Access.I, 1, InvSlot.InvSide.TOP, InvSlotConsumableLiquid.OpType.Fill, new Fluid[] { FluidRegister.Neutron});
    this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
    this.redstone = (Redstone)addComponent((TileEntityComponent)new Redstone((TileEntityBlock)this));
    this.redstone.subscribe(new Redstone.IRedstoneChangeHandler() {
          public void onRedstoneChange(int newLevel) {
            TileEntityMassFabricator.this.energy.setEnabled((newLevel == 0));
          }
        });
    this.fluids = (Fluids)addComponent((TileEntityComponent)new Fluids((TileEntityBlock)this));
    this.fluidTank = (FluidTank)this.fluids.addTank("fluidTank", 10000, Fluids.fluidPredicate(new Fluid[] {FluidRegister.Neutron }));
    this.comparator.setUpdate(() -> {
          int count = calcRedstoneFromInvSlots(new InvSlot[] { (InvSlot)this.amplifierSlot });
          return (count > 0) ? count : ((this.scrap > 0) ? 1 : 0);
        });
  }
  
  public static void init() {
    Recipes.matterAmplifier = (IMachineRecipeManager)new MatterAmplifierRecipeManager();
  //  addAmplifier(ItemName.crafting.getItemStack(CraftingItemType.scrap), 1, 5000);
    //addAmplifier(ItemName.crafting.getItemStack(CraftingItemType.scrap_box), 1, 45000);
  }
  
  public static void addAmplifier(ItemStack input, int amount, int amplification) {
    addAmplifier(Recipes.inputFactory.forStack(input, amount), amplification);
  }
  
  public static void addAmplifier(String input, int amount, int amplification) {
    addAmplifier(Recipes.inputFactory.forOreDict(input, amount), amplification);
  }
  
  public static void addAmplifier(IRecipeInput input, int amplification) {
    Recipes.matterAmplifier.addRecipe(input, Integer.valueOf(amplification), null, false);
  }
  
  public void readFromNBT(NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    this.scrap = nbt.getInteger("scrap");
    this.lastEnergy = nbt.getDouble("lastEnergy");
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    nbt.setInteger("scrap", this.scrap);
    nbt.setDouble("lastEnergy", this.lastEnergy);
    return nbt;
  }
  
  protected void onLoaded() {
    super.onLoaded();
    if (!(getWorld()).isRemote)
      setUpgradestat(); 
  }
  
  protected void onUnloaded() {
    if (IC2.platform.isRendering() && this.audioSource != null) {
      IC2.audioManager.removeSources(this);
      this.audioSource = null;
      this.audioSourceScrap = null;
    } 
    super.onUnloaded();
  }
  
  protected void updateEntityServer() {
    super.updateEntityServer();
    this.redstonePowered = false;
    boolean needsInvUpdate = false;
    needsInvUpdate |= this.upgradeSlot.tickNoMark();
    if (this.redstone.hasRedstoneInput() || this.energy.getEnergy() <= 0.0D) {
      setState(0);
      setActive(false);
    } else {
      if (this.scrap > 0) {
        double bonus = Math.min(this.scrap, this.energy.getEnergy() - this.lastEnergy);
        if (bonus > 0.0D) {
          this.energy.forceAddEnergy(5.0D * bonus);
          this.scrap = (int)(this.scrap - bonus);
        } 
        setState(2);
      } else {
        setState(1);
      } 
      setActive(true);
      if (this.scrap < 10000) {
        MachineRecipeResult<IRecipeInput, Integer, ItemStack> recipe = this.amplifierSlot.process();
        if (recipe != null) {
          this.amplifierSlot.consume(recipe);
          this.scrap += ((Integer)recipe.getOutput()).intValue();
        } 
      } 
      if (this.energy.getEnergy() >= this.energy.getCapacity())
        needsInvUpdate = attemptGeneration(); 
      needsInvUpdate |= this.containerslot.processFromTank((IFluidTank)this.fluidTank, this.outputSlot);
      this.lastEnergy = this.energy.getEnergy();
      if (needsInvUpdate)
        markDirty(); 
    } 
  }
  
  public boolean amplificationIsAvailable() {
    if (this.scrap > 0)
      return true; 
    MachineRecipeResult<? extends IRecipeInput, ? extends Integer, ? extends ItemStack> recipe = this.amplifierSlot.process();
    return (recipe != null && ((Integer)recipe.getOutput()).intValue() > 0);
  }
  
  public boolean attemptGeneration() {
    if (this.fluidTank.getFluidAmount() + 1 > this.fluidTank.getCapacity())
      return false; 
    this.fluidTank.fillInternal(new FluidStack(FluidRegister.Neutron, 1), true);
    this.energy.useEnergy(this.energy.getCapacity());
    return true;
  }
  
  public String getProgressAsString() {
    int p = (int)Math.min(100.0D * this.energy.getFillRatio(), 100.0D);
    return "" + p + "%";
  }
  
  public ContainerBase<TileEntityMassFabricator> getGuiContainer(EntityPlayer player) {
	    return (ContainerBase)new ContainerMatter(player, this);
	  }
	  
	  @SideOnly(Side.CLIENT)
	  public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
	    return (GuiScreen)new GuiMatter(new ContainerMatter(player, this));
	  }
	  
  public void onGuiClosed(EntityPlayer player) {}
  
  private void setState(int aState) {
    this.state = aState;
    if (this.prevState != this.state)
      ((NetworkManager)IC2.network.get(true)).updateTileEntityField((TileEntity)this, "state"); 
    this.prevState = this.state;
  }
  
  public List<String> getNetworkedFields() {
    List<String> ret = new ArrayList<>();
    ret.add("state");
    ret.addAll(super.getNetworkedFields());
    return ret;
  }
  
  public void onNetworkUpdate(String field) {
    if (field.equals("state") && this.prevState != this.state) {
      switch (this.state) {
        case 0:
          if (this.audioSource != null)
            this.audioSource.stop(); 
          if (this.audioSourceScrap != null)
            this.audioSourceScrap.stop(); 
          break;
        case 1:
          if (this.audioSource == null)
            this.audioSource = IC2.audioManager.createSource(this, PositionSpec.Center, "Generators/MassFabricator/MassFabLoop.ogg", true, false, IC2.audioManager.getDefaultVolume()); 
          if (this.audioSource != null)
            this.audioSource.play(); 
          if (this.audioSourceScrap != null)
            this.audioSourceScrap.stop(); 
          break;
        case 2:
          if (this.audioSource == null)
            this.audioSource = IC2.audioManager.createSource(this, PositionSpec.Center, "Generators/MassFabricator/MassFabLoop.ogg", true, false, IC2.audioManager.getDefaultVolume()); 
          if (this.audioSourceScrap == null)
            this.audioSourceScrap = IC2.audioManager.createSource(this, PositionSpec.Center, "Generators/MassFabricator/MassFabScrapSolo.ogg", true, false, IC2.audioManager.getDefaultVolume()); 
          if (this.audioSource != null)
            this.audioSource.play(); 
          if (this.audioSourceScrap != null)
            this.audioSourceScrap.play(); 
          break;
      } 
      this.prevState = this.state;
    } 
    super.onNetworkUpdate(field);
  }
  
  public void markDirty() {
    super.markDirty();
    if (IC2.platform.isSimulating())
      setUpgradestat(); 
  }
  
  public void setUpgradestat() {
    this.upgradeSlot.onChanged();
    this.energy.setSinkTier(applyModifier(14, this.upgradeSlot.extraTier, 1.0D));
  }
  
  private static int applyModifier(int base, int extra, double multiplier) {
    double ret = Math.round((base + extra) * multiplier);
    return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int)ret;
  }
  
  public double getEnergy() {
    return this.energy.getEnergy();
  }
  
  public boolean useEnergy(double amount) {
    return this.energy.useEnergy(amount);
  }
  
  public Set<UpgradableProperty> getUpgradableProperties() {
    return EnumSet.of(UpgradableProperty.RedstoneSensitive, UpgradableProperty.Transformer, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing, UpgradableProperty.FluidProducing, UpgradableProperty.Processing);
  }
  
 
 
  
  
  public int scrap;
  
  private double lastEnergy;
  
  private static final int StateIdle = 0;
  
  private static final int StateRunning = 1;
  
  private static final int StateRunningScrap = 2;
  
  private int state;
  
  private int prevState;
  
  public boolean redstonePowered;
  
  private AudioSource audioSource;
  
  private AudioSource audioSourceScrap;
  
  public final InvSlotUpgrade upgradeSlot;
  
  public final InvSlotProcessable<IRecipeInput, Integer, ItemStack> amplifierSlot;
  
  public final InvSlotOutput outputSlot;
  
  public final InvSlotConsumableLiquid containerslot;
  
  @GuiSynced
  public final FluidTank fluidTank;
  
  protected final Redstone redstone;
  
  protected final Fluids fluids;
}
