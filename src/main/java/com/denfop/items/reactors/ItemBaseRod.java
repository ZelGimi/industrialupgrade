package com.denfop.items.reactors;

import com.denfop.IUCore;
import com.denfop.utils.ModUtils;
import ic2.api.reactor.IReactor;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;


public class ItemBaseRod extends ItemReactor {

    private final ItemStack[] depletedreactorrod;
    private final int heat;

    private final float power;

    public ItemBaseRod(String internalName, int cells, int time, int heat, float power, ItemStack[] depletedrod) {
        super(internalName, cells, time);
        this.heat = heat;
        this.power = power;
        this.depletedreactorrod = depletedrod;
        this.setCreativeTab(IUCore.ItemTab);
    }

    protected int getFinalHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
        if (reactor.isFluidCooled()) {
            float breedereffectiveness = (float) reactor.getHeat() / (float) reactor.getMaxHeat();
            if (breedereffectiveness > 0.5D) {
                heat *= this.heat;
            }
        }

        return heat;
    }

    protected ItemStack getDepletedStack(ItemStack stack, IReactor reactor) {
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
            final ItemStack stack,
            final World world,
            final List<String> tooltip,
            final ITooltipFlag advanced
    ) {
        super.addInformation(stack, world, tooltip, advanced);
        double[] p = new double[]{5.0D, 20D, 60D, 200D};

        double temp = Math.log10(this.numberOfCells);
        double temp1 = Math.log10(2);
        double m = temp / temp1;
        tooltip.add(Localization.translate("reactor.info") + ModUtils.getString(p[(int) m] * this.power) + " EU");
        tooltip.add(Localization.translate("reactor.info1") + ModUtils.getString((p[(int) m] * this.power) + p[(int) m] * (power / 2) * 0.99) + " EU");

    }


    public boolean acceptUraniumPulse(
            ItemStack stack,
            IReactor reactor,
            ItemStack pulsingStack,
            int youX,
            int youY,
            int pulseX,
            int pulseY,
            boolean heatrun
    ) {
        if (!heatrun) {
            float breedereffectiveness = (float) reactor.getHeat() / (float) reactor.getMaxHeat();
            float ReaktorOutput = (power / 2) * breedereffectiveness + this.power;
            reactor.addOutput(ReaktorOutput);
        }

        return true;
    }

}
