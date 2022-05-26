package com.denfop.container;

import com.denfop.tiles.base.TileEntityQuarryVein;
import ic2.core.ContainerFullInv;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerQuarryVein extends ContainerFullInv<TileEntityQuarryVein> {

    public ContainerQuarryVein(EntityPlayer entityPlayer, TileEntityQuarryVein tileEntity1) {
        this(entityPlayer, tileEntity1, 166);

    }

    public ContainerQuarryVein(EntityPlayer entityPlayer, TileEntityQuarryVein tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("empty");
        ret.add("analysis");
        ret.add("progress");
        ret.add("number");
        ret.add("x");
        ret.add("level");
        ret.add("y");
        ret.add("z");
        ret.add("energy");
        ret.add("max");
        return ret;
    }


}
