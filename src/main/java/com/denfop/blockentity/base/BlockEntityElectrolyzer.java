package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine2Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuElectrolyzer;
import com.denfop.inventory.*;
import com.denfop.screen.ScreenElectrolyzer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.sound.EnumSound;
import com.denfop.utils.DamageHandler;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

;

public class BlockEntityElectrolyzer extends BlockEntityElectricMachine implements IManufacturerBlock, BlockEntityUpgrade, IHasRecipe {

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
    private int levelMech;

    public BlockEntityElectrolyzer(BlockPos pos, BlockState state) {
        super(24000, 1, 2, BlockBaseMachine2Entity.electrolyzer_iu, pos, state);
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

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        CompoundTag compoundTag = super.writeToNBT(nbttagcompound);
        compoundTag.putInt("levelMech", levelMech);
        return compoundTag;
    }

    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        levelMech = nbttagcompound.getInt("levelMech");
    }

    @Override
    public void init() {
        Recipes.recipes.getRecipeFluid().addRecipe("electrolyzer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 4)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidhydrogen.getInstance().get(),
                        2
                ), new FluidStack(FluidName.fluidoxygen.getInstance().get(), 1))));
        Recipes.recipes.getRecipeFluid().addRecipe("electrolyzer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidfluorhyd.getInstance().get(), 5)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidhydrogen.getInstance().get(),
                        3
                ), new FluidStack(FluidName.fluidfluor.getInstance().get(), 1))));

        Recipes.recipes.getRecipeFluid().addRecipe("electrolyzer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidhydrogenbromide.getInstance().get(), 5)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidhydrogen.getInstance().get(),
                        3
                ), new FluidStack(FluidName.fluidbromine.getInstance().get(), 1))));

        Recipes.recipes.getRecipeFluid().addRecipe("electrolyzer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidwastesulfuricacid.getInstance().get(), 2)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidsulfurtrioxide.getInstance().get(),
                        1
                ), new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 1))));


    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine2Entity.electrolyzer_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1.getBlock(this.getTeBlock().getId());
    }

    @Override
    public ContainerMenuElectrolyzer getGuiContainer(Player entityPlayer) {
        return new ContainerMenuElectrolyzer(entityPlayer, this);

    }

    public void onLoaded() {
        super.onLoaded();
        if (!getWorld().isClientSide) {
            setOverclockRates();
            this.fluid_handler.load();
        }
    }

    public void onUnloaded() {
        super.onUnloaded();

    }


    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenElectrolyzer((ContainerMenuElectrolyzer) isAdmin);

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
        if (this.getActive() && this.level.getGameTime() % 5 == 0) {
            ParticleUtils.spawnElectrolyzerParticles(level, pos, level.random);
        }
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
                int size = this.getFluidTank(0).getFluidAmount() / inputFluidStack.getAmount();
                size = Math.min(this.levelMech + 1, size);
                int cap = this.getFluidTank(1).getCapacity() - this.getFluidTank(1).getFluidAmount();
                FluidStack outputFluidStack = output.output_fluid.get(0);
                cap /= outputFluidStack.getAmount();
                cap = Math.min(cap, size);
                int cap1 = this.getFluidTank(2).getCapacity() - this.getFluidTank(2).getFluidAmount();
                FluidStack outputFluidStack1 = output.output_fluid.get(1);
                cap1 /= outputFluidStack1.getAmount();
                size = Math.min(Math.min(size, cap1), cap);
                if (this.getFluidTank(1).getCapacity() - this.getFluidTank(1).getFluidAmount() >= outputFluidStack.getAmount()) {
                    FluidStack fluidStack = new FluidStack(
                            outputFluidStack.getFluid(),
                            outputFluidStack.getAmount() * size
                    );
                    this.fluidTank2.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                    drain = true;

                }
                if (this.getFluidTank(2).getCapacity() - this.getFluidTank(2).getFluidAmount() >= outputFluidStack1.getAmount()) {
                    FluidStack fluidStack = new FluidStack(
                            outputFluidStack1.getFluid(),
                            outputFluidStack1.getAmount() * size
                    );
                    this.fluidTank3.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                    drain1 = true;
                }
                if (drain || drain1) {
                    int drains = size * inputFluidStack.getAmount();
                    this.getFluidTank(0).drain(drains, IFluidHandler.FluidAction.EXECUTE);
                    if (!this.getActive()) {
                        this.setActive(true);
                        initiate(0);
                    }
                    this.energy.useEnergy(25);


                    setActive(true);
                    ItemStack cathode = this.cathodeslot.get(0);
                    ItemStack anode = this.anodeslot.get(0);
                    if (cathode.getDamageValue() < cathode.getMaxDamage()) {
                        DamageHandler.damage(cathode, 1, null);
                    }
                    if (anode.getDamageValue() < anode.getMaxDamage()) {
                        DamageHandler.damage(anode, 1, null);
                    }
                    if (cathode.getDamageValue() == cathode.getMaxDamage()) {
                        this.cathodeslot.consume(1);
                    }
                    if (anode.getDamageValue() == anode.getMaxDamage()) {
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
            final Player player,
            final InteractionHand hand,
            final Direction side,
            final Vec3 hitX
    ) {
        if (levelMech < 10) {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation.getItem())) {
                return super.onActivated(player, hand, side, hitX);
            } else {
                stack.shrink(1);
                this.levelMech++;
                return true;
            }
        } else {
            return super.onActivated(player, hand, side, hitX);
        }
    }

    @Override
    public int getLevelMechanism() {
        return this.levelMech;
    }

    @Override
    public void setLevelMech(final int levelMech) {
        this.levelMech = levelMech;
    }

    @Override
    public void removeLevel(final int level) {
        this.levelMech -= level;
    }

    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage, EnumBlockEntityUpgrade.FluidExtract,
                EnumBlockEntityUpgrade.FluidInput, EnumBlockEntityUpgrade.ItemInput
        );
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.electrolyzer.getSoundEvent();
    }

}
