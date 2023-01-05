package com.denfop.tiles.base;


import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerCombinerMatter;
import com.denfop.gui.GuiCombinerMatter;
import com.denfop.invslot.InvSlotMatter;
import com.denfop.invslot.InvSlotUpgrade;
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
import ic2.core.block.comp.ComparatorEmitter;
import ic2.core.block.comp.Redstone;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByList;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.ref.FluidName;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class TileEntityCombinerMatter extends TileEntityElectricLiquidTankInventory implements IHasGui, IUpgradableBlock {

    public final InvSlotMatter inputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotProcessable<IRecipeInput, Integer, ItemStack> amplifierSlot;
    public final InvSlotOutput outputSlot;
    public final InvSlotConsumableLiquid containerslot;
    protected final Redstone redstone;
    private final ComparatorEmitter comparator;
    public int scrap;
    public double energycost;
    private int state, prevState;
    private AudioSource audioSource, audioSourceScrap;
    private double lastEnergy;

    public TileEntityCombinerMatter() {
        super(0, 14, 12);
        this.energycost = 0;
        this.amplifierSlot = new InvSlotProcessable<IRecipeInput, Integer, ItemStack>(this, "scrap", 1, Recipes.matterAmplifier) {
            protected ItemStack getInput(ItemStack stack) {
                return stack;
            }

            protected void setInput(ItemStack input) {
                this.put(input);
            }
        };
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.containerslot = new InvSlotConsumableLiquidByList(this, "containerslot", InvSlot.Access.I, 1,
                InvSlot.InvSide.TOP, InvSlotConsumableLiquid.OpType.Fill, FluidName.uu_matter.getInstance()
        );
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, "upgrade", 4);
        this.inputSlot = new InvSlotMatter(this);

        this.redstone = addComponent(new Redstone(this));
        this.comparator = this.addComponent(new ComparatorEmitter(this));
        this.comparator.setUpdate(() -> {
            int count = calcRedstoneFromInvSlots(this.amplifierSlot);
            if (count > 0) {
                return count;
            } else {
                return this.scrap > 0 ? 1 : 0;
            }
        });
        this.energy = this.addComponent(AdvEnergy.asBasicSink(this, 0, 14).addManagedSlot(this.dischargeSlot));

    }

    private static int applyModifier(int base) {
        double ret = Math.round((base + 14) * 1.0);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @Override
    public int getInventoryStackLimit() {
        return 4;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.scrap = nbttagcompound.getInteger("scrap");
        this.lastEnergy = nbttagcompound.getDouble("lastEnergy");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("scrap", this.scrap);
        nbttagcompound.setDouble("lastEnergy", this.lastEnergy);
        return nbttagcompound;

    }

    public void updateEntityServer() {
        super.updateEntityServer();

        if (this.redstone.hasRedstoneInput() || this.energy.getEnergy() <= 0.0D) {
            if (this.getActive()) {
                setState(0);
                setActive(false);
            }
        } else {
            if (this.scrap > 0) {
                double bonus = Math.min(this.scrap, this.energy.getEnergy() - this.lastEnergy);
                if (bonus > 0.0D) {
                    this.energy.forceAddEnergy(5.0D * bonus);
                    this.scrap = (int) ((double) this.scrap - bonus);
                }

                this.setState(2);
            } else {
                this.setState(1);
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


            if (this.energy.canUseEnergy(energycost)) {
                attemptGeneration();
            }
            this.lastEnergy = this.energy.getEnergy();


            MutableObject<ItemStack> output = new MutableObject<>();
            if (this.containerslot.transferFromTank(this.fluidTank, output, true)
                    && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
                this.containerslot.transferFromTank(this.fluidTank, output, false);
                if (output.getValue() != null) {
                    this.outputSlot.add(output.getValue());
                }
            }

            if (this.upgradeSlot.tickNoMark()) {
                setUpgradestat();
            }


        }

    }


    public void onUnloaded() {
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
            this.audioSourceScrap = null;
        }
        super.onUnloaded();
    }

    public boolean attemptGeneration() {
        int k = (int) (this.energy.getEnergy() / this.energycost);
        int m;

        if (this.fluidTank.getFluidAmount() + 1 > this.fluidTank.getCapacity()) {
            return false;
        }
        m = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();
        this.fluidTank.fillInternal(new FluidStack(FluidName.uu_matter.getInstance(), Math.min(m, k)), true);
        this.energy.useEnergy(this.energycost * Math.min(m, k));
        return true;
    }


    public ContainerBase<TileEntityCombinerMatter> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerCombinerMatter(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiCombinerMatter(new ContainerCombinerMatter(entityPlayer, this));
    }


    public void onGuiClosed(EntityPlayer entityPlayer) {
    }

    private void setState(int aState) {
        this.state = aState;
        if (this.prevState != this.state) {
            IC2.network.get(true).updateTileEntityField(this, "state");
        }
        this.prevState = this.state;
    }

    public List<String> getNetworkedFields() {
        List<String> ret = new Vector<>(1);
        ret.add("state");
        ret.addAll(super.getNetworkedFields());
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
                    if (this.audioSource == null) {
                        this.audioSource = IC2.audioManager.createSource(
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
                    if (this.audioSourceScrap != null) {
                        this.audioSourceScrap.stop();
                    }
                    break;
                case 2:
                    if (this.audioSource == null) {
                        this.audioSource = IC2.audioManager.createSource(
                                this,
                                PositionSpec.Center,
                                "Generators/MassFabricator/MassFabLoop.ogg",
                                true,
                                false,
                                IC2.audioManager.getDefaultVolume()
                        );
                    }
                    if (this.audioSourceScrap == null) {
                        this.audioSourceScrap = IC2.audioManager.createSource(
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
                    break;
            }
            this.prevState = this.state;
        }
        super.onNetworkUpdate(field);
    }

    public float getWrenchDropRate() {
        return 0.7F;
    }

    public boolean canFill(Fluid fluid) {
        return fluid == FluidName.uu_matter.getInstance();
    }

    public boolean canDrain(Fluid fluid) {
        return true;
    }

    public void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            setUpgradestat();
            this.inputSlot.update();
        }
    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setUpgradestat();
        }
    }

    public void setUpgradestat() {
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energy.canUseEnergy(amount)) {
            this.energy.useEnergy(amount);
            return true;
        }
        return false;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.RedstoneSensitive, UpgradableProperty.Transformer,
                UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing, UpgradableProperty.FluidProducing
        );
    }

}
