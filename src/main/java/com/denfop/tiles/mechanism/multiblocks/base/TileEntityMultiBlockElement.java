package com.denfop.tiles.mechanism.multiblocks.base;

import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.api.multiblock.IMultiElement;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.componets.AbstractComponent;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TileEntityMultiBlockElement extends TileEntityInventory implements IMultiElement {

    IMainMultiBlock mainMultiBlock;


    public TileEntityMultiBlockElement(IMultiTileBlock tileBlock, BlockPos pos, BlockState state) {
        super(tileBlock, pos, state);
    }

    public boolean doesSideBlockRendering(Direction side) {
        return getMain() == null;
    }


    @Override
    public IMainMultiBlock getMain() {
        return mainMultiBlock;
    }

    @Override
    public void setMainMultiElement(final IMainMultiBlock main) {
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
            if (this.getMain() instanceof TileEntityInventory) {
                return ((TileEntityInventory) this.getMain()).onActivated(player, hand, side, vec3);
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
