package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.reactors.EnumTypeComponent;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.api.reactors.IReactorItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemReactorPlate extends ItemDamage implements IReactorItem {

    private final int level;
    private final double percent;

    public ItemReactorPlate(final String name, int level, double percent) {
        super(name, 0);
        this.level = level;
        this.percent = percent;
        setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {

        final String loc = Constants.MOD_ID +
                ':' +
                "reactors" + "/" + name;
        return new ModelResourceLocation(loc, null);
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item", "iu").replace(".name", ""));
    }

    @Override
    public boolean needClear(ItemStack stack) {
        return this.getMaxCustomDamage(stack) - this.getCustomDamage(
                stack) == 0;
    }

    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    @Override
    public EnumTypeComponent getType() {
        return EnumTypeComponent.PLATE;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getAutoRepair(final IAdvReactor reactor) {
        return 0;
    }

    @Override
    public int getRepairOther(final IAdvReactor reactor) {
        return 0;
    }

    @Override
    public void addInformation(
            @Nonnull final ItemStack stack,
            final World world,
            @Nonnull final List<String> tooltip,
            @Nonnull final ITooltipFlag advanced
    ) {
        super.addInformation(stack, world, tooltip, advanced);

        tooltip.add(Localization.translate("reactor.component_level") + this.level);
        tooltip.add(Localization.translate("reactor.component_level1"));

    }

    @Override
    public int getDamageCFromHeat(final int heat, final IAdvReactor reactor) {
        return 1;
    }

    @Override
    public int getHeat(final IAdvReactor reactor) {
        return 0;
    }

    @Override
    public double getHeatRemovePercent(final IAdvReactor reactor) {
        return percent;
    }

    @Override
    public void damageItem(final ItemStack stack, final int damage) {

    }

    @Override
    public boolean updatableItem() {
        return false;
    }

    @Override
    public boolean caneExtractHeat() {
        return false;
    }

    @Override
    public double getEnergyProduction(final IAdvReactor reactor) {
        return 0;
    }

}
