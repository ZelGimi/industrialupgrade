package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemToolCrafting extends Item implements IModelRegister {

    private final String name;

    public ItemToolCrafting(String name, int maximumUses) {
        super();
        this.setMaxDamage(maximumUses - 1);
        this.setMaxStackSize(1);
        this.canRepair = false;
        this.name = name;
        this.setCreativeTab(IUCore.EnergyTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    protected static int getRemainingUses(ItemStack stack) {
        return stack.getMaxDamage() - stack.getItemDamage() + 1;
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu."));
    }

    public String getItemStackDisplayName() {
        return I18n.translateToLocal(this.getUnlocalizedName().replace("item.", "iu."));
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, @NotNull ITooltipFlag advanced) {
        tooltip.add(Localization.translate("item.itemTool.tooltip.UsesLeft", getRemainingUses(stack)));
    }

    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    public @NotNull ItemStack getContainerItem(ItemStack stack) {
        ItemStack ret = stack.copy();
        return ret.attemptDamageItem(1, IUCore.random, null) ? ModUtils.emptyStack : ret;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(
                this,
                0,
                new ModelResourceLocation(Constants.MOD_ID + ":" + name, null)
        );
    }

}
