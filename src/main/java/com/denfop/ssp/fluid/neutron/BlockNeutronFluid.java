package com.denfop.ssp.fluid.neutron;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockNeutronFluid extends BlockFluidClassic {

    public BlockNeutronFluid(Fluid fluid) {
        super(fluid, Material.WATER);
        setRegistryName("neutronfluid");
    }

}
