package com.denfop.container;

import com.denfop.tiles.base.TileAnalyzer;
import net.minecraft.world.entity.player.Player;

public class ContainerAnalyzer extends ContainerFullInv<TileAnalyzer> {

    public ContainerAnalyzer(Player entityPlayer, TileAnalyzer tileEntity1) {
        this(entityPlayer, tileEntity1, 214, 198 + 58);

    }

    public ContainerAnalyzer(Player entityPlayer, TileAnalyzer tileEntity1, int width, int height) {
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


}
