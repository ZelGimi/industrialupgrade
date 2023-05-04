package com.denfop.container;

import com.denfop.tiles.reactors.TileEntityBaseNuclearReactorElectric;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerBaseNuclearReactor extends ContainerFullInv<TileEntityBaseNuclearReactorElectric> {

    public final short size;

    public ContainerBaseNuclearReactor(EntityPlayer entityPlayer, TileEntityBaseNuclearReactorElectric tileEntity1) {
        super(entityPlayer, tileEntity1, 214, 243);
        this.size = tileEntity1.getReactorSize();
        int startX = 26 - 18;
        int startY = 25;

        if (tileEntity1.sizeY == 7) {
            startY -= 18;
        }
        int col;
        int col1;
        for (col = 0; col < tileEntity1.reactorSlot.size(); col++) {
            col1 = col % this.size;
            int y = col / this.size;
            this.addSlotToContainer(new SlotInvSlot(tileEntity1.reactorSlot, col, startX + 18 * col1, startY + 18 * y));

        }


    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("heat");
        ret.add("maxHeat");
        ret.add("sizeX");
        ret.add("sizeY");
        ret.add("background");
        ret.add("storage");
        ret.add("output");
        ret.add("coef");
        ret.add("coef1");
        ret.add("work");
        ret.add("perenergy");
        ret.add("pastEnergy");
        ret.add("change");
        return ret;
    }

}
