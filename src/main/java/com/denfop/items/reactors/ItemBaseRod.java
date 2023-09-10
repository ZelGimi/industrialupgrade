package com.denfop.items.reactors;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUPotion;
import com.denfop.Localization;
import com.denfop.api.item.IHazmatLike;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;


public class ItemBaseRod extends ItemDamage implements IRadioactiveItemType, IReactorComponent {

    public final int numberOfCells;
    private final ItemStack[] depletedreactorrod;
    private final int heat;
    private final float power;
    private final String name;

    public ItemBaseRod(String internalName, int cells, int time, int heat, float power, ItemStack[] depletedrod) {
        super(internalName, time);
        this.heat = heat;
        this.power = power;
        this.depletedreactorrod = depletedrod;
        this.setCreativeTab(IUCore.ReactorsTab);
        this.setMaxStackSize(1);
        this.numberOfCells = cells;
        this.setNoRepair();
        this.setCreativeTab(IUCore.ReactorsTab);
        this.name = internalName;
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {

        final String loc = Constants.MOD_ID +
                ':' +
                "reactors" + "/" + name;
        return new ModelResourceLocation(loc, null);
    }

    public void onUpdate(ItemStack stack, World world, Entity rawEntity, int slotIndex, boolean isCurrentItem) {
        if (rawEntity instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) rawEntity;
            if (!IHazmatLike.hasCompleteHazmat(entity)) {
                IUPotion.radiation.applyTo(
                        entity,
                        this.getRadiationDuration(),
                        this.getRadiationAmplifier()
                );
            }
        }
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item", "iu").replace(".name", ""));
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    protected ItemStack getDepletedStack(ItemStack stack, IAdvReactor reactor) {
        ItemStack ret;
        double temp = Math.log10(this.numberOfCells);
        double temp1 = Math.log10(2);
        double p = temp / temp1;
        if (depletedreactorrod[(int) p] != null) {
            ret = depletedreactorrod[(int) p];
            return new ItemStack(ret.getItem(), 1);
        }
        throw new RuntimeException("invalid cell count: " + this.numberOfCells);
    }

    @Override
    public void addInformation(
            @Nonnull final ItemStack stack,
            final World world,
            @Nonnull final List<String> tooltip,
            @Nonnull final ITooltipFlag advanced
    ) {
        super.addInformation(stack, world, tooltip, advanced);
        tooltip.add(Localization.translate("iu.reactoritem.durability") + " " + (this.getMaxCustomDamage(stack) - this.getCustomDamage(
                stack)) + "/" + this.getMaxCustomDamage(stack));
        double[] p = new double[]{5.0D, 20D, 60D, 200D};

        double temp = Math.log10(this.numberOfCells);
        double temp1 = Math.log10(2);
        double m = temp / temp1;
        tooltip.add(Localization.translate("reactor.info") + ModUtils.getString(p[(int) m] * this.power) + " EF");
        tooltip.add(Localization.translate("reactor.info1") + ModUtils.getString((p[(int) m] * this.power) + p[(int) m] * (power / 2) * 0.99) + " EF");

    }


    public boolean acceptUraniumPulse(
            ItemStack stack,
            IAdvReactor reactor,
            ItemStack pulsingStack,
            int youX,
            int youY,
            int pulseX,
            int pulseY,
            boolean heatrun
    ) {
        if (!heatrun) {
            float breedereffectiveness = (float) reactor.getHeat() / (float) reactor.getMaxHeat();
            float ReaktorOutput = (this.power / 2) * breedereffectiveness + this.power;
            reactor.addOutput(ReaktorOutput);
        }

        return true;
    }

    @Override
    public int getRadiationDuration() {
        return 200;
    }

    @Override
    public int getRadiationAmplifier() {
        return 100;
    }

    @Override
    public boolean canBePlacedIn(final ItemStack var1, final IAdvReactor var2) {
        return true;
    }

}
