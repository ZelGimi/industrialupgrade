package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.IType;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuHandlerHeavyOre;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.screen.ScreenHandlerHeavyOre;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ParticleUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public abstract class BlockEntityBaseHandlerHeavyOre extends BlockEntityElectricMachine
        implements BlockEntityUpgrade, IUpdateTick, IType {


    public final InventoryOutput outputSlot;
    public final InventoryUpgrade upgradeSlot;
    public final HeatComponent heat;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProcess componentProcess;
    public final ComponentProgress componentProgress;
    private final EnumTypeStyle enumTypeSlot;
    private final double coef;
    private final Inventory input_slot;
    private final ComponentUpgrade componentUpgrades;
    public InventoryRecipes inputSlotA;
    public MachineRecipe output;
    private boolean auto;
    private int[] col;


    public BlockEntityBaseHandlerHeavyOre(
            EnumTypeStyle enumTypeSlot, MultiBlockEntity block, BlockPos pos, BlockState state
    ) {
        super(300, 1, 1, block, pos, state);
        this.enumTypeSlot = enumTypeSlot;
        this.outputSlot = new InventoryOutput(this, 3 + 2 * Math.min(3, enumTypeSlot.ordinal()));
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.inputSlotA = new InventoryRecipes(this, "handlerho", this);
        this.heat = this.addComponent(HeatComponent
                .asBasicSink(this, 5000));
        this.col = new int[0];
        this.coef = getCoef();
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((BlockEntityBaseHandlerHeavyOre) this.getParent()).componentProcess;

            }
        });
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, (int) (300 / this.getSpeed()), 1) {
            @Override
            public void operateWithMax(final MachineRecipe output) {
                operate(output);

            }

            public void operateWithMax(MachineRecipe output, int size) {

                if (output.getRecipe() == null) {
                    return;
                }
                for (int i = 0; i < size; i++) {
                    operate(output);
                }
            }

            @Override
            public void operateOnce(final List<ItemStack> processResult) {
                for (int i = 0; i < col.length; i++) {
                    RandomSource rand = level.random;
                    if ((100 - col[i]) <= rand.nextInt(100)) {
                        this.outputSlot.add(processResult.get(i));
                    }
                }
                this.invSlotRecipes.consume();
            }
        });
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);
        this.input_slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (this.get(0).isEmpty()) {
                    ((BlockEntityBaseHandlerHeavyOre) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((BlockEntityBaseHandlerHeavyOre) this.base).inputSlotA.changeAccepts(this.get(0));
                }
                return content;
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }

            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.recipe_schedule.getItem();
            }
        };
        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));

    }


    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getActive() && this.level.getGameTime() % 5 == 0) {
            ParticleUtils.spawnMineralSeparatorParticles(level, pos, level.random);
        }
    }

    protected double getCoef() {
        switch (this.enumTypeSlot) {
            case ADVANCED:
                return 1.1;
            case PERFECT:
                return 1.3;
            case IMPROVED:
                return 1.2;
            default:
                return 1;
        }
    }

    @Override
    public EnumTypeStyle getStyle() {
        return this.enumTypeSlot;
    }


    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {

        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.heatmachine.info"));
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.componentProcess.getEnergyConsume() + Localization.translate(
                    "iu.machines_work_energy_type_eu"));
            tooltip.add(Localization.translate("iu.machines_work_length") + this.componentProcess.getOperationsPerTick());

        }
        super.addInformation(stack, tooltip);
    }


    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(EnumBlockEntityUpgrade.Processing, EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage, EnumBlockEntityUpgrade.ItemExtract, EnumBlockEntityUpgrade.ItemInput
        );
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            inputSlotA.load();
            this.getOutput();
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get(0));
            }
        }
    }

    public void onUnloaded() {
        super.onUnloaded();
    }


    @Override
    public ContainerMenuHandlerHeavyOre getGuiContainer(Player entityPlayer) {
        return new ContainerMenuHandlerHeavyOre(entityPlayer, this);

    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenHandlerHeavyOre((ContainerMenuHandlerHeavyOre) isAdmin);
    }


    public MachineRecipe getOutput() {

        this.output = this.inputSlotA.process();


        return this.output;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.handlerho.getSoundEvent();
    }

    @Override
    public void onUpdate() {

    }

    private double getSpeed() {
        switch (this.enumTypeSlot) {
            case ADVANCED:
                return 1.3;
            case IMPROVED:
                return 1.5;
            case PERFECT:
                return 2;
            default:
                return 1;
        }
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
        if (output == null) {
            this.col = new int[0];
        } else {
            this.col = new int[output.getRecipe().output.items.size()];
            for (int i = 0; i < col.length; i++) {
                col[i] = (int) (output.getRecipe().output.metadata.getInt(("input" + i)) * this.coef);
                col[i] = Math.min(col[i], 95);
            }
        }
    }

}
