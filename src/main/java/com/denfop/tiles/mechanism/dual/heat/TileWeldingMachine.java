package com.denfop.tiles.mechanism.dual.heat;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiWelding;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileWeldingMachine extends TileDoubleElectricMachine implements IHasRecipe {


    public TileWeldingMachine() {
        super(1, 140, 1, EnumDoubleElectricMachine.WELDING);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addRecipe(ItemStack fill, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.crafting_elements, 1, 122)), input.getInput(fill)),
                new RecipeOutput(nbt, output)
        ));
    }

    public static void addRecipe(ItemStack container, ItemStack fill, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.getInput(container), input.getInput(fill)),
                new RecipeOutput(nbt, output)
        ));
    }

    public static void addRecipe(String fill, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.crafting_elements, 1, 122)), input.getInput(fill)),
                new RecipeOutput(nbt, output)
        ));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.welding;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.welding.getSoundEvent();
    }

    public void init() {

        addRecipe("plateLead", new ItemStack(IUItem.coolpipes, 1, 0), 1000);
        addRecipe("plateDenseIron", new ItemStack(IUItem.coolpipes, 1, 1), 2000);
        addRecipe("plateSteel", new ItemStack(IUItem.coolpipes, 1, 2), 3000);
        addRecipe("plateDenseSteel", new ItemStack(IUItem.coolpipes, 1, 3), 4000);
        addRecipe("doubleplateRedbrass", new ItemStack(IUItem.coolpipes, 1, 4), 5000);

        addRecipe("plateAluminium", new ItemStack(IUItem.pipes, 1, 0), 1000);
        addRecipe("doubleplateAluminium", new ItemStack(IUItem.pipes, 1, 1), 2000);
        addRecipe("plateDuralumin", new ItemStack(IUItem.pipes, 1, 2), 3000);
        addRecipe("doubleplateDuralumin", new ItemStack(IUItem.pipes, 1, 3), 4000);
        addRecipe("doubleplateAlcled", new ItemStack(IUItem.pipes, 1, 4), 5000);

        for (int i = 0; i < 5; i++) {
            addRecipe(
                    new ItemStack(IUItem.coolpipes, 1, i),
                    new ItemStack(IUItem.pipes, 1, i),
                    new ItemStack(IUItem.heatcold_pipes, 1
                            , i),
                    1000 + 1000 * i
            );
        }

    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiWelding(new ContainerDoubleElectricMachine(entityPlayer, this, this.type));
    }


    public String getStartSoundFile() {
        return "Machines/welding.ogg";
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


}
