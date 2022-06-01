package com.denfop.api.hadroncollider;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class Structures {

    private final EnumLevelCollider level;
    private final Map<BlockPos, Class> map;
    private final BlockPos pos;

    public Structures(EnumLevelCollider levelCollider, BlockPos pos) {
        this.level = levelCollider;
        this.map = new HashMap<>();
        this.pos = pos;
        this.process();

    }

    public Structures(EnumLevelCollider levelCollider) {
        this.level = levelCollider;
        this.map = new HashMap<>();
        this.pos = BlockPos.ORIGIN;
        this.process();
    }

    public IMainController hasController(BlockPos pos, Class classes, World world) {
        for (Map.Entry<BlockPos, Class> entry : this.map.entrySet()) {
            if (entry.getValue() == classes) {
                final BlockPos pos1 = entry.getKey();
                final BlockPos pos2 = new BlockPos(pos.getX() - pos1.getX(), pos.getY() - pos1.getY(), pos.getZ() - pos1.getZ());
                if (world.getTileEntity(pos2) instanceof IMainController) {
                    return (IMainController) world.getTileEntity(pos2);
                }
            }
        }
        return null;
    }

    public boolean update(IMainController controller) {
        BlockPos pos = ((TileEntity) controller).getPos();
        for (Map.Entry<BlockPos, Class> entry : this.map.entrySet()) {
            final BlockPos pos1 = entry.getKey();
            final BlockPos pos2 = pos.add(pos1);
            if (((TileEntity) controller).getWorld().getTileEntity(pos2) == null) {
                return false;
            }
            if (!(((TileEntity) controller).getWorld().getTileEntity(pos2) instanceof IColliderBuilding)) {
                return false;
            }

        }
        return true;
    }

    private void process() {
        switch (this.level) {
            case ONE:
                this.map.put(pos, IMainController.class);
                this.map.put(pos.add(0, 0, 1), IPurifierBlock.class);
                this.map.put(pos.add(1, 0, 1), IOverclockingBlock.class);
                this.map.put(pos.add(1, 0, 0), IExtractBlock.class);
                break;
            case TWO:
                this.map.put(pos, IMainController.class);
                this.map.put(pos.add(0, 0, -1), IReceivedBlock.class);
                this.map.put(pos.add(0, 0, 1), IOverclockingBlock.class);

                this.map.put(pos.add(1, 0, 2), IBlocksStabilizator.class);
                this.map.put(pos.add(2, 0, 2), IBatteryBlock.class);
                this.map.put(pos.add(3, 0, 2), IBlocksStabilizator.class);

                this.map.put(pos.add(1, 0, -2), IBlocksStabilizator.class);
                this.map.put(pos.add(2, 0, -2), IMagnetBlock.class);
                this.map.put(pos.add(3, 0, -2), IBlocksStabilizator.class);

                this.map.put(pos.add(4, 0, 0), IBlocksStabilizator.class);
                this.map.put(pos.add(4, 0, -1), IExtractBlock.class);
                this.map.put(pos.add(4, 0, 1), IBlocksStabilizator.class);

                break;
        }
    }


}
