package com.denfop.tiles.mechanism.dual.heat;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiWelding;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileEntityDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInputFactory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityWeldingMachine extends TileEntityDoubleElectricMachine implements IHasRecipe {


    private boolean auto;

    public TileEntityWeldingMachine() {
        super(1, 140, 1, EnumDoubleElectricMachine.WELDING);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addRecipe(ItemStack fill, ItemStack output, int temperature) {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;

        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.forStack(new ItemStack(IUItem.crafting_elements, 1, 122)), input.forStack(fill)),
                new RecipeOutput(nbt, output)
        ));
    }

    public static void addRecipe(ItemStack container, ItemStack fill, ItemStack output, int temperature) {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;

        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.forStack(container), input.forStack(fill)),
                new RecipeOutput(nbt, output)
        ));
    }

    public static void addRecipe(String fill, ItemStack output, int temperature) {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;

        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.forStack(new ItemStack(IUItem.crafting_elements, 1, 122)), input.forOreDict(fill)),
                new RecipeOutput(nbt, output)
        ));
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

    protected List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.auto) {
            ret.add(new ItemStack(IUItem.autoheater));
            this.auto = false;
        }
        return ret;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.auto = nbttagcompound.getBoolean("auto");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("auto", this.auto);
        return nbttagcompound;
    }

    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        final ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem().equals(IUItem.autoheater) && !this.auto) {
            this.auto = true;
            stack.shrink(1);
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiWelding(new ContainerDoubleElectricMachine(entityPlayer, this, this.type));
    }

    @Override
    public void operateOnce(MachineRecipe output, List<ItemStack> processResult) {
        this.inputSlotA.consume();
        this.outputSlot.add(processResult);
    }

    public String getStartSoundFile() {
        return "Machines/welding.ogg";
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        if (this.auto) {
            if (this.heat.getEnergy() + 1 <= this.heat.getCapacity()) {
                this.heat.addEnergy(2);
            }
        }
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


}
