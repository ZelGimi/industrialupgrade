package com.denfop.tiles.mechanism;


import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.Recipes;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GUIEnriched;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileEntityDoubleElectricMachine;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.RecipeOutput;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityEnrichment extends TileEntityDoubleElectricMachine {

    public TileEntityEnrichment() {
        super(1, 300, 1, Localization.translate("iu.enrichment.name"), EnumDoubleElectricMachine.ENRICH);
    }

    public static void init() {
        addenrichment(
                new ItemStack(IUItem.toriy),
                new ItemStack(Items.GLOWSTONE_DUST),
                new ItemStack(IUItem.radiationresources, 1, 4)
        );
        addenrichment(
                new ItemStack(Blocks.GLOWSTONE, 1),
                "ingotUranium",
                new ItemStack(IUItem.itemSSP, 1, 0)
        );
        addenrichment(new ItemStack(IUItem.itemSSP, 1, 0), Ic2Items.reinforcedGlass, new ItemStack(IUItem.itemSSP, 2, 1));
        addenrichment(new ItemStack(IUItem.Helium, 1), new ItemStack(IUItem.cell_all, 1), new ItemStack(IUItem.cell_all, 4, 2));
        addenrichment(
                new ItemStack(IUItem.sunnarium, 1, 3),
                new ItemStack(IUItem.itemSSP, 1, 0),
                new ItemStack(IUItem.sunnarium, 1, 0)
        );

    }

    public static void addenrichment(ItemStack container, ItemStack fill, ItemStack output) {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.enrichment.addRecipe(input.forStack(container), input.forStack(fill), null, output);

    }

    public static void addenrichment(ItemStack container, String fill, ItemStack output) {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.enrichment.addRecipe(input.forStack(container), input.forOreDict(fill), null, output);

    }

    @Override
    public void operateOnce(RecipeOutput output, List<ItemStack> processResult) {
        this.inputSlotA.consume(0);
        this.outputSlot.add(processResult);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIEnriched(new ContainerDoubleElectricMachine(entityPlayer, this, type));
    }

    public String getStartSoundFile() {
        return "Machines/enrichment.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


}
