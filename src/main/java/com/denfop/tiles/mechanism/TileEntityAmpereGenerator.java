package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerAmpereGenerator;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiAmpereGenerator;
import com.denfop.gui.GuiCore;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public class TileEntityAmpereGenerator extends TileElectricMachine implements IUpdatableTileEvent, IManufacturerBlock {


    public final ComponentBaseEnergy pressure;
    public final Energy energy;
    public int levelBlock;

    public TileEntityAmpereGenerator(BlockPos pos, BlockState state) {
        super(0, 0, 1, BlockBaseMachine3.ampere_generator, pos, state);


        this.energy = this.addComponent(Energy.asBasicSink(this, 4000, 14));
        this.pressure = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.AMPERE, this, 2000));


    }

    @Override
    public int getLevelMechanism() {
        return this.levelBlock;
    }

    @Override
    public void setLevelMech(final int level) {
        this.levelBlock = level;
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
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.ampere_generator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        return packet;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {

    }


    public void updateEntityServer() {
        super.updateEntityServer();


        if (this.energy.getEnergy() >= 2 && this.pressure.getEnergy() + 1 <= this.pressure.getCapacity()) {
            int max = (int) Math.min(levelBlock + 1, energy.getEnergy() / ((levelBlock + 1) * 2));
            max = (int) Math.min(max, (this.pressure.getCapacity() - pressure.getEnergy()) / ((levelBlock + 1)));
            this.pressure.addEnergy(max);
            this.energy.useEnergy(max * 2);
            this.setActive(true);
        } else {
            setActive(false);
        }


    }


    @Override
    public ContainerAmpereGenerator getGuiContainer(final Player entityPlayer) {
        return new ContainerAmpereGenerator(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiAmpereGenerator((ContainerAmpereGenerator) menu);
    }


    @Override
    public SoundEvent getSound() {
        return null;
    }

}
