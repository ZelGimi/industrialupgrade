package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityMagnet;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerMagnet extends ContainerFullInv<TileEntityMagnet> {

    public ContainerMagnet(EntityPlayer entityPlayer, TileEntityMagnet tileEntity1) {
        this(entityPlayer, tileEntity1, 166);
        if (tileEntity1.outputSlot != null) {

            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j, 30 + 18 * j, 6
                ));
            }
            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 6, 30 + 18 * j, 6 + 18
                ));
            }
            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 12, 30 + 18 * j, 6 + 18 + 18
                ));
            }
            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 18, 30 + 18 * j, 6 + 18 + 18 + 18
                ));
            }
        }
    }

    public ContainerMagnet(EntityPlayer entityPlayer, TileEntityMagnet tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energyconsume");
        ret.add("energy");
        ret.add("sound");
        return ret;
    }

}
