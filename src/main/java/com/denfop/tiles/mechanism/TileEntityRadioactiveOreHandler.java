package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.*;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerRadioactiveOreHandler;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiRadioactiveOreHandler;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityRadioactiveOreHandler extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    public final InvSlotRecipes inputSlotA;
    public final ComponentBaseEnergy componentRadiation;
    public final InvSlotOutput outputSlot1;
    public MachineRecipe output;

    public TileEntityRadioactiveOreHandler(BlockPos pos, BlockState state) {
        super(200, 1, 1,BlockBaseMachine3.radioactive_handler_ore,pos,state);
        Recipes.recipes.addInitRecipes(this);
        this.outputSlot1 = new InvSlotOutput(this, 1);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((TileEntityRadioactiveOreHandler) this.getParent()).componentProcess;
                this.energy = this.getParent().getComp(Energy.class);
                this.setOverclockRates(this.invSlot);
            }
        });
        this.addComponent(new SoilPollutionComponent(this, 0.25));
        this.addComponent(new AirPollutionComponent(this, 0.25));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 100
        ));

        this.inputSlotA = new InvSlotRecipes(this, "radioactive_handler", this);
        this.componentProcess = this.addComponent(new ComponentProcess(this, 200, 1) {
            @Override
            public boolean checkRecipe() {
                return componentRadiation.getEnergy() + 1 < componentRadiation.getCapacity();
            }

            @Override
            protected int getRadiationSize(final int size) {
                return (int) Math.min(size, componentRadiation.getCapacity() - componentRadiation.getEnergy());
            }

            public boolean canAddItemStack() {
                return this.outputSlot.canAdd(
                        this.updateTick
                                .getRecipeOutput()
                                .getRecipe()
                                .getOutput().items.get(0));
            }

            public void operateWithMax(MachineRecipe output) {
                if (output.getRecipe() == null) {
                    return;
                }
                int size = 64;
                final List<Integer> list = this.updateTick.getRecipeOutput().getList();
                if (this.invSlotRecipes.getRecipe().workbench()) {
                    size = 1;
                } else {
                    for (int i = 0; i < list.size(); i++) {

                        size = Math.min(
                                size,
                                this.invSlotRecipes.get(i).getCount() / list.get(i)
                        );
                    }
                }
                int maxSize = size;
                int count = this.outputSlot.get(0).isEmpty() ? output.getRecipe().output.items.get(0).getMaxStackSize() :
                        this.outputSlot.get(0).getMaxStackSize() - this.outputSlot.get(0).getCount();
                ItemStack outputStack = this.updateTick.getRecipeOutput().getRecipe().output.items.get(0);
                count = count / Math.max(outputStack.getCount(), 1);
                size = Math.min(size, count);
                size = Math.min(
                        size,
                        this.updateTick.getRecipeOutput().getRecipe().output.items.get(0).getItem().getMaxStackSize()
                );
                if (this.updateTick.getRecipeOutput().getRecipe().input.getFluid() != null) {
                    final int size1 = this.invSlotRecipes.getTank().getFluidAmount() / this.updateTick
                            .getRecipeOutput()
                            .getRecipe().input.getFluid().getAmount();
                    size = Math.min(size, size1);
                }
                size = Math.min(size, this.operationsPerTick);
                size = Math.min(this.getSESize(size), this.getRadiationSize(size));
                this.invSlotRecipes.consume(size, output);
                this.outputSlot.add(output.getRecipe().getOutput().items.get(0), size);
                for (int i = 0; i < size; i++) {
                    if (WorldBaseGen.random.nextInt(100) < output.getRecipe().output.metadata.getInt("random")) {
                        outputSlot1.add(output.getRecipe().getOutput().items.get(1));
                    }
                }
                this.consumeSE(size);
                this.consumeRadiation(size);
                if (maxSize == size) {
                    this.updateTick.setRecipeOutput(null);
                }
            }

            @Override
            protected void consumeRadiation(final int size) {
                componentRadiation.addEnergy(size);
            }

            @Override
            public boolean checkRadiation(final boolean consume) {
                return true;
            }
        });
        this.componentRadiation = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.RADIATION, this, 2000));
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);

    }

    public static void addRecipe(ItemStack container, ItemStack output, int random, ItemStack itemStack) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        final CompoundTag nbt = ModUtils.nbt();
        nbt.putInt("random", random);

        Recipes.recipes.addRecipe(
                "radioactive_handler",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container)),
                        new RecipeOutput(nbt, output, itemStack)
                )
        );
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getOperationsPerTick());
        }
        super.addInformation(stack, tooltip);

    }

    @Override
    public ContainerRadioactiveOreHandler getGuiContainer(final Player var1) {
        return new ContainerRadioactiveOreHandler(var1, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiRadioactiveOreHandler((ContainerRadioactiveOreHandler) menu);
    }

    @Override
    public void init() {


        addRecipe(new ItemStack(IUItem.classic_ore.getItem(3), 1), new ItemStack(IUItem.nuclear_res.getStack(21), 2), 5,
                new ItemStack(IUItem.nuclear_res.getStack(16), 1)
        );

        addRecipe(new ItemStack(IUItem.toriyore.getItem(), 1), new ItemStack(IUItem.nuclear_res.getStack(19), 1), 10,
                new ItemStack(IUItem.nuclear_res.getStack(14), 4)
        );

        addRecipe(new ItemStack(IUItem.radiationore.getItem(1), 1), new ItemStack(IUItem.nuclear_res.getStack(18), 1), 10,
                new ItemStack(IUItem.nuclear_res.getStack(13), 4)
        );

        addRecipe(new ItemStack(IUItem.radiationore.getItem(), 1), new ItemStack(IUItem.nuclear_res.getStack(17), 1), 10,
                new ItemStack(IUItem.nuclear_res.getStack(15), 4)
        );

        addRecipe(new ItemStack(IUItem.radiationore.getItem(2), 1), new ItemStack(IUItem.nuclear_res.getStack(20), 1), 20,
                new ItemStack(IUItem.nuclear_res.getStack(16), 4)
        );
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.radioactive_handler_ore;
    }

    @Override
    public void onUpdate() {

    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            inputSlotA.load();
            this.getOutput();
        }


    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        return this.output;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }


}
