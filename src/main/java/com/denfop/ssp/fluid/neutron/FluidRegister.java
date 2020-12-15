package com.denfop.ssp.fluid.neutron;

import com.denfop.ssp.SuperSolarPanels;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidRegister {
	public static final FluidNeutron Neutron = (FluidNeutron) new FluidNeutron("neutron",
			SuperSolarPanels.getIdentifier("blocks/uu_matter1_still"),
			SuperSolarPanels.getIdentifier("blocks/uu_matter1_flow"))
			.setDensity(1100)
			.setGaseous(false)
			.setLuminosity(5)
			.setViscosity(900)
			.setTemperature(300)
			.setUnlocalizedName("neutron");

	public static void register() {
		registerFluid(Neutron);
	}

	public static void registerFluid(Fluid fluid) {
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
	}
}