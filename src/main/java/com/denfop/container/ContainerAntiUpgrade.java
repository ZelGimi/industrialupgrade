package com.denfop.container;

import com.denfop.tiles.base.TileEntityAntiUpgradeBlock;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerAntiUpgrade extends ContainerFullInv<TileEntityAntiUpgradeBlock> {

    public ContainerAntiUpgrade(EntityPlayer entityPlayer, TileEntityAntiUpgradeBlock tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        addSlotToContainer(new SlotInvSlot(tileEntity1.input,
                0, 106, 34
        ));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                    i, 96 + i * 18, 65
            ));
        }
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("progress");
        ret.add("index");

        ret.add("sound");
        return ret;
    }

}
