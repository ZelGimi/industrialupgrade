package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.*;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerRocketAssembler;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiRocketAssembler;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityRocketAssembler extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    private static final List<AABB> aabbs = Collections.singletonList(new AABB(-0.5, 0.0D, -0.5, 1.5, 2.0D,
            1.5
    ));
    public final InvSlotUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    public final InvSlotRecipes inputSlotA;
    public final ComponentUpgrade componentUpgrades;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public MachineRecipe output;

    public TileEntityRocketAssembler(BlockPos pos, BlockState state) {
        super(800, 1, 1, BlockBaseMachine3.rocket_assembler, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 800
        ));
        this.inputSlotA = new InvSlotRecipes(this, "rocketassembler", this) {
            @Override
            public int getStackSizeLimit() {
                return 1;
            }
        };
        this.componentProcess = this.addComponent(new ComponentProcess(this, 800, 1));
        this.componentProcess.setHasAudio(false);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);

        this.componentUpgrades = this.addComponent(new ComponentUpgrade(this, TypeUpgrade.INSTANT, TypeUpgrade.STACK));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public static void addRecipe(
            ItemStack container, ItemStack fill1, ItemStack fill2, ItemStack fill3, ItemStack fill4,
            ItemStack fill5, ItemStack output
    ) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "rocketassembler",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(753), 1)),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(758), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(747), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(732), 1)),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(698), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(733), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(765), 1)),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill1),
                                input.getInput(fill2),
                                input.getInput(fill1),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill3),
                                input.getInput(fill3),
                                input.getInput(fill3),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill4),
                                input.getInput(fill4),
                                input.getInput(fill4),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill5),
                                input.getInput(fill5),
                                input.getInput(fill5),
                                input.getInput(container),
                                input.getInput(fill5),
                                input.getInput(fill5),
                                input.getInput(fill5)
                        ),
                        new RecipeOutput(null, output)
                )
        );
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
    public ContainerRocketAssembler getGuiContainer(final Player var1) {
        return new ContainerRocketAssembler(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiRocketAssembler((ContainerRocketAssembler) menu);
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

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.rocket_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    public List<AABB> getAabbs(boolean forCollision) {
        return aabbs;
    }

    @Override
    public void init() {
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(726)), new ItemStack(IUItem.crafting_elements.getStack(745)),
                new ItemStack(IUItem.crafting_elements.getStack(742)), new ItemStack(IUItem.crafting_elements.getStack(744)),
                new ItemStack(IUItem.crafting_elements.getStack(743)), new ItemStack(IUItem.crafting_elements.getStack(695)),
                new ItemStack(IUItem.rocket.getItem())
        );
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(707)), new ItemStack(IUItem.crafting_elements.getStack(763)),
                new ItemStack(IUItem.crafting_elements.getStack(761)), new ItemStack(IUItem.crafting_elements.getStack(764))
                , new ItemStack(IUItem.crafting_elements.getStack(762)), new ItemStack(IUItem.crafting_elements.getStack(702)),
                new ItemStack(IUItem.adv_rocket.getItem())
        );
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(727)), new ItemStack(IUItem.crafting_elements.getStack(751)),
                new ItemStack(IUItem.crafting_elements.getStack(748)), new ItemStack(IUItem.crafting_elements.getStack(750)),
                new ItemStack(IUItem.crafting_elements.getStack(749)), new ItemStack(IUItem.crafting_elements.getStack(692)),
                new ItemStack(IUItem.imp_rocket.getItem())
        );
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(711)), new ItemStack(IUItem.crafting_elements.getStack(757)),
                new ItemStack(IUItem.crafting_elements.getStack(754)), new ItemStack(IUItem.crafting_elements.getStack(756)),
                new ItemStack(IUItem.crafting_elements.getStack(755)), new ItemStack(IUItem.crafting_elements.getStack(699)),
                new ItemStack(IUItem.per_rocket.getItem())
        );
    }

}
