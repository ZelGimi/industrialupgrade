package com.denfop.register;

import com.denfop.blocks.BlockFluidIU;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.IUFluid;
import com.denfop.blocks.fluid.IUFluidType;
import com.denfop.items.ItemBucket;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.denfop.DataItem.objects;
import static com.denfop.register.Register.BLOCKS;
import static com.denfop.register.Register.ITEMS;

public class FluidHandler {

    public final IUFluidType fluidType;
    public final DeferredHolder<Block, BlockFluidIU> liquedBlock;
    public final DeferredHolder<Fluid, IUFluid> source;
    public final DeferredHolder<Fluid, IUFluid> flowing;
    private BaseFlowingFluid.Properties properties;

    public FluidHandler(IUFluidType fluidType, FluidName fluidName) {
        this.fluidType = fluidType;
        this.source = Register.FLUIDS.register(fluidName.getName().toLowerCase(), () -> new IUFluid(this.properties, true));
        this.flowing = Register.FLUIDS.register(fluidName.getName().toLowerCase() + "_flowing",
                () -> new IUFluid(this.properties, false));
        MapColor steam = MapColor.COLOR_GRAY;
        DeferredHolder<Item, Item> bucket = ITEMS.register("bucket/" + fluidName.name().toLowerCase().replace("fluid", ""), () -> new ItemBucket(source.get(), fluidName));
        objects.add(bucket);
        DeferredHolder<Block, BlockFluidIU> blockRegistryObject = BLOCKS.register("fluid/" + fluidName.name().toLowerCase(), () -> new BlockFluidIU(source, BlockBehaviour.Properties.of().mapColor(steam).liquid().lightLevel(state -> source.get().getFluidType().getLightLevel()).replaceable()));
        this.liquedBlock = blockRegistryObject;
        this.properties = new BaseFlowingFluid.Properties(() -> this.fluidType, this.source, this.flowing).slopeFindDistance(2).levelDecreasePerBlock(2).bucket(bucket).block(blockRegistryObject);

    }

    public DeferredHolder<Block, BlockFluidIU> getLiquedBlock() {
        return liquedBlock;
    }

    public IUFluidType getFluidType() {
        return fluidType;
    }

    public BaseFlowingFluid.Properties getProperties() {
        return properties;
    }

    public void setProperties(BaseFlowingFluid.Properties properties) {
        this.properties = properties;
    }


}
