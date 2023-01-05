package com.denfop.container;

import com.denfop.tiles.base.TileEntityMultiMachine;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerMultiMetalFormer extends ContainerMultiMachine {

    public ContainerMultiMetalFormer(
            EntityPlayer entityPlayer, TileEntityMultiMachine tileEntity1,
            int sizeWorkingSlot
    ) {
        super(entityPlayer, tileEntity1, sizeWorkingSlot);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("mode");
        return ret;
    }

}
