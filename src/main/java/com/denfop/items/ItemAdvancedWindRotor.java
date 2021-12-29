package com.denfop.items;

import com.denfop.Constants;
import com.denfop.items.reactors.ItemGradualInt;
import ic2.api.item.IKineticRotor;
import ic2.core.block.kineticgenerator.gui.GuiWaterKineticGenerator;
import ic2.core.block.kineticgenerator.gui.GuiWindKineticGenerator;
import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.profile.NotClassic;
import ic2.core.util.ConfigUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@NotClassic
public class ItemAdvancedWindRotor extends ItemGradualInt implements IKineticRotor {

    private final int maxWindStrength;
    private final int minWindStrength;
    private final int radius;
    private final float efficiency;
    private final ResourceLocation renderTexture;
    private final boolean water;

    public ItemAdvancedWindRotor(
            String name, int Radius, int durability, float efficiency, int minWindStrength, int maxWindStrength,
            ResourceLocation RenderTexture
    ) {
        super(name, durability);
        this.setMaxStackSize(1);
        this.radius = Radius;
        this.efficiency = efficiency;
        this.renderTexture = RenderTexture;
        this.minWindStrength = minWindStrength;
        this.maxWindStrength = maxWindStrength;
        this.water = false;
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

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {
        StringBuilder loc = new StringBuilder();
        loc.append(Constants.MOD_ID);
        loc.append(':');

        loc.append("rotor").append("/").append(name);
        return new ModelResourceLocation(loc.toString(), null);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(Localization.translate("ic2.itemrotor.wind.info", this.minWindStrength, this.maxWindStrength));
        GearboxType type = null;
        if (Minecraft.getMinecraft().currentScreen instanceof GuiWaterKineticGenerator) {
            type = GearboxType.WATER;
        } else if (Minecraft.getMinecraft().currentScreen instanceof GuiWindKineticGenerator) {
            type = GearboxType.WIND;
        }

        if (type != null) {
            tooltip.add(Localization.translate("ic2.itemrotor.fitsin." + this.isAcceptedType(stack, type)));
        }

        int windStrength = 40;
        int windStrength1 = 60;
        double KU = windStrength * this.getEfficiency(stack) * 10.0F * ConfigUtil.getFloat(
                MainConfig.get(),
                "balance/energy/kineticgenerator/wind"
        );
        int eu = (int) (KU * 0.25D * (double) ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/Kinetic"));
        double KU1 = windStrength1 * this.getEfficiency(stack) * 10.0F * ConfigUtil.getFloat(
                MainConfig.get(),
                "balance/energy/kineticgenerator/wind"
        );
        int eu1 = (int) (KU1 * 0.25D * (double) ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/Kinetic"));

        tooltip.add(Localization.translate("iu.windgenerator") + windStrength + " " + Localization.translate("iu.windgenerator1") + eu);
        tooltip.add(Localization.translate("iu.windgenerator") + windStrength1 + " " + Localization.translate("iu.windgenerator1") + eu1);


    }

    public int getDiameter(ItemStack stack) {
        return this.radius;
    }

    public ResourceLocation getRotorRenderTexture(ItemStack stack) {
        return this.renderTexture;
    }

    public float getEfficiency(ItemStack stack) {
        return this.efficiency;
    }

    public int getMinWindStrength(ItemStack stack) {
        return this.minWindStrength;
    }

    public int getMaxWindStrength(ItemStack stack) {
        return this.maxWindStrength;
    }

    public boolean isAcceptedType(ItemStack stack, GearboxType type) {
        return type == GearboxType.WIND || this.water;
    }

}
