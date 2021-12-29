//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by translate decompiler)
//

package com.denfop.blocks;

import com.denfop.Constants;
import com.denfop.api.IFluidModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.IdentityHashMap;
import java.util.Map;

public class IUFluid extends Fluid implements IFluidModelProvider {

    private static final ResourceLocation fluidLocation = new ResourceLocation(Constants.MOD_ID, "fluid");

    public IUFluid(FluidName name) {
        super(name.getName(), name.getTextureLocation(false), name.getTextureLocation(true));
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(FluidName name) {
        if (name.getInstance().canBePlacedInWorld()) {
            final String variant = "type=" + name.name();
            ModelLoader.setCustomStateMapper(this.getBlock(), new IStateMapper() {
                public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
                    Map<IBlockState, ModelResourceLocation> ret = new IdentityHashMap();
                    ModelResourceLocation loc = new ModelResourceLocation(IUFluid.fluidLocation, variant);

                    for (final IBlockState state : IUFluid.this.getBlock().getBlockState().getValidStates()) {
                        ret.put(state, loc);
                    }

                    return ret;
                }
            });
            Item item = Item.getItemFromBlock(this.getBlock());
            if (item != null && item != Items.AIR) {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(fluidLocation, variant));
            }

        }
    }

}
