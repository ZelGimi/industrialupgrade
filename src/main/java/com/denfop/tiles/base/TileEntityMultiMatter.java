package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.api.gui.IType;
import com.denfop.api.inv.IHasGui;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.audio.AudioSource;
import com.denfop.audio.PositionSpec;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.BasicRedstoneComponent;
import com.denfop.componets.ComparatorEmitter;
import com.denfop.componets.Fluids;
import com.denfop.componets.Redstone;
import com.denfop.container.ContainerMultiMatter;
import com.denfop.gui.GuiMultiMatter;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotConsumableLiquid;
import com.denfop.invslot.InvSlotConsumableLiquidByList;
import com.denfop.invslot.InvSlotProcessableStandard;
import com.denfop.invslot.InvSlotUpgrade;
import ic2.api.energy.tile.IExplosionPowerOverride;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipeResult;
import ic2.api.recipe.Recipes;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.network.GuiSynced;
import ic2.core.ref.FluidName;
import ic2.core.util.ConfigUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;


public abstract class TileEntityMultiMatter extends TileEntityElectricMachine implements IHasGui, IUpgradableBlock,
        IExplosionPowerOverride, INetworkClientTileEntityEventListener, IType {

    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotProcessableStandard<IRecipeInput, Integer, ItemStack> amplifierSlot;
    public final InvSlotOutput outputSlot;
    public final InvSlotConsumableLiquid containerslot;
    @GuiSynced
    public final FluidTank fluidTank;
    public final float energycost;
    protected final Fluids fluids;
    private final Redstone redstone;
    private final BasicRedstoneComponent comparator;
    public boolean work;
    public int scrap = 0;
    public boolean redstonePowered = false;
    private double lastEnergy;
    private int state = 0;
    private int prevState = 0;
    private AudioSource audioSource;
    private AudioSource audioSourceScrap;

    public TileEntityMultiMatter(float storageEnergy, int sizeTank, float maxtempEnergy) {
        super(Math.round(maxtempEnergy * ConfigUtil.getFloat(MainConfig.get(), "balance/uuEnergyFactor")), 3, 1);
        this.amplifierSlot = new InvSlotProcessableStandard<IRecipeInput, Integer, ItemStack>(
                this,
                "scrap",
                1,
                Recipes.matterAmplifier
        ) {
            protected ItemStack getInput(ItemStack stack) {
                return stack;
            }

            protected void setInput(ItemStack input) {
                this.put(input);
            }


        };
        this.energycost = storageEnergy * ConfigUtil.getFloat(MainConfig.get(), "balance/uuEnergyFactor");
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.containerslot = new InvSlotConsumableLiquidByList(
                this,
                "container",
                InvSlot.Access.I,
                1,
                InvSlot.InvSide.ANY,
                InvSlotConsumableLiquid.OpType.Fill,
                FluidName.uu_matter.getInstance()
        );
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", sizeTank * 1000,
                Fluids.fluidPredicate(FluidName.uu_matter.getInstance())
        );
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.redstone = this.addComponent(new Redstone(this));
        this.redstone.subscribe((newLevel) -> {
            this.energy.setEnabled(newLevel == 0);
            this.work = newLevel != 0;
        });
        this.comparator = this.addComponent(new ComparatorEmitter(this));
        this.comparator.setUpdate(() -> {
            int count = calcRedstoneFromInvSlots(this.amplifierSlot);
            if (count > 0) {
                return count;
            } else {
                return this.scrap > 0 ? 1 : 0;
            }
        });
    }

    private static int applyModifier(int extra) {
        double ret = (double) Math.round(((double) 3 + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.matter_work_info") + (int) this.energycost);
        }

        if (this.getComp(AdvEnergy.class) != null) {
            AdvEnergy energy = this.getComp(AdvEnergy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }


    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        if (i == 10) {
            super.onNetworkEvent(entityPlayer, i);
            setState(0);
            return;
        }
        this.work = !this.work;
        this.energy.setReceivingEnabled(this.work);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.scrap = nbt.getInteger("scrap");
        this.lastEnergy = nbt.getDouble("lastEnergy");
        this.work = nbt.getBoolean("work");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("scrap", this.scrap);
        nbt.setDouble("lastEnergy", this.lastEnergy);
        nbt.setBoolean("work", this.work);
        return nbt;
    }

    protected void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            this.setUpgradestat();
        }

    }

    protected void onUnloaded() {
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
            this.audioSourceScrap = null;
        }

        super.onUnloaded();
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        this.redstonePowered = false;
        if (this.work && !(this.energy.getEnergy() <= 0.0D)) {
            if (this.scrap > 0) {
                double bonus = Math.min(this.scrap, this.energy.getEnergy() - this.lastEnergy);
                if (bonus > 0.0D) {
                    this.energy.forceAddEnergy(5.0D * bonus);
                    this.scrap = (int) ((double) this.scrap - bonus);
                }
                if (sound) {
                    this.setState(2);
                }
            } else {
                if (sound) {
                    this.setState(1);
                }
            }
            if (!this.getActive()) {
                this.setActive(true);
            }
            if (this.scrap < 10000) {
                MachineRecipeResult<IRecipeInput, Integer, ItemStack> recipe = this.amplifierSlot.process();
                if (recipe != null) {
                    this.amplifierSlot.consume(recipe);
                    this.scrap += recipe.getOutput();
                }
            }
            if (this.energy.getEnergy() >= this.energycost) {
                this.attemptGeneration();
            }
            if (!this.containerslot.isEmpty()) {
                this.containerslot.processFromTank(this.fluidTank, this.outputSlot);
            }

            this.lastEnergy = this.energy.getEnergy();

        } else {
            if (sound) {
                this.setState(0);
            }
            if (this.getActive()) {
                this.setActive(false);
            }
        }
        if (this.upgradeSlot.tickNoMark()) {
            setUpgradestat();
        }
    }

    public void attemptGeneration() {
        int k = (int) (this.energy.getEnergy() / this.energycost);
        int m;

        if (this.fluidTank.getFluidAmount() + 1 > this.fluidTank.getCapacity()) {
            return;
        }
        m = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();
        this.fluidTank.fillInternal(new FluidStack(FluidName.uu_matter.getInstance(), Math.min(m, k)), true);
        this.energy.useEnergy(this.energycost * Math.min(m, k));
    }

    public String getProgressAsString() {
        int p = Math.min((int) (this.energy.getEnergy() * 100.0D / this.energycost), 100);
        return "" + p + "%";
    }

    public ContainerMultiMatter getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerMultiMatter(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiMultiMatter(new ContainerMultiMatter(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer player) {
    }

    private void setState(int aState) {
        this.state = aState;
        if (this.prevState != this.state) {
            IUCore.network.get(true).updateTileEntityField(this, "state");
        }

        this.prevState = this.state;
    }

    public List<String> getNetworkFields() {
        List<String> ret = new ArrayList<>();
        ret.add("state");
        ret.addAll(super.getNetworkFields());
        return ret;
    }

    public void onNetworkUpdate(String field) {
        if (field.equals("state") && this.prevState != this.state) {
            switch (this.state) {
                case 0:
                    if (this.audioSource != null) {
                        this.audioSource.stop();
                    }

                    if (this.audioSourceScrap != null) {
                        this.audioSourceScrap.stop();
                    }
                    break;
                case 1:
                    if (sound) {
                        if (this.audioSource == null) {
                            this.audioSource = IUCore.audioManager.createSource(
                                    this,
                                    PositionSpec.Center,
                                    "Generators/MassFabricator/MassFabLoop.ogg",
                                    true,
                                    false,
                                    IC2.audioManager.getDefaultVolume()
                            );
                        }

                        if (this.audioSource != null) {
                            this.audioSource.play();
                        }
                    }
                    if (this.audioSourceScrap != null) {
                        this.audioSourceScrap.stop();
                    }
                    break;
                case 2:
                    if (sound) {
                        if (this.audioSource == null) {
                            this.audioSource = IUCore.audioManager.createSource(
                                    this,
                                    PositionSpec.Center,
                                    "Generators/MassFabricator/MassFabLoop.ogg",
                                    true,
                                    false,
                                    IC2.audioManager.getDefaultVolume()
                            );
                        }

                        if (this.audioSourceScrap == null) {
                            this.audioSourceScrap = IUCore.audioManager.createSource(
                                    this,
                                    PositionSpec.Center,
                                    "Generators/MassFabricator/MassFabScrapSolo.ogg",
                                    true,
                                    false,
                                    IC2.audioManager.getDefaultVolume()
                            );
                        }

                        if (this.audioSource != null) {
                            this.audioSource.play();
                        }

                        if (this.audioSourceScrap != null) {
                            this.audioSourceScrap.play();
                        }
                    }
            }

            this.prevState = this.state;
        }

        super.onNetworkUpdate(field);
    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            this.setUpgradestat();
        }

    }

    public void setUpgradestat() {
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        return this.energy.useEnergy(amount);
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.RedstoneSensitive,
                UpgradableProperty.Transformer,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing,
                UpgradableProperty.FluidProducing
        );
    }

    public boolean shouldExplode() {
        return true;
    }

    public float getExplosionPower(int tier, float defaultPower) {
        return 15.0F;
    }

}
