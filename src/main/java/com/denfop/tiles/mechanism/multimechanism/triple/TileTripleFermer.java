package com.denfop.tiles.mechanism.multimechanism.triple;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import com.denfop.tiles.mechanism.multimechanism.IFarmer;
import net.minecraft.item.ItemStack;

public class TileTripleFermer extends TileMultiMachine implements IFarmer {

    private final InvSlot fertilizerSlot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    int col = 0;

    public TileTripleFermer() {
        super(EnumMultiMachine.TRIPLE_Fermer.usagePerTick, EnumMultiMachine.TRIPLE_Fermer.lenghtOperation
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
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.075));
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
        return BlockMoreMachine3.triple_farmer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
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
