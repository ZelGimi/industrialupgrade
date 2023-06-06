package com.denfop.container;

import com.denfop.tiles.base.TileEntityTransformer;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerTransformer extends ContainerFullInv<TileEntityTransformer> {

    public ContainerTransformer(EntityPlayer player, TileEntityTransformer tileEntity1, int height) {
        super(player, tileEntity1, height);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("configuredMode");
        ret.add("inputFlow");
        ret.add("outputFlow");
        return ret;
    }

}
