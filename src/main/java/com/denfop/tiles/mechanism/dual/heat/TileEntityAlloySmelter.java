package com.denfop.tiles.mechanism.dual.heat;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiAlloySmelter;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileEntityDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class TileEntityAlloySmelter extends TileEntityDoubleElectricMachine implements IHasRecipe {


    private boolean auto;

    public TileEntityAlloySmelter() {
        super(1, 300, 1, EnumDoubleElectricMachine.ALLOY_SMELTER);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addAlloysmelter(IRecipeInput container, IRecipeInput fill, ItemStack output, int temperature) {
        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("alloysmelter", new BaseMachineRecipe(
                new Input(container, fill),
                new RecipeOutput(nbt, output)
        ));
    }

    public void init() {

        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        addAlloysmelter(
                input.forStack(new ItemStack(Items.IRON_INGOT), 1),
                input.forStack(new ItemStack(Items.COAL), 2),
                new ItemStack(Ic2Items.advIronIngot.getItem(), 1, 5), 4000
        );
        addAlloysmelter(
                input.forStack(new ItemStack(Items.GOLD_INGOT), 1),
                input.forOreDict("ingotSilver", 1),
                new ItemStack(
                        OreDictionary.getOres("ingotElectrum").get(0).getItem(),
                        2,
                        OreDictionary.getOres("ingotElectrum").get(0).getItemDamage()
                ), 3500
        );
        addAlloysmelter(
                input.forOreDict("ingotNickel", 1),
                input.forStack(new ItemStack(Items.IRON_INGOT), 2),
                new ItemStack(
                        OreDictionary.getOres("ingotInvar").get(0).getItem(),
                        3,
                        OreDictionary.getOres("ingotInvar").get(0).getItemDamage()
                ), 5000
        );

        addAlloysmelter(
                input.forOreDict("ingotCopper", 1),
                input.forOreDict("ingotZinc", 1),
                new ItemStack(IUItem.alloysingot, 1, 2), 3000
        );
        addAlloysmelter(
                input.forOreDict("ingotNickel", 1),
                input.forOreDict("ingotChromium", 1),
                new ItemStack(IUItem.alloysingot, 1, 4), 4000
        );
        addAlloysmelter(
                input.forOreDict("ingotTin", 1),
                input.forOreDict("ingotCopper", 3),
                Ic2Items.bronzeIngot, 1000
        );
        addAlloysmelter(
                input.forOreDict("ingotAluminium", 1),
                input.forOreDict("ingotMagnesium", 1),
                new ItemStack(IUItem.alloysingot, 1, 8), 2000
        );
        addAlloysmelter(
                input.forOreDict("ingotAluminum", 1),
                input.forOreDict("ingotMagnesium", 1),
                new ItemStack(IUItem.alloysingot, 1, 8), 2000
        );
        addAlloysmelter(
                input.forOreDict("ingotAluminium", 1),
                input.forOreDict("ingotTitanium", 1),
                new ItemStack(IUItem.alloysingot, 1, 1), 5000
        );
        addAlloysmelter(
                input.forOreDict("ingotAluminum", 1),
                input.forOreDict("ingotTitanium", 1),
                new ItemStack(IUItem.alloysingot, 1, 1), 5000
        );
        addAlloysmelter(
                input.forStack(new ItemStack(Items.IRON_INGOT), 1),
                input.forOreDict("ingotManganese", 1),
                new ItemStack(IUItem.alloysingot, 1, 9), 4500
        );


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
        return new GuiAlloySmelter(new ContainerDoubleElectricMachine(entityPlayer, this, this.type));
    }

    @Override
    public void operateOnce(MachineRecipe output, List<ItemStack> processResult) {
        this.inputSlotA.consume();
        this.outputSlot.add(processResult);
    }

    public String getStartSoundFile() {
        return "Machines/alloysmelter.ogg";
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
