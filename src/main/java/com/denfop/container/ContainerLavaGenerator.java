package com.denfop.container;


import com.denfop.tiles.mechanism.generator.things.fluid.TileLavaGenerator;
import net.minecraft.world.entity.player.Player;

public class ContainerLavaGenerator extends ContainerFullInv<TileLavaGenerator> {

    public ContainerLavaGenerator(Player entityPlayer, TileLavaGenerator tileEntity1) {
        super(entityPlayer, tileEntity1, 166);

        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 125, 59));
        addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot, 0, 125, 23));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
    }


}
