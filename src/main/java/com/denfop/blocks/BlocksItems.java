package com.denfop.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BlocksItems {


    public BlocksItems() {
    }

    private static void registerIC2fluid(
            FluidName name, int density,
            int temperature, boolean isGaseous
    ) {
        Material steam = new MaterialLiquid(MapColor.SILVER);
        Fluid fluid =
                (new IUFluid(name))
                        .setDensity(density)
                        .setTemperature(temperature)
                        .setGaseous(isGaseous);
        if (!FluidRegistry.registerFluid(fluid)) {
            fluid = FluidRegistry.getFluid(name.getName());
        }

        if (!fluid.canBePlacedInWorld()) {
            Block block = new BlockIUFluid(name, fluid, steam);
            fluid.setBlock(block);
            fluid.setUnlocalizedName(block.getUnlocalizedName().substring(4));
        }

        name.setInstance(fluid);
        FluidRegistry.addBucketForFluid(fluid);
    }

    public void init() {

        initFluids();
    }

    private void initFluids() {

        registerIC2fluid(FluidName.fluidNeutron, 3000, 300, false);
        registerIC2fluid(FluidName.fluidHelium, 1000, 300, true);
        registerIC2fluid(FluidName.fluidbenz, 3000, 500, false);
        registerIC2fluid(FluidName.fluiddizel, 3000, 500, false);
        registerIC2fluid(FluidName.fluidneft, 3000, 500, false);
//
        registerIC2fluid(FluidName.fluidpolyeth, 3000, 2000, true);
        registerIC2fluid(FluidName.fluidpolyprop, 3000, 2000, true);
        registerIC2fluid(FluidName.fluidoxy, 3000, 500, true);

        registerIC2fluid(FluidName.fluidhyd, 3000, 500, true);
        registerIC2fluid(FluidName.fluidazot, 3000, 500, true);
        registerIC2fluid(FluidName.fluidco2, 3000, 500, true);


    }

}
