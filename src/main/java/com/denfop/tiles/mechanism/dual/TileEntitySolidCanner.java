package com.denfop.tiles.mechanism.dual;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiSolidCanner;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileEntityDoubleElectricMachine;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.item.type.CraftingItemType;
import ic2.core.item.type.NuclearResourceType;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntitySolidCanner extends TileEntityDoubleElectricMachine implements IHasRecipe {

    public TileEntitySolidCanner() {
        super(2, 200, 1, EnumDoubleElectricMachine.CANNING);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addBottleRecipe(ItemStack container, ItemStack fill, ItemStack output) {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "cannerbottle",
                new BaseMachineRecipe(
                        new Input(input.forStack(container), input.forStack(fill)),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addBottleRecipe(ItemStack container, Item fill, ItemStack output) {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "cannerbottle",
                new BaseMachineRecipe(
                        new Input(input.forStack(container), input.forStack(new ItemStack(fill))),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addBottleRecipe(ItemStack container, int i, ItemStack fill, ItemStack output) {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "cannerbottle",
                new BaseMachineRecipe(
                        new Input(input.forStack(container, i), input.forStack(fill)),
                        new RecipeOutput(null, output)
                )
        );
    }

    public static void addBottleRecipe(ItemStack container, ItemStack fill, int i, ItemStack output) {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "cannerbottle",
                new BaseMachineRecipe(
                        new Input(input.forStack(container), input.forStack(fill, i)),
                        new RecipeOutput(null, output)
                )
        );
    }


    public void init() {
        ItemStack fuelRod = ItemName.crafting.getItemStack(CraftingItemType.fuel_rod);
        addBottleRecipe(
                fuelRod,
                ItemName.nuclear.getItemStack(NuclearResourceType.uranium),
                ItemName.uranium_fuel_rod.getItemStack()
        );
        addBottleRecipe(fuelRod, ItemName.nuclear.getItemStack(NuclearResourceType.mox), ItemName.mox_fuel_rod.getItemStack());
        ItemStack tinCan = ItemName.crafting.getItemStack(CraftingItemType.tin_can);
        ItemStack filledTinCan = ItemName.filled_tin_can.getItemStack();
        addBottleRecipe(tinCan, new ItemStack(Items.POTATO), filledTinCan);
        addBottleRecipe(tinCan, 2, new ItemStack(Items.COOKIE), StackUtil.copyWithSize(filledTinCan, 2));
        addBottleRecipe(tinCan, 2, new ItemStack(Items.MELON), StackUtil.copyWithSize(filledTinCan, 2));
        addBottleRecipe(tinCan, 2, new ItemStack(Items.FISH), StackUtil.copyWithSize(filledTinCan, 2));
        addBottleRecipe(tinCan, 2, new ItemStack(Items.CHICKEN), StackUtil.copyWithSize(filledTinCan, 2));
        addBottleRecipe(tinCan, 3, new ItemStack(Items.PORKCHOP), StackUtil.copyWithSize(filledTinCan, 3));
        addBottleRecipe(tinCan, 3, new ItemStack(Items.BEEF), StackUtil.copyWithSize(filledTinCan, 3));
        addBottleRecipe(tinCan, 4, new ItemStack(Items.APPLE), StackUtil.copyWithSize(filledTinCan, 4));
        addBottleRecipe(tinCan, 4, new ItemStack(Items.CARROT), StackUtil.copyWithSize(filledTinCan, 4));
        addBottleRecipe(tinCan, 5, new ItemStack(Items.BREAD), StackUtil.copyWithSize(filledTinCan, 5));
        addBottleRecipe(tinCan, 5, new ItemStack(Items.COOKED_FISH), StackUtil.copyWithSize(filledTinCan, 5));
        addBottleRecipe(tinCan, 6, new ItemStack(Items.COOKED_CHICKEN), StackUtil.copyWithSize(filledTinCan, 6));
        addBottleRecipe(tinCan, 6, new ItemStack(Items.BAKED_POTATO), StackUtil.copyWithSize(filledTinCan, 6));
        addBottleRecipe(tinCan, 6, new ItemStack(Items.MUSHROOM_STEW), StackUtil.copyWithSize(filledTinCan, 6));
        addBottleRecipe(tinCan, 6, new ItemStack(Items.PUMPKIN_PIE), StackUtil.copyWithSize(filledTinCan, 6));
        addBottleRecipe(tinCan, 8, new ItemStack(Items.COOKED_PORKCHOP), StackUtil.copyWithSize(filledTinCan, 8));
        addBottleRecipe(tinCan, 8, new ItemStack(Items.COOKED_BEEF), StackUtil.copyWithSize(filledTinCan, 8));
        addBottleRecipe(tinCan, 12, new ItemStack(Items.CAKE), StackUtil.copyWithSize(filledTinCan, 12));
        addBottleRecipe(tinCan, new ItemStack(Items.POISONOUS_POTATO), 2, filledTinCan);
        addBottleRecipe(tinCan, new ItemStack(Items.ROTTEN_FLESH), 2, filledTinCan);


    }

    @Override
    public void operateOnce(MachineRecipe output, List<ItemStack> processResult) {
        this.inputSlotA.consume();
        this.outputSlot.add(processResult);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiSolidCanner(new ContainerDoubleElectricMachine(entityPlayer, this, type));
    }

    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


}
