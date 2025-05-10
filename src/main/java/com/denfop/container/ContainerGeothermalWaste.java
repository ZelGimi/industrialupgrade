package com.denfop.container;

import com.denfop.tiles.geothermalpump.TileEntityGeothermalWaste;
import net.minecraft.world.entity.player.Player;

public class ContainerGeothermalWaste extends ContainerFullInv<TileEntityGeothermalWaste> {

    public ContainerGeothermalWaste(TileEntityGeothermalWaste tileEntityGeothermalWaste, Player var1) {
        super(tileEntityGeothermalWaste, var1);


        for (int i = 0; i < 4; i++) {
            int xDisplayPosition1 = 176 / 2 - 36;

            addSlotToContainer(new SlotInvSlot(tileEntityGeothermalWaste.getSlot(), i, xDisplayPosition1 + i * 18, 30));

        }
    }

}
