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
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentTimer;
import com.denfop.container.ContainerMoonSpotter;
import com.denfop.container.ContainerNightConverter;
import com.denfop.gui.GuiMoonSpotter;
import com.denfop.gui.GuiNightConverter;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Timer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityNightConverter extends TileElectricMachine implements
        IUpgradableBlock, IUpdateTick, IUpdatableTileEvent {


    public final InvSlotRecipes inputSlotA;
    public final ComponentBaseEnergy ne;
    public final ComponentProgress progress;
    public MachineRecipe output;

    public TileEntityNightConverter() {
        super(0, 14, 1);
        inputSlotA = new InvSlotRecipes(this, "solar_glass_recipe", this);
        inputSlotA.setStackSizeLimit(1);
        this.ne = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.NIGHT,this,10000));
        this.progress = this.addComponent(new ComponentProgress(this,1,80));
    }

    public static void addRecipe(int container) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "solar_glass_recipe",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.solar_day_glass, 1, container))),
                        new RecipeOutput(null, new ItemStack(IUItem.solar_night_glass, 1, container))
                )
        );
    }







    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.night_converter;
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
        if (this.ne.getEnergy() >= 62.5 && this.output != null) {
            this.progress.addProgress();
            this.setActive(true);
            this.ne.useEnergy(62.5);
            if (this.progress.getBar() >= 1) {
                this.progress.setProgress((short) 0);
                this.inputSlotA.consume();
                this.outputSlot.add(this.output.getRecipe().output.items.get(0));
                getOutput();
            }
        }else{
            this.setActive(false);
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
    public ContainerNightConverter getGuiContainer(final EntityPlayer var1) {
        return new ContainerNightConverter(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiNightConverter(getGuiContainer(var1));
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }


}
