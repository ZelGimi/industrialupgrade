package com.denfop.integration.de;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;

public class IUDEItem extends Item implements IModelRegister {

    private final String name;
    private final String path;

    public IUDEItem(String name) {
        this(name, "");
    }

    public IUDEItem(String name, String path) {
        super();
        this.setCreativeTab(IUCore.ItemTab);
        this.setMaxStackSize(64);

        this.name = name;
        this.path = path;
        setUnlocalizedName(name);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu.") + ".name");
    }

    public String getUnlocalizedName() {
        return super.getUnlocalizedName() + ".name";
    }

    @Override
    public boolean hasEffect(@Nonnull final ItemStack stack) {
        return true;
    }

    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(
                this,
                0,
                new ModelResourceLocation(Constants.MOD_ID + ":" + path + this.name, null)
        );
    }


}
