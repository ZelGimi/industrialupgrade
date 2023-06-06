package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import ic2.api.item.IBoxable;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

public abstract class ItemToolIU extends ItemTool implements IModelRegister, IBoxable {

    protected final String name;
    protected final Set<? extends IToolClass> toolClasses;
    protected EnumRarity rarity;

    protected ItemToolIU(String name, HarvestLevel harvestLevel, Set<? extends IToolClass> toolClasses) {
        this(name, harvestLevel, toolClasses, new HashSet<>());
    }

    protected ItemToolIU(
            String name,
            HarvestLevel harvestLevel,
            Set<? extends IToolClass> toolClasses,
            Set<Block> mineableBlocks
    ) {
        this(name, 0.0F, 0.0F, harvestLevel, toolClasses, mineableBlocks);
    }

    protected ItemToolIU(
            String name,
            float damage,
            float speed,
            HarvestLevel harvestLevel,
            Set<? extends IToolClass> toolClasses,
            Set<Block> mineableBlocks
    ) {
        super(damage, speed, harvestLevel.toolMaterial, mineableBlocks);
        this.rarity = EnumRarity.COMMON;
        this.toolClasses = toolClasses;
        this.setMaxStackSize(1);
        this.setCreativeTab(IUCore.EnergyTab);
        this.name = name;
        for (final IToolClass toolClass : toolClasses) {
            if (toolClass.getName() != null) {
                this.setHarvestLevel(toolClass.getName(), harvestLevel.level);
            }
        }

        if (toolClasses.contains(ToolClass.Pickaxe) && harvestLevel.toolMaterial == ToolMaterial.DIAMOND) {
            mineableBlocks.add(Blocks.OBSIDIAN);
            mineableBlocks.add(Blocks.REDSTONE_ORE);
            mineableBlocks.add(Blocks.LIT_REDSTONE_ORE);
        }

        if (name != null) {
            this.setUnlocalizedName(name);
            BlocksItems.registerItem(this, IUCore.getIdentifier(name));
        }
        IUCore.proxy.addIModelRegister(this);

    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {

        final String loc = Constants.MOD_ID +
                ':' +
                "energy" + "/" + name;
        return new ModelResourceLocation(loc, null);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(String name) {
        this.registerModel(0, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name) {
        registerModel(this, meta, name);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name, String extraName) {
        registerModel(this, meta, name);
    }

    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(5);
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        return this.getUnlocalizedName();
    }

    public String getUnlocalizedNameInefficiently(ItemStack itemStack) {
        return this.getUnlocalizedName(itemStack);
    }

    public String getItemStackDisplayName(ItemStack itemStack) {
        return Localization.translate(this.getUnlocalizedName(itemStack));
    }

    protected boolean isInCreativeTab(CreativeTabs tab) {
        return super.isInCreativeTab(tab);
    }


    public boolean canHarvestBlock(IBlockState state, ItemStack itemStack) {
        Material material = state.getMaterial();


        for (IToolClass toolClass : this.toolClasses) {
            if (toolClass.getBlacklist().contains(state.getBlock()) || toolClass.getBlacklist().contains(material)) {
                return false;
            }

            if (toolClass.getWhitelist().contains(state.getBlock()) || toolClass.getWhitelist().contains(material)) {
                return true;
            }
        }

        return super.canHarvestBlock(state, itemStack);
    }

    public float getDestroySpeed(ItemStack itemStack, IBlockState state) {
        return this.canHarvestBlock(state, itemStack) ? this.efficiency : super.getDestroySpeed(itemStack, state);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return stack.isItemEnchanted() && this.rarity != EnumRarity.EPIC ? EnumRarity.RARE : this.rarity;
    }


    public boolean canBeStoredInToolbox(ItemStack itemStack) {
        return true;
    }


}
