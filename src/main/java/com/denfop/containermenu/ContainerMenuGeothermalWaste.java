package com.denfop.containermenu;

import com.denfop.blockentity.geothermalpump.BlockEntityGeothermalWaste;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuGeothermalWaste extends ContainerMenuFullInv<BlockEntityGeothermalWaste> {

    public ContainerMenuGeothermalWaste(BlockEntityGeothermalWaste tileEntityGeothermalWaste, Player var1) {
        super(tileEntityGeothermalWaste, var1);


        for (int i = 0; i < 4; i++) {
            int xDisplayPosition1 = 176 / 2 - 36;

            addSlotToContainer(new SlotInvSlot(tileEntityGeothermalWaste.getSlot(), i, xDisplayPosition1 + i * 18, 30));

        }
    }

}
