package com.denfop.tiles.quarry_earth;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEarthQuarry;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TileEntityRigDrill extends TileEntityMultiBlockElement implements IRigDrill {

    private List<DataPos> dataPos;

    public TileEntityRigDrill(BlockPos pos, BlockState state) {
        super(BlockEarthQuarry.earth_rig, pos, state);
    }


    @Override
    public BlockTileEntity getBlock() {
        return IUItem.earthQuarry.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockEarthQuarry.earth_rig;
    }

    @Override
    public void startOperation(final List<DataPos> dataPos) {
        this.dataPos = dataPos;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getMain() != null && dataPos != null) {
            TileEntityEarthQuarryController controller = (TileEntityEarthQuarryController) this.getMain();
            for (int i = 0; i < 8; i++) {
                if (dataPos.isEmpty()) {
                    break;
                }
                final DataPos dataPos1 = dataPos.remove(0);
                if (controller.getEnergy().getEnergy() > 50) {
                    BlockState state = level.getBlockState(dataPos1.getPos());
                    List<ItemStack> stacks = state.getBlock().getDrops(state, (ServerLevel) level,dataPos1.getPos()
                            , null
                    );
                    level.setBlock(dataPos1.getPos(), Blocks.AIR.defaultBlockState(),3);
                    for (ItemStack stack : stacks) {
                        chest:
                        for (IEarthChest chest : controller.earthChestList) {
                            if (chest.getSlot().add(stack)) {
                                controller.getEnergy().useEnergy(50);
                                break chest;
                            }
                        }
                    }
                }
            }
        }
    }

}
