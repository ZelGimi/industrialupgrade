package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.client.ComponentVisibleArea;
import com.denfop.tiles.mechanism.TileMagnet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.List;

public class TileEntityAntiMagnet extends TileEntityInventory {

    private static final List<AABB> aabbs = Collections.singletonList(new AABB(0, 0.0D, 0, 1, 1.4D, 1));
    private final ComponentVisibleArea visible;
    public String player;

    public TileEntityAntiMagnet(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.antimagnet, pos, state);
        this.player = "";
        visible = this.addComponent(new ComponentVisibleArea(this));
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        return false;
    }

    public List<AABB> getAabbs(boolean forCollision) {
        return aabbs;
    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        tooltip.add(Localization.translate("iu.antimagnet.info"));

    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        AABB axisalignedbb = new AABB(
                this.getBlockPos().getX() - 10,
                this.getBlockPos().getY() -10,
                this.getBlockPos().getZ() -10,
                this.getBlockPos().getX() +10 + 1,
                this.getBlockPos().getY() + 10 + 1,
                this.getBlockPos().getZ() +10 + 1
        );
        visible.aabb = axisalignedbb;
    }

    @Override
    public void onPlaced(final ItemStack stack, final LivingEntity placer, final Direction facing) {
        super.onPlaced(stack, placer, facing);
        if (placer instanceof Player) {
            Player player = (Player) placer;
            this.player = player.getName().getString();
            for (int x = this.pos.getX() - 10; x <= this.pos.getX() + 10; x++) {
                for (int y = this.pos.getY() - 10; y <= this.pos.getY() + 10; y++) {
                    for (int z = this.pos.getZ() - 10; z <= this.pos.getZ() + 10; z++) {
                        final BlockEntity tileEntity = getWorld().getBlockEntity(new BlockPos(x, y, z));
                        if (tileEntity != null && !(new BlockPos(
                                x,
                                y,
                                z
                        ).equals(this.pos))) {
                            if (tileEntity instanceof TileMagnet) {
                                TileMagnet tile = (TileMagnet) tileEntity;
                                if (!tile.player.equals(this.player)) {
                                    tile.work = false;
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.antimagnet;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.player = nbttagcompound.getString("player");
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putString("player", this.player);
        return nbttagcompound;
    }

}
