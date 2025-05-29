package com.denfop.container;


import com.denfop.tiles.mechanism.generator.things.fluid.TileWaterGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerWaterGenerator extends ContainerFullInv<TileWaterGenerator> {

    public ContainerWaterGenerator(Player entityPlayer, TileWaterGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 125, 59));
        addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot, 0, 125, 23));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
    }


}
