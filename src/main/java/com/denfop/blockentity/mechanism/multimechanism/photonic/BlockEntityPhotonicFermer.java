package com.denfop.blockentity.mechanism.multimechanism.photonic;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blockentity.mechanism.multimechanism.IFarmer;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.inventory.Inventory;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPhotonicFermer extends BlockEntityMultiMachine implements IFarmer {

    private final Inventory fertilizerSlot;
    int col = 0;

    public BlockEntityPhotonicFermer(BlockPos pos, BlockState state) {
        super(EnumMultiMachine.PHO_Fermer, BlocksPhotonicMachine.photonic_fermer, pos, state
        );
        this.fertilizerSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.fertilizer.getItem();
            }

            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.FERTILIZER;
            }
        };
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.cold.buffer.storage = 0;
    }

    @Override
    public void readContainerPacket(CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.col = customPacketBuffer.readInt();
    }

    public int getFertilizer() {
        return col;
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer packetBuffer = super.writeContainerPacket();
        packetBuffer.writeInt(col);
        return packetBuffer;
    }

    @Override
    public Inventory getFertilizerSlot() {
        return fertilizerSlot;
    }

    @Override
    public int getSize(int size) {
        size = Math.min(super.getSize(size), fertilizerSlot.get(0).getCount() * 8 + col);
        return size;

    }

    @Override
    public boolean canoperate(final int size) {
        return !fertilizerSlot.isEmpty() && fertilizerSlot.get(0).getCount() * 8 + col >= size;
    }

    @Override
    public void consume(final int size) {
        int size1 = size;
        while (size1 > 0) {
            if (col == 0) {
                col += 16;
                fertilizerSlot.get(0).shrink(1);
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

    public MultiBlockEntity getTeBlock() {
        return BlocksPhotonicMachine.photonic_fermer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.PHO_Fermer;
    }


}
