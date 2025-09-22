package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerElectrolyzer;
import com.denfop.gui.GuiElectrolyzer;
import com.denfop.invslot.*;
import com.denfop.invslot.Inventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileElectrolyzer extends TileElectricMachine implements IManufacturerBlock, IUpgradableBlock, IHasRecipe {

    public final InventoryElectrolyzer cathodeslot;
    public final InventoryElectrolyzer anodeslot;
    public final Fluids.InternalFluidTank fluidTank2;
    public final Fluids.InternalFluidTank fluidTank1;
    public final Fluids.InternalFluidTank fluidTank3;
    public final FluidHandlerRecipe fluid_handler;
    public final InventoryFluidByList fluidSlot1;
    public final InventoryFluidByList fluidSlot2;
    public final InventoryFluidByList fluidSlot3;
    public final InventoryUpgrade upgradeSlot;
    private final Fluids fluids;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private int level;

    public TileElectrolyzer() {
        super(24000, 1, 2);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank1", 12 * 1000, Inventory.TypeItemSlot.INPUT);


        this.fluidTank2 = fluids.addTank("fluidTank2", 12 * 1000, Inventory.TypeItemSlot.OUTPUT);


        this.fluidTank3 = fluids.addTank("fluidTank3", 12 * 1000, Inventory.TypeItemSlot.OUTPUT);
        this.fluid_handler = new FluidHandlerRecipe("electrolyzer", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidTank3.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(1)));
        this.fluidSlot1 = new InventoryFluidByList(this, 1, this.fluid_handler.getFluids(0));
        this.fluidSlot2 = new InventoryFluidByList(this, 1, this.fluid_handler.getOutputFluids(0));
        this.fluidSlot3 = new InventoryFluidByList(this, 1, this.fluid_handler.getOutputFluids(1));
        this.fluidSlot2.setTypeFluidSlot(InventoryFluid.TypeFluidSlot.OUTPUT);
        this.fluidSlot3.setTypeFluidSlot(InventoryFluid.TypeFluidSlot.OUTPUT);
        this.cathodeslot = new InventoryElectrolyzer(this, 1);
        this.anodeslot = new InventoryElectrolyzer(this, 0);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        Recipes.recipes.getRecipeFluid().addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        NBTTagCompound compound = super.writeToNBT(nbttagcompound);
        compound.setInteger("levelMech",level);
        return compound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        level = nbttagcompound.getInteger("levelMech");
    }


    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @Override
    public void init() {
        Recipes.recipes.getRecipeFluid().addRecipe("electrolyzer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidRegistry.WATER, 4)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidhyd.getInstance(),
                        2
                ), new FluidStack(FluidName.fluidoxy.getInstance(), 1))));
        Recipes.recipes.getRecipeFluid().addRecipe("electrolyzer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidfluorhyd.getInstance(), 5)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidhyd.getInstance(),
                        3
                ), new FluidStack(FluidName.fluidfluor.getInstance(), 1))));

        Recipes.recipes.getRecipeFluid().addRecipe("electrolyzer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidhydrogenbromide.getInstance(), 5)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidhyd.getInstance(),
                        3
                ), new FluidStack(FluidName.fluidbromine.getInstance(), 1))));

        Recipes.recipes.getRecipeFluid().addRecipe("electrolyzer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidwastesulfuricacid.getInstance(), 2)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidsulfurtrioxide.getInstance(),
                        1
                ), new FluidStack(FluidRegistry.WATER, 1))));


    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.electrolyzer_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
    }

    @Override
    public ContainerElectrolyzer getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerElectrolyzer(entityPlayer, this);

    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            setOverclockRates();
            this.fluid_handler.load();
        }
    }

    public void onUnloaded() {
        super.onUnloaded();

    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiElectrolyzer(new ContainerElectrolyzer(entityPlayer, this));

    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 25 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip);

    }

    public void updateEntityServer() {
        super.updateEntityServer();
        MutableObject<ItemStack> output1 = new MutableObject<>();
        boolean check = false;
        if (this.fluidTank1.getFluidAmount() + 1000 <= this.fluidTank1.getCapacity() && this.fluidSlot1.transferToTank(
                this.fluidTank1,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot.canAdd(output1.getValue()))) {
            this.fluidSlot1.transferToTank(this.fluidTank1, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot.add(output1.getValue());
            }
            check = true;
        }
        if (this.fluidTank2.getFluidAmount() - 1000 >= 0 && this.fluidSlot2.transferFromTank(
                this.fluidTank2,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot.canAdd(output1.getValue()))) {
            this.fluidSlot2.transferFromTank(this.fluidTank2, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot.add(output1.getValue());
            }
            check = true;
        }
        if (this.fluidTank3.getFluidAmount() - 1000 >= 0 && this.fluidSlot3.transferFromTank(
                this.fluidTank3,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot.canAdd(output1.getValue()))) {
            this.fluidSlot3.transferFromTank(this.fluidTank3, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot.add(output1.getValue());
            }
        }
        if (check || (this.fluid_handler.output() == null && this.fluidTank1.getFluidAmount() >= 1)) {
            this.fluid_handler.getOutput();
        } else {
            if (this.fluid_handler.output() != null && !this.fluid_handler.checkFluids()) {
                this.fluid_handler.setOutput(null);
            }
        }

        if (this.cathodeslot.isEmpty() || this.anodeslot.isEmpty()) {
            if (this.getActive()) {
                this.setActive(false);
                initiate(2);
            }

        } else {


            boolean drain = false;
            boolean drain1 = false;
            if (this.fluid_handler.output() != null && this.fluid_handler.canOperate() && this.fluid_handler.canFillFluid() && this.energy.canUseEnergy(
                    25)) {
                final BaseFluidMachineRecipe output = this.fluid_handler.output();
                final FluidStack inputFluidStack = output.input.getInputs().get(0);
                int size = this.getFluidTank(0).getFluidAmount() / inputFluidStack.amount;
                size = Math.min(this.level + 1, size);
                int cap = this.getFluidTank(1).getCapacity() - this.getFluidTank(1).getFluidAmount();
                FluidStack outputFluidStack = output.output_fluid.get(0);
                cap /= outputFluidStack.amount;
                cap = Math.min(cap, size);
                int cap1 = this.getFluidTank(2).getCapacity() - this.getFluidTank(2).getFluidAmount();
                FluidStack outputFluidStack1 = output.output_fluid.get(1);
                cap1 /= outputFluidStack1.amount;
                size = Math.min(Math.min(size, cap1), cap);
                if (this.getFluidTank(1).getCapacity() - this.getFluidTank(1).getFluidAmount() >= outputFluidStack.amount) {
                    FluidStack fluidStack = new FluidStack(
                            outputFluidStack.getFluid(),
                            outputFluidStack.amount * size
                    );
                    this.fluidTank2.fill(fluidStack, true);
                    drain = true;

                }
                if (this.getFluidTank(2).getCapacity() - this.getFluidTank(2).getFluidAmount() >= outputFluidStack1.amount) {
                    FluidStack fluidStack = new FluidStack(
                            outputFluidStack1.getFluid(),
                            outputFluidStack1.amount * size
                    );
                    this.fluidTank3.fill(fluidStack, true);
                    drain1 = true;
                }
                if (drain || drain1) {
                    int drains = size * inputFluidStack.amount;
                    this.getFluidTank(0).drain(drains, true);
                    if (!this.getActive()) {
                        this.setActive(true);
                        initiate(0);
                    }
                    this.energy.useEnergy(25);


                    setActive(true);
                    ItemStack cathode = this.cathodeslot.get();
                    ItemStack anode = this.anodeslot.get();
                    if (cathode.getItemDamage() < cathode.getMaxDamage()) {
                        cathode.setItemDamage(cathode.getItemDamage() + 1);
                    }
                    if (anode.getItemDamage() < anode.getMaxDamage()) {
                        anode.setItemDamage(anode.getItemDamage() + 1);
                    }
                    if (cathode.getItemDamage() == cathode.getMaxDamage()) {
                        this.cathodeslot.consume(1);
                    }
                    if (anode.getItemDamage() == anode.getMaxDamage()) {
                        this.anodeslot.consume(1);
                    }
                } else {
                    setActive(false);
                }


            } else {
                setActive(false);
            }
        }

        if (this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }

    }

    public void setOverclockRates() {
        int tier = this.upgradeSlot.getTier(1);
        this.energy.setSinkTier(tier);
        this.energy.setCapacity(this.upgradeSlot.getEnergyStorage(
                24000
        ));
    }

    public FluidTank getFluidTank(int i) {
        switch (i) {
            case 1:
                return this.fluidTank2;
            case 2:
                return this.fluidTank3;
            default:
                return this.fluidTank1;
        }
    }

    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (level < 10) {
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation)) {
                return super.onActivated(player, hand, side, hitX, hitY, hitZ);
            } else {
                stack.shrink(1);
                this.level++;
                return true;
            }
        } else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final int level) {
        this.level = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.level -= level;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.FluidExtract,
                UpgradableProperty.FluidInput, UpgradableProperty.ItemInput
        );
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.electrolyzer.getSoundEvent();
    }

}
