package com.denfop.componets;

import com.denfop.blockentity.base.BlockEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class Redstone extends AbstractComponent {

    private final List<RedstoneHandler> changeSubscribers = new ArrayList<>();
    protected int redstoneInput;
    private boolean needUpdate;

    public Redstone(BlockEntityInventory parent) {
        super(parent);
    }

    public void onLoaded() {
        super.onLoaded();
        this.update();
    }

    public void onUnloaded() {
        super.onUnloaded();
    }

    @Override
    public void onNeighborChange(BlockState srcBlock, BlockPos neighborPos) {
        super.onNeighborChange(srcBlock, neighborPos);
        this.needUpdate = true;
    }

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getParent().getLevel().getGameTime() % 20 == 0 && needUpdate) {
            this.update();
            needUpdate = false;
        }
    }

    public void update() {
        try {
            Level world = parent.getLevel();

            int input = world.getBestNeighborSignal(parent.getBlockPos());


            if (input != redstoneInput) {
                redstoneInput = input;

                if (changeSubscribers != null) {
                    for (RedstoneHandler subscriber : changeSubscribers) {
                        subscriber.action(input);
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public int getRedstoneInput() {
        return this.redstoneInput;
    }

    public boolean hasRedstoneInput() {
        return this.redstoneInput > 0;
    }

    public void subscribe(RedstoneHandler handler) {
        if (handler == null) {
            throw new NullPointerException("null handler");
        } else {
            this.changeSubscribers.add(handler);
        }
    }

}
