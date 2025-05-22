package com.denfop.tiles.mechanism.multimechanism.photonic;

import com.denfop.IUItem;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import com.denfop.tiles.mechanism.multimechanism.IFarmer;
import net.minecraft.item.ItemStack;

public class TilePhotonicFermer extends TileMultiMachine implements IFarmer {

    private final InvSlot fertilizerSlot;
    int col = 0;

    public TilePhotonicFermer() {
        super(EnumMultiMachine.PHO_Fermer
        );
        this.fertilizerSlot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.fertilizer;
            }

            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.FERTILIZER;
            }
        };
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.cold.storage = 0;
    }

    @Override
    public InvSlot getFertilizerSlot() {
        return fertilizerSlot;
    }

    @Override
    public int getSize(int size) {
        size = Math.min(super.getSize(size), fertilizerSlot.get().getCount() * 8 + col);
        return size;

    }

    @Override
    public boolean canoperate(final int size) {
        return !fertilizerSlot.isEmpty() && fertilizerSlot.get().getCount() * 8 + col >= size;
    }

    @Override
    public void consume(final int size) {
        int size1 = size;
        while (size1 > 0) {
            if (col == 0) {
                col += 16;
                fertilizerSlot.get().shrink(1);
            }
            if (size1 <= col) {
                col -= size1;
                size1 = 0;
            } else {
                size1 -= col;
                col = 0;
            }
        }

    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_fermer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine;
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.PHO_Fermer;
    }


}
