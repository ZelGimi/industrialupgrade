package com.denfop.container;

import com.denfop.tiles.base.TileEntityQuarryVein;
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
        ret.add("progress");
        ret.add("level");
        ret.add("vein");
        return ret;
    }


}
