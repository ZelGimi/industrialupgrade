package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.base.BlockEntityElectricMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachineEntity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuModuleMachine;
import com.denfop.inventory.InventoryModule;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenModuleMachine;
import com.denfop.sound.EnumSound;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityModuleMachine extends BlockEntityElectricMachine
        implements IUpdatableTileEvent {


    public final InventoryModule inputslot;
    public final InventoryModule inputslotA;
    public List<TagKey<Item>> listItems = new ArrayList<>();

    public BlockEntityModuleMachine(BlockPos pos, BlockState state) {
        super(0, 10, 0, BlockBaseMachineEntity.modulator, pos, state);


        this.inputslot = new InventoryModule(this, 0, 27);
        inputslot.setStackSizeLimit(1);
        this.inputslotA = new InventoryModule(this, 1, 1);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachineEntity.modulator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines.getBlock(getTeBlock());
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.inputslot.update();
    }


    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenModuleMachine((ContainerMenuModuleMachine) menu);
    }

    public ContainerMenuModuleMachine getGuiContainer(Player entityPlayer) {
        return new ContainerMenuModuleMachine(entityPlayer, this);
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.pen.getSoundEvent();
    }


    @Override
    public void updateTileServer(Player player, double event) {
        if (!this.inputslotA.isEmpty()) {
            initiate(2);
            initiate(0);
            this.inputslotA.get(0).setTag(new CompoundTag());
            for (int i = 0; i < this.inputslot.size(); i++) {
                String l = "number_" + i;
                if (i < this.listItems.size()) {
                    ModUtils.NBTSetString(inputslotA.get(0), l, this.listItems.get(i).location().toString());
                }

            }
            final CompoundTag nbt = this.inputslotA.get(0).getTag();
            nbt.putInt("size", this.listItems.size());

        }


    }

}
