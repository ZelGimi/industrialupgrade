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
import com.denfop.container.ContainerSatelliteAssembler;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSatelliteAssembler;
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

public class TileEntitySatelliteAssembler extends TileElectricMachine implements
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

    public TileEntitySatelliteAssembler(BlockPos pos, BlockState state) {
        super(800, 1, 1, BlockBaseMachine3.satellite_assembler, pos, state);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 800
        ));
        this.inputSlotA = new InvSlotRecipes(this, "satelliteassembler", this) {
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
                "satelliteassembler",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill1),
                                input.getInput(container),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(752), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(766), 1)),
                                input.getInput(fill2),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill3),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(739), 1)),
                                input.getInput(fill2),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(734), 1)),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill3),
                                input.getInput(fill3),
                                input.getInput(fill2),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(738), 1)),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(768), 1)),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill3),
                                input.getInput(new ItemStack(IUItem.crafting_elements.getStack(731), 1)),
                                input.getInput(fill3),
                                input.getInput(fill3),
                                input.getInput(container),
                                input.getInput(fill1),
                                input.getInput(container),
                                input.getInput(fill3),
                                input.getInput(fill3),
                                input.getInput(container),
                                input.getInput(fill1),
                                input.getInput(container),
                                input.getInput(container)
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
    public ContainerSatelliteAssembler getGuiContainer(final Player var1) {
        return new ContainerSatelliteAssembler(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiSatelliteAssembler((ContainerSatelliteAssembler) menu);
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
        return BlockBaseMachine3.satellite_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    public List<AABB> getAabbs(boolean forCollision) {
        return aabbs;
    }

    @Override
    public void init() {
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(726)), new ItemStack(IUItem.crafting_elements.getStack(729)),
                new ItemStack(IUItem.crafting_elements.getStack(746)), new ItemStack(IUItem.crafting_elements.getStack(740)),
                new ItemStack(IUItem.satellite.getItem())
        );
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(707)), new ItemStack(IUItem.crafting_elements.getStack(735)),
                new ItemStack(IUItem.crafting_elements.getStack(730)), new ItemStack(IUItem.crafting_elements.getStack(767)),
                new ItemStack(IUItem.adv_satellite.getItem())
        );
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(727)), new ItemStack(IUItem.crafting_elements.getStack(728)),
                new ItemStack(IUItem.crafting_elements.getStack(737)), new ItemStack(IUItem.crafting_elements.getStack(736)),
                new ItemStack(IUItem.imp_satellite.getItem())
        );
        addRecipe(new ItemStack(IUItem.crafting_elements.getStack(711)), new ItemStack(IUItem.crafting_elements.getStack(760)),
                new ItemStack(IUItem.crafting_elements.getStack(759)), new ItemStack(IUItem.crafting_elements.getStack(741)),
                new ItemStack(IUItem.per_satellite.getItem())
        );
    }

}
