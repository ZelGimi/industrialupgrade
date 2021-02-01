package com.denfop.ssp.items.reactors;


import com.denfop.ssp.common.Configs;
import com.denfop.ssp.items.SSPItems;
import com.denfop.ssp.items.resource.CraftingThings;
import ic2.api.reactor.IReactor;
import ic2.core.profile.NotClassic;
import net.minecraft.item.ItemStack;

@NotClassic
public class ItemReactorProton extends ItemReactorUranium {

    public ItemReactorProton(String name, int cells) {
        super(name, cells, Configs.proton_fuel_rod);

    }

    protected int getFinalHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
		/*if (reactor.isFluidCooled()) {
			float breedereffectiveness = reactor.getHeat() / reactor.getMaxHeat();
			if (breedereffectiveness > 0.5D)
				heat *= 1;
		}*/
        return heat;
    }

    protected ItemStack getDepletedStack(ItemStack stack, IReactor reactor) {
        ItemStack ret;
        switch (this.numberOfCells) {
            case 1:
                ret = SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_proton_fuel_rod);
                return ret.copy();
            case 2:
                ret = SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_dual_proton_fuel_rod);
                return ret.copy();
            case 4:
                ret = SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_quad_proton_fuel_rod);
                return ret.copy();
            case 8:
                ret = SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_eit_proton_fuel_rod);
                return ret.copy();
        }
        throw new RuntimeException("invalid cell count: " + this.numberOfCells);
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
            float breedereffectiveness = (reactor.getHeat() / reactor.getMaxHeat()) * 32.0F;
            float ReaktorOutput = 32.0F * breedereffectiveness + 3.0F;
            reactor.addOutput(ReaktorOutput);
        }
        return true;
    }

}
