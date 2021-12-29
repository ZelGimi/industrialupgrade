package com.denfop.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BlocksItems {

    public BlocksItems() {
    }

    public static void init() {

        initFluids();

    }

    private static void initFluids() {
        registerIC2fluid(FluidName.fluidNeutron, 3867955, 3000, 300, false);
        registerIC2fluid(FluidName.fluidHelium, 10983500, 1000, 300, true);
        registerIC2fluid(FluidName.fluidbenz, 3867955, 3000, 500, false);
        registerIC2fluid(FluidName.fluiddizel, 3867955, 3000, 500, false);
        registerIC2fluid(FluidName.fluidneft, 3867955, 3000, 500, false);
//
        registerIC2fluid(FluidName.fluidpolyeth, 3867955, 3000, 2000, true);
        registerIC2fluid(FluidName.fluidpolyprop, 3867955, 3000, 2000, true);
        registerIC2fluid(FluidName.fluidoxy, 3867955, 3000, 500, true);

        registerIC2fluid(FluidName.fluidhyd, 3867955, 3000, 500, true);

    }

    private static void registerIC2fluid(
            FluidName name, int color, int density,
            int temperature, boolean isGaseous
    ) {

        Fluid fluid =
                (new IUFluid(name))
                        .setDensity(density)
                        .setTemperature(temperature)
                        .setGaseous(isGaseous);
        if (!FluidRegistry.registerFluid(fluid)) {
            fluid = FluidRegistry.getFluid(name.getName());
        }

        if (!fluid.canBePlacedInWorld()) {
            Block block = new BlockIUFluid(name, fluid, new MaterialLiquid(MapColor.TNT), color);
            fluid.setBlock(block);
            fluid.setUnlocalizedName(block.getUnlocalizedName().substring(4));
        } else {
            Block var10 = fluid.getBlock();
        }

        name.setInstance(fluid);
        FluidRegistry.addBucketForFluid(fluid);
    }

}
