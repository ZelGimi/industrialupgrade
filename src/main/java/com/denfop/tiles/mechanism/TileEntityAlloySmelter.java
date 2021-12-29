package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.Recipes;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GUIAlloySmelter;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileEntityDoubleElectricMachine;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.RecipeOutput;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class TileEntityAlloySmelter extends TileEntityDoubleElectricMachine {

    public TileEntityAlloySmelter() {
        super(1, 300, 1, Localization.translate("iu.Alloymachine.name"), EnumDoubleElectricMachine.ALLOY_SMELTER);
    }

    public static void init() {

        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        addAlloysmelter(
                input.forStack(new ItemStack(Items.IRON_INGOT), 1),
                input.forStack(new ItemStack(Items.COAL), 2),
                new ItemStack(Ic2Items.advIronIngot.getItem(), 1, 5)
        );
        addAlloysmelter(
                input.forStack(new ItemStack(Items.GOLD_INGOT), 1),
                input.forOreDict("ingotSilver", 1),
                new ItemStack(
                        OreDictionary.getOres("ingotElectrum").get(0).getItem(),
                        2,
                        OreDictionary.getOres("ingotElectrum").get(0).getItemDamage()
                )
        );
        addAlloysmelter(
                input.forOreDict("ingotNickel", 1),
                input.forStack(new ItemStack(Items.IRON_INGOT), 2),
                new ItemStack(
                        OreDictionary.getOres("ingotInvar").get(0).getItem(),
                        3,
                        OreDictionary.getOres("ingotInvar").get(0).getItemDamage()
                )
        );

        addAlloysmelter(
                input.forOreDict("ingotCopper", 1),
                input.forOreDict("ingotZinc", 1),
                new ItemStack(IUItem.alloysingot, 1, 2)
        );
        addAlloysmelter(
                input.forOreDict("ingotNickel", 1),
                input.forOreDict("ingotChromium", 1),
                new ItemStack(IUItem.alloysingot, 1, 4)
        );
        addAlloysmelter(
                input.forOreDict("ingotAluminium", 1),
                input.forOreDict("ingotMagnesium", 1),
                new ItemStack(IUItem.alloysingot, 1, 8)
        );
        addAlloysmelter(
                input.forOreDict("ingotAluminium", 1),
                input.forOreDict("ingotTitanium", 1),
                new ItemStack(IUItem.alloysingot, 1, 1)
        );
        addAlloysmelter(
                input.forStack(new ItemStack(Items.IRON_INGOT), 1),
                input.forOreDict("ingotManganese", 1),
                new ItemStack(IUItem.alloysingot, 1, 9)
        );


    }

    public static void addAlloysmelter(IRecipeInput container, IRecipeInput fill, ItemStack output) {
        Recipes.Alloysmelter.addRecipe(container, fill, null, output);
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIAlloySmelter(new ContainerDoubleElectricMachine(entityPlayer, this, this.type));
    }

    @Override
    public void operateOnce(RecipeOutput output, List<ItemStack> processResult) {
        this.inputSlotA.consume(0);
        this.outputSlot.add(processResult);
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


}
