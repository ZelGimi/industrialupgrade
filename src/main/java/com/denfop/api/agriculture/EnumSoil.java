package com.denfop.api.agriculture;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.recipe.IInputItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.denfop.blocks.BlockClassicOre.BOOL_PROPERTY;

public enum EnumSoil {
    FARMLAND(Blocks.FARMLAND),
    MYCELIUM(Blocks.MYCELIUM),
    SAND(Blocks.SAND),
    SOULSAND(Blocks.SOUL_SAND),
    ENDER(Blocks.END_STONE),
    GRAVEL(Blocks.GRAVEL),
    QUARTZ(IUItem.preciousore.getBlock(3), IUItem.preciousore.getBlock(3).defaultBlockState(), false),
    DIAMOND(Blocks.DIAMOND_ORE),
    EMERALD(Blocks.EMERALD_ORE),
    COAL(Blocks.COAL_ORE),
    IRON(Blocks.IRON_ORE),
    GOLD(Blocks.GOLD_ORE),
    LAPIS(Blocks.LAPIS_ORE),
    REDSTONE(Blocks.REDSTONE_ORE),
    MICHALOV(IUItem.ore.getBlock(0), IUItem.ore.getStateFromMeta(0), false),
    ALUMINUM(IUItem.ore.getBlock(1), IUItem.ore.getStateFromMeta(1), false),
    VANADIUM(IUItem.ore.getBlock(2), IUItem.ore.getStateFromMeta(2), false),
    TUNGSTEN(IUItem.ore.getBlock(3), IUItem.ore.getStateFromMeta(3), false),
    COBALT(IUItem.ore.getBlock(4), IUItem.ore.getStateFromMeta(4), false),
    MAGNESIUM(IUItem.ore.getBlock(5), IUItem.ore.getStateFromMeta(5), false),
    NICKEL(IUItem.ore.getBlock(6), IUItem.ore.getStateFromMeta(6), false),
    PLATINUM(IUItem.ore.getBlock(7), IUItem.ore.getStateFromMeta(7), false),
    TITANIUM(IUItem.ore.getBlock(8), IUItem.ore.getStateFromMeta(8), false),
    CHROMIUM(IUItem.ore.getBlock(9), IUItem.ore.getStateFromMeta(9), false),
    SPINEL(IUItem.ore.getBlock(10), IUItem.ore.getStateFromMeta(10), false),
    SILVER(IUItem.ore.getBlock(11), IUItem.ore.getStateFromMeta(11), false),
    ZINC(IUItem.ore.getBlock(12), IUItem.ore.getStateFromMeta(12), false),
    MANGANESE(IUItem.ore.getBlock(13), IUItem.ore.getStateFromMeta(13), false),
    IRIDIUM(IUItem.ore.getBlock(14), IUItem.ore.getStateFromMeta(14), false),
    GERMANIUM(IUItem.ore.getBlock(15), IUItem.ore.getStateFromMeta(15), false),
    COPPER(Blocks.COPPER_ORE, Blocks.COPPER_ORE.defaultBlockState(), false),
    TIN(IUItem.classic_ore.getBlock(1), IUItem.classic_ore.getStateFromMeta(1), false),
    LEAD(IUItem.classic_ore.getBlock(2), IUItem.classic_ore.getStateFromMeta(2), false),
    URANIUM(IUItem.classic_ore.getBlock(3), IUItem.classic_ore.getStateFromMeta(3).setValue(BOOL_PROPERTY, false), false),
    ARSENIC(IUItem.ore3.getBlock(0), IUItem.ore3.getStateFromMeta(0), false),
    BARIUM(IUItem.ore3.getBlock(1), IUItem.ore3.getStateFromMeta(1), false),
    BISMUTH(IUItem.ore3.getBlock(2), IUItem.ore3.getStateFromMeta(2), false),
    GADOLINIUM(IUItem.ore3.getBlock(3), IUItem.ore3.getStateFromMeta(3), false),
    GALLIUM(IUItem.ore3.getBlock(4), IUItem.ore3.getStateFromMeta(4), false),
    HAFNIUM(IUItem.ore3.getBlock(5), IUItem.ore3.getStateFromMeta(5), false),
    YTTRIUM(IUItem.ore3.getBlock(6), IUItem.ore3.getStateFromMeta(6), false),
    MOLYBDENUM(IUItem.ore3.getBlock(7), IUItem.ore3.getStateFromMeta(7), false),
    NEODYMIUM(IUItem.ore3.getBlock(8), IUItem.ore3.getStateFromMeta(8), false),
    NIOBIUM(IUItem.ore3.getBlock(9), IUItem.ore3.getStateFromMeta(9), false),
    PALLADIUM(IUItem.ore3.getBlock(10), IUItem.ore3.getStateFromMeta(10), false),
    POLONIUM(IUItem.ore3.getBlock(11), IUItem.ore3.getStateFromMeta(11), false),
    STRONTIUM(IUItem.ore3.getBlock(12), IUItem.ore3.getStateFromMeta(12), false),
    THALLIUM(IUItem.ore3.getBlock(13), IUItem.ore3.getStateFromMeta(13), false),
    ZIRCONIUM(IUItem.ore3.getBlock(14), IUItem.ore3.getStateFromMeta(14), false),
    OSMIUM(IUItem.ore2.getBlock(3), IUItem.ore2.getStateFromMeta(3), false),
    TANTALUM(IUItem.ore2.getBlock(4), IUItem.ore2.getStateFromMeta(4), false),
    CADMIUM(IUItem.ore2.getBlock(5), IUItem.ore2.getStateFromMeta(5), false),
    ;

    private final Block block;
    private final BlockState state;
    private final boolean ignore;

    EnumSoil(final Block block) {
        this.block = block;
        this.state = block.defaultBlockState();
        this.ignore = true;
    }

    EnumSoil(final Block block, final BlockState state, boolean ignore) {
        this.block = block;
        this.state = state;
        this.ignore = ignore;
    }

    public Block getBlock() {
        return this.block;
    }

    static List<IInputItemStack> lst;

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
    public static boolean contain(ItemStack stack){
        for (IInputItemStack iInputItemStack : getBlocks()){
            if (iInputItemStack.matches(stack))
                return true;
        }
        return false;
    }
    public static EnumSoil get(ItemStack stack){
        for (int i = 0; i < EnumSoil.values().length;i++){
            if ( getBlocks().get(i).matches(stack))
                return EnumSoil.values()[i];
        }
        return null;
    }
    public ItemStack getStack() {
        return new ItemStack(block);
    }

    public boolean isIgnore() {
        return ignore;
    }

    public BlockState getState() {
        return state;
    }
}
