package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgrade;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.componets.TypeUpgrade;
import com.denfop.container.ContainerBattery;
import com.denfop.container.ContainerRoverAssembler;
import com.denfop.gui.GuiBattery;
import com.denfop.gui.GuiRoverAssembler;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityRoverAssembler extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(-0.3, 0.0D, -0.2, 1.3, 2.0D,
            1.2
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
    public TileEntityRoverAssembler() {
        super(800, 1, 1);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 800
        ));
        this.inputSlotA = new InvSlotRecipes(this, "roverassembler", this){
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
    @Override
    public void onUpdate() {

    }
    @Override
    public ContainerRoverAssembler getGuiContainer(final EntityPlayer var1) {
        return new ContainerRoverAssembler(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiRoverAssembler(getGuiContainer(var1));
    }

    public static void addRecipe(ItemStack container, ItemStack fill1, ItemStack fill2,ItemStack fill3,ItemStack output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "roverassembler",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill1),
                                input.getInput(fill1),
                                input.getInput(fill1),
                                input.getInput(fill1),
                                input.getInput(fill1),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill1),
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,710)),
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,724)),
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,723)),
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,713)),
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,722)),
                                input.getInput(fill1),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill1),
                                input.getInput(fill2),
                                input.getInput(fill1),
                                input.getInput(fill2),
                                input.getInput(fill1),
                                input.getInput(fill2),
                                input.getInput(fill1),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill1),
                                input.getInput(container),
                                input.getInput(fill1),
                                input.getInput(container),
                                input.getInput(fill1),
                                input.getInput(container),
                                input.getInput(fill3),
                                input.getInput(fill3),
                                input.getInput(fill3)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }
    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
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
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.rover_assembler;
    }
    @Override
    public void init() {
        addRecipe(new ItemStack(IUItem.crafting_elements,1,726),new ItemStack(IUItem.crafting_elements,1,714),
                new ItemStack(IUItem.crafting_elements,1,716),new ItemStack(IUItem.crafting_elements,1,709),
                new ItemStack(IUItem.rover));
        addRecipe(new ItemStack(IUItem.crafting_elements,1,707),new ItemStack(IUItem.crafting_elements,1,725),
                new ItemStack(IUItem.crafting_elements,1,719),new ItemStack(IUItem.crafting_elements,1,720),
                new ItemStack(IUItem.adv_rover));
        addRecipe(new ItemStack(IUItem.crafting_elements,1,727),new ItemStack(IUItem.crafting_elements,1,708),
                new ItemStack(IUItem.crafting_elements,1,712),new ItemStack(IUItem.crafting_elements,1,715),
                new ItemStack(IUItem.imp_rover));
        addRecipe(new ItemStack(IUItem.crafting_elements,1,711),new ItemStack(IUItem.crafting_elements,1,721),
                new ItemStack(IUItem.crafting_elements,1,718),new ItemStack(IUItem.crafting_elements,1,717),
                new ItemStack(IUItem.per_rover));
    }
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return aabbs;
    }

}
