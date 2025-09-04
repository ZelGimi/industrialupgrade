package com.denfop.blockentity.mechanism.steam;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.widget.IType;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSteamHandlerHeavyOre;
import com.denfop.inventory.Inventory;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSteamHandlerHeavyOre;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntitySteamHandlerHeavyOre extends BlockEntityElectricMachine
        implements IUpdateTick, IType {


    public final InventoryOutput outputSlot;
    public final ComponentSteamProcess componentProcess;
    public final ComponentProgress componentProgress;
    public final Fluids.InternalFluidTank fluidTank;
    public final PressureComponent pressure;
    public final ComponentSteamEnergy steam;
    public final HeatComponent heat;
    private final EnumTypeStyle enumTypeSlot;
    private final double coef;
    private final Fluids fluids;
    public InventoryRecipes inputSlotA;
    public MachineRecipe output;
    private int[] col;
    private boolean work = false;


    public BlockEntitySteamHandlerHeavyOre(
            BlockPos pos, BlockState state) {
        super(0, 1, 1, BlockBaseMachine3Entity.steam_handler_ore, pos, state);
        this.enumTypeSlot = EnumTypeStyle.DEFAULT;
        this.outputSlot = new InventoryOutput(this, 3 + 2 * enumTypeSlot.ordinal());
        this.inputSlotA = new InventoryRecipes(this, "handlerho", this);
        this.col = new int[0];
        this.coef = getCoef();
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = fluids.addTank("fluidTank2", 4000, Inventory.TypeItemSlot.NONE, Fluids.fluidPredicate(
                FluidName.fluidsteam.getInstance().get()
        ));
        this.heat = this.addComponent(HeatComponent
                .asBasicSink(this, 3000));
        this.pressure = this.addComponent(PressureComponent.asBasicSink(this, 3));
        this.steam = this.addComponent(ComponentSteamEnergy.asBasicSink(this, 4000));
        this.steam.setFluidTank(fluidTank);
        this.componentProcess = this.addComponent(new ComponentSteamProcess(this, (int) (300 / this.getSpeed()),
                1, 3
        ) {
            @Override
            public void operateWithMax(final MachineRecipe output) {
                if (output.getRecipe() == null) {
                    return;
                }
                for (int i = 0; i < 1; i++) {
                    operate(output);
                }

            }

            @Override
            public void operateOnce(final List<ItemStack> processResult) {
                for (int i = 0; i < col.length; i++) {
                    final RandomSource rand = level.random;
                    if ((100 - col[i]) <= rand.nextInt(100)) {
                        this.outputSlot.add(processResult.get(i));
                    }
                }
                this.invSlotRecipes.consume();
            }
        });

        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);

    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.steam_handler_ore;
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

        }
        tooltip.add(Localization.translate("iu.steam_separator"));
        super.addInformation(stack, tooltip);
    }


    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput
        );
    }

    @Override
    public void onNeighborChange(final BlockState neighbor, final BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (work) {
            if (this.pos.below().distSqr(neighborPos) == 0) {
                FluidState blockState = level.getFluidState(this.pos.below());
                if (blockState.getType() != net.minecraft.world.level.material.Fluids.EMPTY) {
                    this.work = blockState.getType().isSame(Fluids.LAVA);
                } else {
                    work = false;
                }
            }
        } else {
            if (this.pos.below().distSqr(neighborPos) == 0) {
                FluidState blockState = level.getFluidState(this.pos.below());
                if (blockState.getType() != net.minecraft.world.level.material.Fluids.EMPTY) {
                    this.work = blockState.getType().isSame(Fluids.LAVA);
                } else {
                    work = false;
                }
            }
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {

            FluidState blockState = level.getFluidState(this.pos.below());
            if (blockState.getType() != net.minecraft.world.level.material.Fluids.EMPTY) {
                this.work = blockState.getType().isSame(Fluids.LAVA);
            } else {
                work = false;
            }

        }
    }

    public void onUnloaded() {
        super.onUnloaded();
    }


    @Override
    public ContainerMenuSteamHandlerHeavyOre getGuiContainer(Player entityPlayer) {
        return new ContainerMenuSteamHandlerHeavyOre(entityPlayer, this);

    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenSteamHandlerHeavyOre((ContainerMenuSteamHandlerHeavyOre) menu);
    }

    public void operateOnce(List<ItemStack> processResult) {
        for (int i = 0; i < col.length; i++) {
            final RandomSource rand = level.random;
            if ((100 - col[i]) <= rand.nextInt(100)) {
                this.outputSlot.add(processResult.get(i));
            }
        }
        this.inputSlotA.consume();
    }

    public MachineRecipe getOutput() {

        this.output = this.inputSlotA.process();


        return this.output;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.work && this.getWorld().getGameTime() % 2 == 0) {
            this.heat.addEnergy(1);
        }
        if (this.getWorld().getGameTime() % 20 == 0) {
            this.heat.useEnergy(1);
        }
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
                col[i] = Math.max(0, Math.min(col[i], 95) - 5);
            }
        }
    }

}
