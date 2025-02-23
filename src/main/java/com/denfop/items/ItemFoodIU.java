package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFoodIU extends ItemFood implements IModelRegister {
    private final String name;
    private final String path;

    public ItemFoodIU(String name,int amount, float saturation) {
        this(name, "",amount,saturation);
    }

    public ItemFoodIU(String name, String path,int amount, float saturation) {
        super(amount,saturation,false);
        this.setCreativeTab(IUCore.ItemTab);
        this.setMaxStackSize(64);

        this.name = name;
        this.path = path;
        setUnlocalizedName(name);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }
    public String getUnlocalizedName() {
        return super.getUnlocalizedName() + ".name";
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu.") + ".name");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(
                this,
                0,
                new ModelResourceLocation(Constants.MOD_ID + ":" + path + this.name, null)
        );
    }
}
