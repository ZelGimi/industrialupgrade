package com.denfop.blockentity.mechanism.multiblocks.base;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.multiblock.MainMultiBlock;
import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.componets.AbstractComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BlockEntityMultiBlockElement extends BlockEntityInventory implements MultiBlockElement {

    MainMultiBlock mainMultiBlock;


    public BlockEntityMultiBlockElement(MultiBlockEntity tileBlock, BlockPos pos, BlockState state) {
        super(tileBlock, pos, state);
    }

    public boolean doesSideBlockRendering(Direction side) {
        return getMain() == null;
    }


    @Override
    public MainMultiBlock getMain() {
        return mainMultiBlock;
    }

    @Override
    public void setMainMultiElement(final MainMultiBlock main) {
        mainMultiBlock = main;
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (this.getWorld().isClientSide)
            return true;
        if (player.getItemInHand(hand).is(ItemTags.create(new ResourceLocation("forge", "tools/wrench"))))
            return false;
        if (this.getMain() != null && !this.hasOwnInventory()) {
            for (AbstractComponent component : componentList) {
                if (component.onBlockActivated(player, hand))
                    return true;
            }
            if (this.getMain() instanceof BlockEntityInventory) {
                return ((BlockEntityInventory) this.getMain()).onActivated(player, hand, side, vec3);
            } else {
                return true;
            }
        } else {
            if (this.getMain() == null && !this.hasOwnInventory())
                return false;
            return super.onActivated(player, hand, side, vec3);
        }
    }

    @Override
    public void onBlockBreak(boolean wrench) {
        super.onBlockBreak(wrench);
        if (this.getMain() != null) {
            if (this.getMain().isFull()) {
                getMain().setActivated(false);
                getMain().setFull(false);
            }
        }
    }

    @Override
    public void onUnloaded() {

        super.onUnloaded();
    }

}
