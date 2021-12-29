package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GUIPainting;
import com.denfop.utils.ModUtils;
import ic2.api.item.IElectricItem;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.RecipeOutput;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.init.Localization;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TileEntityPainting extends TileEntityDoubleElectricMachine {

    public TileEntityPainting() {
        super(1, 300, 1, Localization.translate("iu.painting.name"), EnumDoubleElectricMachine.PAINTING);
    }

    public static void init() {
        addpainting(new ItemStack(IUItem.nanodrill, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.quantumdrill, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.spectraldrill, 1, OreDictionary.WILDCARD_VALUE));

        addpainting(new ItemStack(IUItem.quantumHelmet, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.quantumLeggings, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.quantumBodyarmor, 1, OreDictionary.WILDCARD_VALUE));
        addpainting(new ItemStack(IUItem.quantumBoots, 1, OreDictionary.WILDCARD_VALUE));

    }


    public static void addpainting(ItemStack container) {
        NBTTagCompound nbt = ModUtils.nbt();
        String name = "Zelen";
        nbt.setString("mode", name);
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.painting.addRecipe(input.forStack(container), input.forStack(new ItemStack(IUItem.paints, 4, 3)), nbt,
                container
        );
        NBTTagCompound nbt1 = ModUtils.nbt();
        name = "Demon";
        nbt1.setString("mode", name);
        Recipes.painting.addRecipe(
                input.forStack(container),
                input.forStack(new ItemStack(IUItem.paints, 4, 4)),
                nbt1,
                container
        );
        NBTTagCompound nbt2 = ModUtils.nbt();
        name = "Dark";
        nbt2.setString("mode", name);
        Recipes.painting.addRecipe(
                input.forStack(container),
                input.forStack(new ItemStack(IUItem.paints, 4, 6)),
                nbt2,
                container
        );
        NBTTagCompound nbt3 = ModUtils.nbt();
        name = "Cold";
        nbt3.setString("mode", name);
        Recipes.painting.addRecipe(
                input.forStack(container),
                input.forStack(new ItemStack(IUItem.paints, 4, 1)),
                nbt3,
                container
        );
        NBTTagCompound nbt4 = ModUtils.nbt();
        name = "Ender";
        nbt4.setString("mode", name);
        Recipes.painting.addRecipe(
                input.forStack(container),
                input.forStack(new ItemStack(IUItem.paints, 4, 7)),
                nbt4,
                container
        );
        NBTTagCompound nbt5 = ModUtils.nbt();
        name = "";
        nbt5.setString("mode", name);
        Recipes.painting.addRecipe(
                input.forStack(container),
                input.forStack(new ItemStack(IUItem.paints, 4, 0)),
                nbt5,
                container
        );

    }

    public void operateOnce(RecipeOutput output, List<ItemStack> processResult) {
        ItemStack stack1 = this.inputSlotA.get(1).getItem() instanceof IElectricItem
                ? this.inputSlotA.get(1)
                : this.inputSlotA.get(0);
        NBTTagCompound tNBT = StackUtil.getOrCreateNbtData(stack1);
        int damage = stack1.getItemDamage();
        final Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.getEnchantments(stack1);
        this.inputSlotA.consume(0);
        this.outputSlot.add(processResult);
        ItemStack stack = this.outputSlot.get();
        stack.setTagCompound(tNBT);
        NBTTagCompound nbt = ModUtils.nbt(stack);
        String mode = output.metadata.getString("mode");
        nbt.setString("mode", mode);
        EnchantmentHelper.setEnchantments(enchantmentMap, stack);
        stack.setItemDamage(damage);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIPainting(new ContainerDoubleElectricMachine(entityPlayer, this, type));
    }

    public String getStartSoundFile() {
        return "Machines/painting.ogg";
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

}
