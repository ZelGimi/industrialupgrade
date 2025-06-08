package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerNightTransformer;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiNightTransformer;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class TileEntityNightTransformer extends TileEntityInventory implements IManufacturerBlock {

    public final ComponentBaseEnergy ne;
    public final ComponentBaseEnergy se;
    public final ComponentBaseEnergy qe;
    public int levelBlock;

    public TileEntityNightTransformer(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.night_transformer,pos,state);
        this.ne = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.NIGHT, this, 10000));
        this.se = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.SOLARIUM, this, 20000));
        this.qe = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 1000));
    }

    @Override
    public int getLevelMechanism() {
        return this.levelBlock;
    }

    public void setLevelMech(final int levelBlock) {
        this.levelBlock = levelBlock;
    }

    @Override
    public void removeLevel(final int level) {
        this.levelBlock -= level;
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (levelBlock < 10) {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation.getItem())) {
                return super.onActivated(player, hand, side, vec3);
            } else {
                stack.shrink(1);
                this.levelBlock++;
                return true;
            }
        } else {
            return super.onActivated(player, hand, side, vec3);
        }
    }



    public List<ItemStack> getWrenchDrops(Player player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.levelBlock != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation.getItem(), this.levelBlock));
            this.levelBlock = 0;
        }
        return ret;
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.levelBlock = nbttagcompound.getInt("level");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("level", this.levelBlock);
        return nbttagcompound;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiNightTransformer((ContainerNightTransformer) menu);
    }

    @Override
    public ContainerNightTransformer getGuiContainer(final Player var1) {
        return new ContainerNightTransformer(this, var1);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.qe.getEnergy() >= 5 && this.se.getEnergy() >= 2 && this.ne.getEnergy() + 1 <= this.ne.getCapacity()) {
            int max = (int) Math.min((levelBlock + 1)*5, qe.getEnergy() / ((levelBlock + 1) * 5));
            max = (int) Math.min(max, se.getEnergy() / ((levelBlock + 1) * 2));
            max = (int) Math.min(max, (this.ne.getCapacity() - ne.getEnergy()) / ((levelBlock + 1)));
            this.qe.useEnergy(max * 5);
            this.se.useEnergy(max * 2);
            this.ne.addEnergy(max);
        }
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.night_transformer;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
