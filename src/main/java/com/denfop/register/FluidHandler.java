package com.denfop.register;

import com.denfop.blocks.BlockFluidIU;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.IUFluid;
import com.denfop.blocks.fluid.IUFluidType;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;

import static com.denfop.register.Register.BLOCKS;

public class FluidHandler {
    public final RegistryObject<IUFluid> source;
    public final RegistryObject<IUFluid> flowing;
    private final IUFluidType fluidType;
    private final RegistryObject<LiquidBlock> liquedBlock;
    private ForgeFlowingFluid.Properties properties;

    public FluidHandler(IUFluidType fluidType, FluidName fluidName) {
        this.fluidType = fluidType;
        this.source = Register.FLUIDS.register(fluidName.getName().toLowerCase(), () -> new IUFluid(this.properties, true));
        this.flowing = Register.FLUIDS.register(fluidName.getName().toLowerCase() + "_flowing",
                () -> new IUFluid(this.properties, false));
        MapColor steam = MapColor.COLOR_GRAY;

        RegistryObject<LiquidBlock> blockRegistryObject = BLOCKS.register("fluid/" + fluidName.name().toLowerCase(), () -> new BlockFluidIU(source, BlockBehaviour.Properties.of().mapColor(steam).replaceable().liquid()));
        this.liquedBlock = blockRegistryObject;
        this.properties = new ForgeFlowingFluid.Properties(() -> this.fluidType, this.source, this.flowing).slopeFindDistance(2).levelDecreasePerBlock(2).block(blockRegistryObject);

    }

    public RegistryObject<LiquidBlock> getLiquedBlock() {
        return liquedBlock;
    }

    public IUFluidType getFluidType() {
        return fluidType;
    }

    public ForgeFlowingFluid.Properties getProperties() {
        return properties;
    }

    public void setProperties(ForgeFlowingFluid.Properties properties) {
        this.properties = properties;
    }


}
