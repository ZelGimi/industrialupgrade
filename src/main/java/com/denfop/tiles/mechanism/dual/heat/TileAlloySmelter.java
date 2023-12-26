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
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiAlloySmelter;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.IInputItemStack;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class TileAlloySmelter extends TileDoubleElectricMachine implements IHasRecipe {


    public TileAlloySmelter() {
        super(1, 300, 1, EnumDoubleElectricMachine.ALLOY_SMELTER);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addAlloysmelter(IInputItemStack container, IInputItemStack fill, ItemStack output, int temperature) {
        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("alloysmelter", new BaseMachineRecipe(
                new Input(container, fill),
                new RecipeOutput(nbt, output)
        ));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.alloy_smelter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

    public void init() {

        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        addAlloysmelter(
                input.getInput(new ItemStack(Items.IRON_INGOT), 1),
                input.getInput(new ItemStack(Items.COAL), 2),
                IUItem.advIronIngot, 4000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(Items.GOLD_INGOT), 1),
                input.getInput("ingotSilver", 1),
                new ItemStack(
                        OreDictionary.getOres("ingotElectrum").get(0).getItem(),
                        2,
                        OreDictionary.getOres("ingotElectrum").get(0).getItemDamage()
                ), 3500
        );
        addAlloysmelter(
                input.getInput("ingotNickel", 1),
                input.getInput(new ItemStack(Items.IRON_INGOT), 2),
                new ItemStack(
                        OreDictionary.getOres("ingotInvar").get(0).getItem(),
                        3,
                        OreDictionary.getOres("ingotInvar").get(0).getItemDamage()
                ), 5000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(Items.COAL), 1),
                input.getInput(new ItemStack(Items.QUARTZ), 4),
                new ItemStack(IUItem.crafting_elements,1,319), 2000
        );
        addAlloysmelter(
                input.getInput("blockSilver", 1),
                input.getInput(ModUtils.getCellFromFluid(FluidName.fluidpolyprop.getInstance()), 1),
                new ItemStack(IUItem.crafting_elements,1,434), 2000
        );
        addAlloysmelter(
                input.getInput("ingotCopper", 1),
                input.getInput("ingotZinc", 1),
                new ItemStack(IUItem.alloysingot, 1, 2), 3000
        );
        addAlloysmelter(
                input.getInput("ingotNickel", 1),
                input.getInput("ingotChromium", 1),
                new ItemStack(IUItem.alloysingot, 1, 4), 4000
        );
        addAlloysmelter(
                input.getInput("ingotTin", 1),
                input.getInput("ingotCopper", 3),
                ModUtils.setSize(IUItem.bronzeIngot, 4), 1000
        );
        addAlloysmelter(
                input.getInput("ingotAluminium", 1),
                input.getInput("ingotMagnesium", 1),
                new ItemStack(IUItem.alloysingot, 1, 8), 2000
        );
        addAlloysmelter(
                input.getInput("ingotAluminum", 1),
                input.getInput("ingotMagnesium", 1),
                new ItemStack(IUItem.alloysingot, 1, 8), 2000
        );
        addAlloysmelter(
                input.getInput("ingotAluminium", 1),
                input.getInput("ingotTitanium", 1),
                new ItemStack(IUItem.alloysingot, 1, 1), 5000
        );
        addAlloysmelter(
                input.getInput("ingotAluminum", 1),
                input.getInput("ingotTitanium", 1),
                new ItemStack(IUItem.alloysingot, 1, 1), 5000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(Items.IRON_INGOT), 1),
                input.getInput("ingotManganese", 1),
                new ItemStack(IUItem.alloysingot, 1, 9), 4500
        );


    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAlloySmelter(new ContainerDoubleElectricMachine(entityPlayer, this, this.type));
    }

    public String getStartSoundFile() {
        return "Machines/alloysmelter.ogg";
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.alloysmelter.getSoundEvent();
    }

}
