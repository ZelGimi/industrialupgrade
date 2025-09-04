package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.base.IManufacturerBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuNightTransformer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenNightTransformer;
import com.denfop.utils.Localization;
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

public class BlockEntityNightTransformer extends BlockEntityInventory implements IManufacturerBlock {

    public final ComponentBaseEnergy ne;
    public final ComponentBaseEnergy se;
    public final ComponentBaseEnergy qe;
    public int levelBlock;

    public BlockEntityNightTransformer(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.night_transformer, pos, state);
        this.ne = this.addComponent(ComponentBaseEnergy.asBasicSource(EnergyType.NIGHT, this, 10000));
        this.se = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.SOLARIUM, this, 20000));
        this.qe = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 1000));
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.night_converter.info"));
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
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenNightTransformer((ContainerMenuNightTransformer) menu);
    }

    @Override
    public ContainerMenuNightTransformer getGuiContainer(final Player var1) {
        return new ContainerMenuNightTransformer(this, var1);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.qe.getEnergy() >= 5 && this.se.getEnergy() >= 2 && this.ne.getEnergy() + 1 <= this.ne.getCapacity()) {
            int max = (int) Math.min((levelBlock + 1) * 5, qe.getEnergy() / ((levelBlock + 1) * 5));
            max = (int) Math.min(max, se.getEnergy() / ((levelBlock + 1) * 2));
            max = (int) Math.min(max, (this.ne.getCapacity() - ne.getEnergy()) / ((levelBlock + 1)));
            this.qe.useEnergy(max * 5);
            this.se.useEnergy(max * 2);
            this.ne.addEnergy(max);
        }
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.night_transformer;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
