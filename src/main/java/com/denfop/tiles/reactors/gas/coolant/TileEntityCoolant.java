package com.denfop.tiles.reactors.gas.coolant;

import com.denfop.api.reactors.IGasReactor;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.componets.CoolComponent;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.ICoolant;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityCoolant extends TileEntityMultiBlockElement implements ICoolant {

    public final CoolComponent cold;
    private final int Blocklevel;
    private IGasReactor gasReactor;

    public TileEntityCoolant(int Blocklevel, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block,pos,state);
        this.Blocklevel = Blocklevel;
        this.cold = this.addComponent(CoolComponent.asBasicSource(this, 8 * Math.pow(2, this.Blocklevel), 14));
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getMain() != null) {
            this.gasReactor = (IGasReactor) this.getMain();
            if (this.gasReactor.getEnergy() != null && this.gasReactor
                    .getEnergy()
                    .getEnergy() >= 30 && this.cold.getEnergy() < this.cold.getCapacity()) {
                this.cold.addEnergy(1);
                this.gasReactor.getEnergy().useEnergy(30);
            }
            if (this.getWorld().getGameTime() % 40 == 0 && this.cold.getEnergy() > 0) {
                this.cold.useEnergy(1);
            }
        }
    }


    @Override
    public int getBlockLevel() {
        return Blocklevel;
    }

    public void setGasReactor(IGasReactor gasReactor) {
        this.gasReactor = gasReactor;
    }

    @Override
    public double getLevelRefrigerator() {
        return this.cold.getEnergy();
    }

}
