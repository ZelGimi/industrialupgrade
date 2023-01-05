package com.denfop.container;

import com.denfop.tiles.base.TileEntityAnalyzer;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerAnalyzer extends ContainerFullInv<TileEntityAnalyzer> {

    public ContainerAnalyzer(EntityPlayer entityPlayer, TileEntityAnalyzer tileEntity1) {
        this(entityPlayer, tileEntity1, 214, 198 + 58);

    }

    public ContainerAnalyzer(EntityPlayer entityPlayer, TileEntityAnalyzer tileEntity1, int width, int height) {
        super(entityPlayer, tileEntity1, width, height);
        for (int i = 0; i < tileEntity1.inputslot.size(); i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputslot,
                    i, 7 + i * 18, 56
            ));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity1.inputslotA,
                0, 78, 56
        ));

    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("xChunk");
        ret.add("zChunk");
        ret.add("xendChunk");
        ret.add("zendChunk");
        ret.add("sum");
        ret.add("sum1");
        ret.add("numberores");
        ret.add("listore");
        ret.add("listnumberore");
        ret.add("breakblock");
        ret.add("quarry");
        ret.add("analysis");
        ret.add("y");
        ret.add("energy");
        ret.add("yore");
        ret.add("middleheightores");
        ret.add("consume");

        ret.add("xcoord");
        ret.add("xendcoord");
        ret.add("zcoord");
        ret.add("zendcoord");

        return ret;
    }

}
