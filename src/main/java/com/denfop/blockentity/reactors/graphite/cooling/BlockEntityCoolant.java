package com.denfop.blockentity.reactors.graphite.cooling;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.graphite.ICooling;
import com.denfop.componets.CoolComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityCoolant extends BlockEntityMultiBlockElement implements ICooling {

    public final CoolComponent cold;
    private final int levelBlock;
    private IGraphiteReactor gasReactor;

    public BlockEntityCoolant(int levelBlock, MultiBlockEntity block, BlockPos pos, BlockState state) {
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
