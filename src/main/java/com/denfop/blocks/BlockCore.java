//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public abstract class BlockCore extends Block {

    protected String modName;
    protected String name;

    public BlockCore(Material material, String modName) {
        super(material);
        this.modName = modName;
        this.preInit();
    }

    public BlockCore(Material material, MapColor blockMapColor, String modName) {
        super(material, blockMapColor);
        this.modName = modName;
    }

    protected abstract boolean preInit();

    public Block setUnlocalizedName(String name) {
        this.name = name;
        name = this.modName + "." + name;
        return super.setUnlocalizedName(name);
    }

    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName();
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.COMMON;
    }

}
