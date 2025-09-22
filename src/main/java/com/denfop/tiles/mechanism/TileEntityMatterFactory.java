package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.ComponentTimer;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerMatterFactory;
import com.denfop.gui.GuiMatterFactory;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Timer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMatterFactory extends TileElectricMachine implements IUpdateTick, IHasRecipe {

    public final InventoryRecipes inputSlotA;
    public final ComponentTimer timer;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public MachineRecipe output;

    public TileEntityMatterFactory() {
        super(2000, 14, 1);
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
                        new RecipeOutput(null, new ItemStack(IUItem.crafting_elements, 1, output))
                )
        );
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public ContainerMatterFactory getGuiContainer(final EntityPlayer var1) {
        return new ContainerMatterFactory(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiMatterFactory(getGuiContainer(var1));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.matter_factory;
    }

    @Override
    public void onUpdate() {

    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            inputSlotA.load();
            this.getOutput();
        }


    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.energy.getEnergy() < 1 && this.inputSlotA.get().isEmpty() || this.output == null || this.outputSlot
                .get()
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
        addRecipe(new ItemStack(IUItem.sunnarium, 2, 2), 423);
        addRecipe(new ItemStack(IUItem.sunnariumpanel, 2, 0), 395);
        addRecipe(new ItemStack(IUItem.sunnariumpanel, 2, 1), 313);
        addRecipe(new ItemStack(IUItem.sunnariumpanel, 2, 2), 348);
        addRecipe(new ItemStack(IUItem.sunnariumpanel, 2, 3), 409);
        addRecipe(new ItemStack(IUItem.sunnariumpanel, 2, 4), 384);
        addRecipe(new ItemStack(IUItem.sunnariumpanel, 2, 5), 388);
        addRecipe(new ItemStack(IUItem.sunnariumpanel, 2, 6), 332);
        addRecipe(new ItemStack(IUItem.sunnariumpanel, 2, 7), 432);
        addRecipe(new ItemStack(IUItem.sunnariumpanel, 2, 8), 361);
        addRecipe(new ItemStack(IUItem.sunnariumpanel, 2, 9), 309);
        addRecipe(new ItemStack(IUItem.sunnariumpanel, 2, 10), 304);
        addRecipe(new ItemStack(IUItem.sunnariumpanel, 2, 11), 318);
        addRecipe(new ItemStack(IUItem.sunnariumpanel, 2, 12), 353);
    }

}
