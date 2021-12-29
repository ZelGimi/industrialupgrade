package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.init.Localization;
import ic2.core.item.type.CraftingItemType;
import ic2.core.ref.ItemName;
import net.minecraft.item.ItemStack;

public class TileEntityAssamplerScrap extends TileEntityMultiMachine {

    public TileEntityAssamplerScrap() {
        super(
                EnumMultiMachine.AssamplerScrap.usagePerTick,
                EnumMultiMachine.AssamplerScrap.lenghtOperation,
                Recipes.createscrap,
                3
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.createscrap);
    }

    public static void init() {
        addrecipe(
                new ItemStack(ItemName.crafting.getItemStack(CraftingItemType.rubber).getItem(), 9, 23),
                new ItemStack(ItemName.crafting.getItemStack(CraftingItemType.rubber).getItem(), 1, 24)
        );
        addrecipe(
                new ItemStack(ItemName.crafting.getItemStack(CraftingItemType.rubber).getItem(), 9, 24),
                new ItemStack(IUItem.doublescrapBox, 1)
        );
    }

    public static void addrecipe(ItemStack input, ItemStack output) {
        final IRecipeInputFactory input1 = ic2.api.recipe.Recipes.inputFactory;

        Recipes.createscrap.addRecipe(input1.forStack(input), null, false, output);
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.AssamplerScrap;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockAssamplerScrap.name");
    }

    public String getStartSoundFile() {
        return "Machines/AssamplerScrap.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
