package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.recipe.ReplicatorRecipe;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemCrystalMemory extends Item implements IModelRegister {

    public ItemCrystalMemory() {
        super();
        this.setMaxStackSize(1);
        String name = "crystal_memory";
        this.setCreativeTab(IUCore.RecourseTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    public boolean isRepairable() {
        return false;
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu.").replace(".name", ""));
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        ItemStack recorded = this.readItemStack(stack);
        if (!ModUtils.isEmpty(recorded)) {
            tooltip.add(Localization.translate("iu.item.CrystalMemory.tooltip.iu.item") + " " + recorded.getDisplayName());
            tooltip.add(Localization.translate("iu.item.CrystalMemory.tooltip.UU-Matter") + " " + ModUtils.getString(
                    ReplicatorRecipe.getInBuckets(
                            recorded)) + "B");
        } else {
            tooltip.add(Localization.translate("iu.item.CrystalMemory.tooltip.Empty"));
        }

    }

    public ItemStack readItemStack(ItemStack stack) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        NBTTagCompound contentTag = nbt.getCompoundTag("Pattern");
        return new ItemStack(contentTag);
    }

    public void writecontentsTag(ItemStack stack, ItemStack recorded) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        NBTTagCompound contentTag = new NBTTagCompound();
        recorded.writeToNBT(contentTag);
        nbt.setTag("Pattern", contentTag);
    }

    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(
                this,
                0,
                new ModelResourceLocation(Constants.MOD_ID + ":" + "crystal_memory", null)
        );
    }

}
