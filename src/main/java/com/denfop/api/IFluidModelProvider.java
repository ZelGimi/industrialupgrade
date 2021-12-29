//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.api;

import com.denfop.blocks.FluidName;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IFluidModelProvider {

    @SideOnly(Side.CLIENT)
    void registerModels(FluidName var1);

}
