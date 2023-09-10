package com.denfop.componets;

import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Redstone extends AbstractComponent {

    private final List<RedstoneHandler> changeSubscribers = new ArrayList<>();
    protected int redstoneInput;

    public Redstone(TileEntityInventory parent) {
        super(parent);
    }

    public void onLoaded() {
        super.onLoaded();
        this.update();
    }

    public void onUnloaded() {
        super.onUnloaded();
    }

    public void onNeighborChange(Block srcBlock, BlockPos neighborPos) {
        super.onNeighborChange(srcBlock, neighborPos);
        this.update();
    }

    public void update() {
        try {
            World world = parent.getWorld();

            int input = world.isBlockIndirectlyGettingPowered(parent.getPos());


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
