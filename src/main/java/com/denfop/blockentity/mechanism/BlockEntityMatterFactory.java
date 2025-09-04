package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.*;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentTimer;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuMatterFactory;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenMatterFactory;
import com.denfop.utils.Timer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityMatterFactory extends BlockEntityElectricMachine implements IUpdateTick, IHasRecipe {

    public final InventoryRecipes inputSlotA;
    public final ComponentTimer timer;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public MachineRecipe output;

    public BlockEntityMatterFactory(BlockPos pos, BlockState state) {
        super(2000, 14, 1, BlockBaseMachine3Entity.matter_factory, pos, state);
        this.inputSlotA = new InventoryRecipes(this, "active_matter_factory", this);
        this.timer = this.addComponent(new ComponentTimer(this, new Timer(0, 30, 0)));
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    public static void addRecipe(ItemStack container, int output) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "active_matter_factory",
                new BaseMachineRecipe(
                        new Input(input.getInput(container)),
                        new RecipeOutput(null, new ItemStack(IUItem.crafting_elements.getStack(output), 1))
                )
        );
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public ContainerMenuMatterFactory getGuiContainer(final Player var1) {
        return new ContainerMenuMatterFactory(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenMatterFactory((ContainerMenuMatterFactory) menu);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.matter_factory;
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
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.energy.getEnergy() < 1 && this.inputSlotA.get(0).isEmpty() || this.output == null || this.outputSlot
                .get(0)
                .getCount() >= 64) {
            this.timer.setCanWork(false);
            this.setActive(false);
            return;
        }
        this.energy.useEnergy(1);
        this.setActive(true);
        if (!this.timer.isCanWork()) {
            this.timer.setCanWork(true);
        }
        if (this.timer.getTimers().get(0).getTime() <= 0) {
            this.inputSlotA.consume();
            this.outputSlot.add(this.output.getRecipe().output.items.get(0));
            getOutput();
            this.timer.resetTime();
        }
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
        this.timer.resetTime();
    }

    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();
        this.timer.resetTime();
        return this.output;
    }

    @Override
    public void init() {
        addRecipe(new ItemStack(IUItem.sunnarium.getStack(2), 2), 423);
        addRecipe(new ItemStack(IUItem.sunnariumpanel.getStack(0), 2), 395);
        addRecipe(new ItemStack(IUItem.sunnariumpanel.getStack(1), 2), 313);
        addRecipe(new ItemStack(IUItem.sunnariumpanel.getStack(2), 2), 348);
        addRecipe(new ItemStack(IUItem.sunnariumpanel.getStack(3), 2), 409);
        addRecipe(new ItemStack(IUItem.sunnariumpanel.getStack(4), 2), 384);
        addRecipe(new ItemStack(IUItem.sunnariumpanel.getStack(5), 2), 388);
        addRecipe(new ItemStack(IUItem.sunnariumpanel.getStack(6), 2), 332);
        addRecipe(new ItemStack(IUItem.sunnariumpanel.getStack(7), 2), 432);
        addRecipe(new ItemStack(IUItem.sunnariumpanel.getStack(8), 2), 361);
        addRecipe(new ItemStack(IUItem.sunnariumpanel.getStack(9), 2), 309);
        addRecipe(new ItemStack(IUItem.sunnariumpanel.getStack(10), 2), 304);
        addRecipe(new ItemStack(IUItem.sunnariumpanel.getStack(11), 2), 318);
        addRecipe(new ItemStack(IUItem.sunnariumpanel.getStack(12), 2), 353);
    }

}
