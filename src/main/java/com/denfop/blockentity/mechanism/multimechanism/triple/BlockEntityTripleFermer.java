package com.denfop.blockentity.mechanism.multimechanism.triple;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blockentity.mechanism.multimechanism.IFarmer;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.inventory.Inventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityTripleFermer extends BlockEntityMultiMachine implements IFarmer {

    private final Inventory fertilizerSlot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    int col = 0;

    public BlockEntityTripleFermer(BlockPos pos, BlockState state) {
        super(EnumMultiMachine.TRIPLE_Fermer.usagePerTick, EnumMultiMachine.TRIPLE_Fermer.lenghtOperation
                , BlockMoreMachine3Entity.triple_farmer, pos, state);
        this.fertilizerSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.fertilizer.getItem();
            }

            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.FERTILIZER;
            }
        };
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.075));
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
        return BlockMoreMachine3Entity.triple_farmer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3.getBlock(getTeBlock().getId());
    }

    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_Fermer;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockFermer2.name");
    }

    public String getStartSoundFile() {
        return "Machines/Fermer.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
