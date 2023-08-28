package com.powerutils;

import com.denfop.IUCore;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;

public class PUItemBase extends Item implements IModelRender {

    private final String name;
    private final String path;

    public PUItemBase(String name) {
        this(name, "");
    }

    public PUItemBase(String name, String path) {
        super();
        this.setCreativeTab(IUCore.ItemTab);
        this.setMaxStackSize(64);

        this.name = name;
        this.path = path;
        setUnlocalizedName(name);
        Register.registerItem((Item) this, PowerUtils.getIdentifier(name)).setUnlocalizedName(name);
        PowerUtils.addIModelRegister(this);
    }

    public String getUnlocalizedName() {
        return "pu." + super.getUnlocalizedName().substring(3) + ".name";
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "pu.") + ".name");
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
