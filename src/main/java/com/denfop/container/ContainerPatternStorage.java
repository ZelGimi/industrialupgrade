package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityPatternStorage;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerPatternStorage extends ContainerFullInv<TileEntityPatternStorage> {

    public ContainerPatternStorage(EntityPlayer player, TileEntityPatternStorage tileEntity1) {
        super(player, tileEntity1, 166);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.diskSlot, 0, 18, 20));
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("index");
        ret.add("maxIndex");
        ret.add("pattern");
        ret.add("patternUu");
        ret.add("patternEu");
        return ret;
    }

}
