package com.denfop.container;

import com.denfop.tiles.base.TileEntityLimiter;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerBlockLimiter extends ContainerFullInv<TileEntityLimiter> {

    public ContainerBlockLimiter(TileEntityLimiter tileEntityLimiter, EntityPlayer entityPlayer) {
        super(entityPlayer, tileEntityLimiter, 166);
        addSlotToContainer(new SlotInvSlot(tileEntityLimiter.slot, 0, 105, 25));


    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("max_value");
        return ret;
    }

}
