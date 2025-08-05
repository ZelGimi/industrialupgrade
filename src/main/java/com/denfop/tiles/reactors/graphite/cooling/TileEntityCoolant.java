package com.denfop.tiles.reactors.graphite.cooling;

import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.componets.CoolComponent;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.graphite.ICooling;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityCoolant extends TileEntityMultiBlockElement implements ICooling {

    public final CoolComponent cold;
    private final int levelBlock;
    private IGraphiteReactor gasReactor;

    public TileEntityCoolant(int levelBlock, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.levelBlock = levelBlock;
        this.cold = this.addComponent(CoolComponent.asBasicSource(this, 8 * Math.pow(2, this.levelBlock), 14));
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getMain() != null) {
            this.gasReactor = (IGraphiteReactor) this.getMain();
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
        return levelBlock;
    }


    @Override
    public double work(double heat) {
        return Math.max(1, heat - this.getWorld().random.nextInt(40 * (levelBlock + 1)));
    }

}
