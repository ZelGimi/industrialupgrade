package com.denfop.api.agriculture;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.recipe.IInputItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public enum EnumSoil {
    FARMLAND(Blocks.FARMLAND),
    MYCELIUM(Blocks.MYCELIUM),
    SAND(Blocks.SAND),
    SOULSAND(Blocks.SOUL_SAND),
    ENDER(Blocks.END_STONE),
    GRAVEL(Blocks.GRAVEL),
    QUARTZ(IUItem.preciousore, IUItem.preciousore.getStateFromMeta(3), false),
    DIAMOND(Blocks.DIAMOND_ORE),
    EMERALD(Blocks.EMERALD_ORE),
    COAL(Blocks.COAL_ORE),
    IRON(Blocks.IRON_ORE),
    GOLD(Blocks.GOLD_ORE),
    LAPIS(Blocks.LAPIS_ORE),
    REDSTONE(Blocks.REDSTONE_ORE),
    MICHALOV(IUItem.ore, IUItem.ore.getStateFromMeta(0), false),
    ALUMINUM(IUItem.ore, IUItem.ore.getStateFromMeta(1), false),
    VANADIUM(IUItem.ore, IUItem.ore.getStateFromMeta(2), false),
    TUNGSTEN(IUItem.ore, IUItem.ore.getStateFromMeta(3), false),
    COBALT(IUItem.ore, IUItem.ore.getStateFromMeta(4), false),
    MAGNESIUM(IUItem.ore, IUItem.ore.getStateFromMeta(5), false),
    NICKEL(IUItem.ore, IUItem.ore.getStateFromMeta(6), false),
    PLATINUM(IUItem.ore, IUItem.ore.getStateFromMeta(7), false),
    TITANIUM(IUItem.ore, IUItem.ore.getStateFromMeta(8), false),
    CHROMIUM(IUItem.ore, IUItem.ore.getStateFromMeta(9), false),
    SPINEL(IUItem.ore, IUItem.ore.getStateFromMeta(10), false),
    SILVER(IUItem.ore, IUItem.ore.getStateFromMeta(11), false),
    ZINC(IUItem.ore, IUItem.ore.getStateFromMeta(12), false),
    MANGANESE(IUItem.ore, IUItem.ore.getStateFromMeta(13), false),
    IRIDIUM(IUItem.ore, IUItem.ore.getStateFromMeta(14), false),
    GERMANIUM(IUItem.ore, IUItem.ore.getStateFromMeta(15), false),
    COPPER(IUItem.classic_ore, IUItem.classic_ore.getStateFromMeta(0), false),
    TIN(IUItem.classic_ore, IUItem.classic_ore.getStateFromMeta(1), false),
    LEAD(IUItem.classic_ore, IUItem.classic_ore.getStateFromMeta(2), false),
    URANIUM(IUItem.classic_ore, IUItem.classic_ore.getStateFromMeta(3), false),
    ARSENIC(IUItem.ore3, IUItem.ore3.getStateFromMeta(0), false),
    BARIUM(IUItem.ore3, IUItem.ore3.getStateFromMeta(1), false),
    BISMUTH(IUItem.ore3, IUItem.ore3.getStateFromMeta(2), false),
    GADOLINIUM(IUItem.ore3, IUItem.ore3.getStateFromMeta(3), false),
    GALLIUM(IUItem.ore3, IUItem.ore3.getStateFromMeta(4), false),
    HAFNIUM(IUItem.ore3, IUItem.ore3.getStateFromMeta(5), false),
    YTTRIUM(IUItem.ore3, IUItem.ore3.getStateFromMeta(6), false),
    MOLYBDENUM(IUItem.ore3, IUItem.ore3.getStateFromMeta(7), false),
    NEODYMIUM(IUItem.ore3, IUItem.ore3.getStateFromMeta(8), false),
    NIOBIUM(IUItem.ore3, IUItem.ore3.getStateFromMeta(9), false),
    PALLADIUM(IUItem.ore3, IUItem.ore3.getStateFromMeta(10), false),
    POLONIUM(IUItem.ore3, IUItem.ore3.getStateFromMeta(11), false),
    STRONTIUM(IUItem.ore3, IUItem.ore3.getStateFromMeta(12), false),
    THALLIUM(IUItem.ore3, IUItem.ore3.getStateFromMeta(13), false),
    ZIRCONIUM(IUItem.ore3, IUItem.ore3.getStateFromMeta(14), false),
    OSMIUM(IUItem.ore2, IUItem.ore2.getStateFromMeta(3), false),
    TANTALUM(IUItem.ore2, IUItem.ore2.getStateFromMeta(4), false),
    CADMIUM(IUItem.ore2, IUItem.ore2.getStateFromMeta(5), false),
    ;

    static List<IInputItemStack> lst;
    private final Block block;
    private final IBlockState state;
    private final boolean ignore;

    EnumSoil(final Block block) {
        this.block = block;
        this.state = block.getDefaultState();
        this.ignore = true;
    }

    EnumSoil(final Block block, final IBlockState state, boolean ignore) {
        this.block = block;
        this.state = state;
        this.ignore = ignore;
    }

    public static List<IInputItemStack> getBlocks() {
        if (lst == null) {
            lst = new LinkedList<>();
            for (EnumSoil enumSoil : values()) {
                lst.add(Recipes.inputFactory.getInput(enumSoil.getStack()));
            }

            lst = new ArrayList<>(lst);
        }
        return lst;
    }

    public static boolean contain(ItemStack stack) {
        for (IInputItemStack iInputItemStack : getBlocks()) {
            if (iInputItemStack.matches(stack)) {
                return true;
            }
        }
        return false;
    }

    public static EnumSoil get(ItemStack stack) {
        for (int i = 0; i < EnumSoil.values().length; i++) {
            if (getBlocks().get(i).matches(stack)) {
                return EnumSoil.values()[i];
            }
        }
        return null;
    }

    public Block getBlock() {
        return this.block;
    }

    public ItemStack getStack() {
        int meta = block.getMetaFromState(state);
        return new ItemStack(block, 1, meta);
    }

    public boolean isIgnore() {
        return ignore;
    }

    public IBlockState getState() {
        return state;
    }
}
