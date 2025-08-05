package com.denfop.container;

import com.denfop.tiles.base.TileEntitySimulatorReactor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ContainerSimulationReactors extends ContainerFullInv<TileEntitySimulatorReactor> {

    public ContainerSimulationReactors(TileEntitySimulatorReactor tileEntitySimulatorReactor, Player var1) {
        super(var1, tileEntitySimulatorReactor, 235, 228);
        addSlotToContainer(new SlotInvSlot(
                tileEntitySimulatorReactor.scheduleSlot,
                0,
                9,
                121
        ));
        if (tileEntitySimulatorReactor.type != -1 && tileEntitySimulatorReactor.levelReactor != -1 && tileEntitySimulatorReactor.reactors != null) {
            int minX = (int) (240 / 2 - (tileEntitySimulatorReactor.reactors.getWidth() / 2D) * 18);
            int minY = (int) (150 / 2 - (tileEntitySimulatorReactor.reactors.getHeight() / 2D) * 18);

            for (int y = 0; y < tileEntitySimulatorReactor.reactors.getHeight(); y++) {
                for (int x = 0; x < tileEntitySimulatorReactor.reactors.getWidth(); x++) {
                    final int finalY = y;
                    final int finalX = x;
                    addSlotToContainer(new SlotVirtual(
                            tileEntitySimulatorReactor,
                            finalY * tileEntitySimulatorReactor.reactors.getWidth() + finalX,
                            minX + finalX * 18,
                            minY + finalY * 18,
                            tileEntitySimulatorReactor.invSlot
                    ) {


                        @Override
                        public void set(final ItemStack stack) {
                            super.set(stack);
                            tileEntitySimulatorReactor.invSlot.set(this.getSlotIndex(), stack);
                        }

                        @Override
                        public int getSlotIndex() {
                            return finalY * tileEntitySimulatorReactor.reactors.getWidth() + finalX;
                        }
                    });
                }
            }
        }
    }

}
