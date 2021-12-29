package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.container.ContainerBaseGenerationChipMachine;
import com.denfop.gui.GUIGenerationMicrochip;
import com.denfop.invslot.InvSlotProcessableGenerationMicrochip;
import com.denfop.items.resource.ItemIngots;
import com.denfop.tiles.base.TileEntityBaseGenerationMicrochip;
import com.denfop.tiles.base.TileEntityElectricMachine;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.upgrade.UpgradableProperty;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityGenerationMicrochip extends TileEntityBaseGenerationMicrochip {

    public TileEntityGenerationMicrochip() {
        super(1, 300, 1);
        this.inputSlotA = new InvSlotProcessableGenerationMicrochip(this, "inputA", 5
        );

    }

    public static void init() {
        add(new ItemStack(Items.FLINT), new ItemStack(Items.DYE, 1, 4), new ItemStack(Items.IRON_INGOT),
                new ItemStack(IUItem.iuingot, 1
                        , 11), new ItemStack(IUItem.iuingot, 1, 15), new ItemStack(IUItem.basecircuit), false
        );
        add(new ItemStack(IUItem.iuingot, 1, 1),
                new ItemStack(Items.REDSTONE, 1),
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(IUItem.iuingot, 1, 7),
                new ItemStack(IUItem.iuingot, 1, 14),
                new ItemStack(IUItem.basecircuit, 1, 1),
                true
        );
        add(new ItemStack(IUItem.iuingot, 1, 18),
                new ItemStack(Items.REDSTONE, 1),
                new ItemStack(Items.DIAMOND),
                new ItemStack(IUItem.iuingot, 1, 0),
                new ItemStack(IUItem.iuingot, 1, 5),
                new ItemStack(IUItem.basecircuit, 1, 2),
                true
        );
        add(new ItemStack(IUItem.iuingot, 1, 18),
                new ItemStack(Items.REDSTONE, 1),
                new ItemStack(Items.EMERALD),
                new ItemStack(IUItem.iuingot, 1, 0),
                new ItemStack(IUItem.iuingot, 1, 5),
                new ItemStack(IUItem.basecircuit, 1, 2),
                true
        );


        add(new ItemStack(IUItem.iuingot, 1, 2), new ItemStack(IUItem.iuingot, 1, 3), new ItemStack(IUItem.basecircuit, 1, 0),
                new ItemStack(IUItem.basecircuit, 1, 3), new ItemStack(IUItem.basecircuit, 1, 6),
                new ItemStack(IUItem.basecircuit, 1, 9), true
        );

        add(new ItemStack(IUItem.iuingot, 1, 8), new ItemStack(IUItem.iuingot, 1, 6), new ItemStack(IUItem.basecircuit, 1, 1),
                new ItemStack(IUItem.basecircuit, 1, 4), new ItemStack(IUItem.basecircuit, 1, 7),
                new ItemStack(IUItem.basecircuit, 1, 10), true
        );
        add(new ItemStack(IUItem.iuingot, 1, 2),
                new ItemStack(IUItem.iuingot, 1, 10),
                new ItemStack(IUItem.basecircuit, 1, 2),
                new ItemStack(IUItem.basecircuit, 1, 5),
                new ItemStack(IUItem.basecircuit, 1, 8),
                new ItemStack(IUItem.basecircuit, 1, 11),
                true
        );

    }

    public static void add(
            ItemStack first,
            ItemStack second,
            ItemStack three,
            ItemStack four,
            ItemStack five,
            ItemStack output,
            boolean check
    ) {
        IRecipeInput first1;
        IRecipeInput second1;
        IRecipeInput three1;
        IRecipeInput four1;
        IRecipeInput five1;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("temperature", (short) 4500);
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        if (check) {
            if (OreDictionary.getOreIDs(first).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(first)[0])
                    .isEmpty() && first.getItem() instanceof ItemIngots) {
                first1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(first)[0]));
            } else {
                first1 = input.forStack(first);
            }
            if (OreDictionary.getOreIDs(second).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(second)[0])
                    .isEmpty() && second.getItem() instanceof ItemIngots) {
                second1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]));
            } else {
                second1 = input.forStack(second);
            }
            if (OreDictionary.getOreIDs(three).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(three)[0])
                    .isEmpty() && three.getItem() instanceof ItemIngots) {
                three1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(three)[0]));
            } else {
                three1 = input.forStack(three);
            }
            if (OreDictionary.getOreIDs(four).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(four)[0])
                    .isEmpty() && four.getItem() instanceof ItemIngots) {
                four1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(four)[0]));
            } else {
                four1 = input.forStack(four);
            }
            if (OreDictionary.getOreIDs(five).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(five)[0])
                    .isEmpty() && five.getItem() instanceof ItemIngots) {
                five1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(five)[0]));
            } else {
                five1 = input.forStack(five);
            }
            Recipes.GenerationMicrochip.addRecipe(first1, second1, three1, four1, five1, output, nbt);
        } else {
            Recipes.GenerationMicrochip.addRecipe(
                    input.forStack(first),
                    input.forStack(second),
                    input.forStack(three),
                    input.forStack(four),
                    input.forStack(five),
                    output,
                    nbt
            );

        }
    }

    public String getInventoryName() {

        return "Generation Microchip";
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIGenerationMicrochip(new ContainerBaseGenerationChipMachine(entityPlayer, this));
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

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing
        );
    }

    @Override
    public short getTemperature() {
        return this.temperature;
    }

    @Override
    public void setTemperature(short temperature) {
        this.temperature = temperature;
    }

    @Override
    public short getMaxTemperature() {
        return this.maxtemperature;
    }

    @Override
    public boolean isFluidTemperature() {
        return false;
    }

    @Override
    public FluidStack getFluid() {
        return null;
    }

    @Override
    public TileEntityElectricMachine getTile() {
        return this;
    }

    @Override
    public boolean reveiver() {
        return true;
    }

}
