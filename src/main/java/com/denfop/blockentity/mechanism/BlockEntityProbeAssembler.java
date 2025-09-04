package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.*;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuProbeAssembler;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenProbeAssembler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockEntityProbeAssembler extends BlockEntityElectricMachine implements
        BlockEntityUpgrade, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    private static final List<AABB> aabbs = Collections.singletonList(new AABB(-0.3, 0.0D, -0.2, 1.3, 2.0D,
            1.2
    ));
    public final InventoryUpgrade upgradeSlot;
    public final ComponentUpgradeSlots componentUpgrade;
    public final ComponentProgress componentProgress;
    public final ComponentProcess componentProcess;
    public final InventoryRecipes inputSlotA;
    public final ComponentUpgrade componentUpgrades;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public MachineRecipe output;

    public BlockEntityProbeAssembler(BlockPos pos, BlockState state) {
        super(800, 1, 1, BlockBaseMachine3Entity.probe_assembler, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 800
        ));
        this.inputSlotA = new InventoryRecipes(this, "probeassembler", this) {
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

    public static void addRecipe(ItemStack container, ItemStack fill1, ItemStack fill2, ItemStack fill3, ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "probeassembler",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(691), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(701), 1)),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill1),
                                input.getInput(container),
                                input.getInput(fill2),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(694), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(698), 1)),
                                input.getInput(fill2),
                                input.getInput(container),
                                input.getInput(fill1),
                                input.getInput(fill1),
                                input.getInput(container),
                                input.getInput(fill2),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(690), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(705), 1)),
                                input.getInput(fill2),
                                input.getInput(container),
                                input.getInput(fill1),
                                input.getInput(fill1),
                                input.getInput(container),
                                input.getInput(fill2),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill2),
                                input.getInput(container),
                                input.getInput(fill1),
                                input.getInput(container),
                                input.getInput(fill3),
                                input.getInput(fill3),
                                input.getInput(container),
                                input.getInput(fill3),
                                input.getInput(fill3)
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
    public ContainerMenuProbeAssembler getGuiContainer(final Player var1) {
        return new ContainerMenuProbeAssembler(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenProbeAssembler((ContainerMenuProbeAssembler) menu);
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
    public Set<EnumBlockEntityUpgrade> getUpgradableProperties() {
        return EnumSet.of(
                EnumBlockEntityUpgrade.Processing,
                EnumBlockEntityUpgrade.Transformer,
                EnumBlockEntityUpgrade.EnergyStorage,
                EnumBlockEntityUpgrade.ItemExtract,
                EnumBlockEntityUpgrade.ItemInput
        );
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.probe_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    public List<AABB> getAabbs(boolean forCollision) {
        return aabbs;
    }

    @Override
    public void init() {
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(726)), new ItemStack(IUItem.crafting_elements.getStack(704)),
                new ItemStack(IUItem.crafting_elements.getStack(706)), new ItemStack(IUItem.crafting_elements.getStack(695)),
                new ItemStack(IUItem.probe.getItem())
        );
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(707)), new ItemStack(IUItem.crafting_elements.getStack(697)),
                new ItemStack(IUItem.crafting_elements.getStack(693)), new ItemStack(IUItem.crafting_elements.getStack(702)),
                new ItemStack(IUItem.adv_probe.getItem())
        );
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(727)), new ItemStack(IUItem.crafting_elements.getStack(697)),
                new ItemStack(IUItem.crafting_elements.getStack(703)), new ItemStack(IUItem.crafting_elements.getStack(692)),
                new ItemStack(IUItem.imp_probe.getItem())
        );
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(711)), new ItemStack(IUItem.crafting_elements.getStack(696)),
                new ItemStack(IUItem.crafting_elements.getStack(700)), new ItemStack(IUItem.crafting_elements.getStack(699)),
                new ItemStack(IUItem.per_probe.getItem())
        );
    }

}
