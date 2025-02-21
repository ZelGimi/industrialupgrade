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
import com.denfop.container.ContainerSatelliteAssembler;
import com.denfop.gui.GuiRoverAssembler;
import com.denfop.gui.GuiSatelliteAssembler;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
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

public class TileEntitySatelliteAssembler  extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent, IHasRecipe {

    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(-0.5, 0.0D, -0.5, 1.5, 2.0D,
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
    public TileEntitySatelliteAssembler() {
        super(800, 1, 1);
        Recipes.recipes.addInitRecipes(this);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot));
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 800
        ));
        this.inputSlotA = new InvSlotRecipes(this, "satelliteassembler", this){
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
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,752)),
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,766)),
                                input.getInput(fill2),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill3),
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,739)),
                                input.getInput(fill2),
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,734)),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill3),
                                input.getInput(fill3),
                                input.getInput(fill2),
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,738)),
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,768)),
                                input.getInput(container),
                                input.getInput(container),
                                input.getInput(fill3),
                                input.getInput(new ItemStack(IUItem.crafting_elements,1,731)),
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
    public ContainerSatelliteAssembler getGuiContainer(final EntityPlayer var1) {
        return new ContainerSatelliteAssembler(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiSatelliteAssembler(getGuiContainer(var1));
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

    @Override
    public void init() {
        addRecipe(new ItemStack(IUItem.crafting_elements,1,726),new ItemStack(IUItem.crafting_elements,1,729),
                new ItemStack(IUItem.crafting_elements,1,746),new ItemStack(IUItem.crafting_elements,1,740),
                new ItemStack(IUItem.satellite));
        addRecipe(new ItemStack(IUItem.crafting_elements,1,707),new ItemStack(IUItem.crafting_elements,1,735),
                new ItemStack(IUItem.crafting_elements,1,730),new ItemStack(IUItem.crafting_elements,1,767),
                new ItemStack(IUItem.adv_satellite));
        addRecipe(new ItemStack(IUItem.crafting_elements,1,727),new ItemStack(IUItem.crafting_elements,1,728),
                new ItemStack(IUItem.crafting_elements,1,737),new ItemStack(IUItem.crafting_elements,1,736),
                new ItemStack(IUItem.imp_satellite));
        addRecipe(new ItemStack(IUItem.crafting_elements,1,711),new ItemStack(IUItem.crafting_elements,1,760),
                new ItemStack(IUItem.crafting_elements,1,759),new ItemStack(IUItem.crafting_elements,1,741),
                new ItemStack(IUItem.per_satellite));
    }

}
