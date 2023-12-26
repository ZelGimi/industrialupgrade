package com.denfop.items.reactors;

import com.denfop.Constants;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemsFan  extends ItemDamage{

    private final int power;
    private final int energy;
    private final int level;

    public ItemsFan(final String name, final int maxDamage,int level, int power, int energy) {
        super(name, maxDamage);
        this.power = power;
        this.level=level;
        this.energy = energy;
        setMaxStackSize(1);
    }

    public int getLevel() {
        return level;
    }

    public int getEnergy() {
        return energy;
    }

    public int getPower() {
        return power;
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }
    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item", "iu").replace(".name", ""));
    }
    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {

        final String loc = Constants.MOD_ID +
                ':' +
                "fan" + "/" + name;
        return new ModelResourceLocation(loc, null);
    }
}
