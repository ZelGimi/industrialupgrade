package com.denfop.blocks.blockitem;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.ItemBlockCore;
import com.denfop.tiles.base.FakePlayerSpawner;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class ItemBlockTileEntity<T extends Enum<T> & IMultiTileBlock> extends ItemBlockCore<T> {
    public final ResourceLocation identifier;

    public ItemBlockTileEntity(BlockTileEntity<T> p_40565_, T element, ResourceLocation identifier) {
        super(p_40565_, element, new Properties().tab(element.getCreativeTab()));
        p_40565_.setItem(this);
        this.identifier = identifier;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_40569_, NonNullList<ItemStack> p_40570_) {
        if (this.allowedIn(p_40569_) && getElement().hasItem()) {
            p_40570_.add(new ItemStack(this));
            if (getElement().hasOtherVersion()) {
                p_40570_.addAll(getElement().getOtherVersion(new ItemStack(this)));
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack p_40572_, @Nullable Level p_40573_, List<Component> p_40574_, TooltipFlag p_40575_) {
        IMultiTileBlock block = this.getTeBlock(p_40572_);
        if (block != null && block.getDummyTe() != null) {
            List<String> stringList = new LinkedList<>();
            block.getDummyTe().addInformation(p_40572_, stringList);
            for (String s : stringList)
                p_40574_.add(Component.literal(s));
        }

    }

    public IMultiTileBlock getTeBlock(ItemStack stack) {
        return stack == null ? null : (!((BlockTileEntity) this.getBlock()).teInfo.getIdMap().isEmpty()) ?
                (IMultiTileBlock) ((BlockTileEntity) this.getBlock()).getValue() : null;
    }

    @Nullable
    @Override
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        BlockState blockstate = level.getBlockState(blockpos);
        Block block = this.getBlock();
        IMultiTileBlock iMultiTileBlock =  getTeBlock(pContext.getItemInHand());
        if (!iMultiTileBlock.getDummyTe().canPlace(iMultiTileBlock.getDummyTe(),blockpos,level))
            return null;
        return super.updatePlacementContext(pContext);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext p_40613_) {
        BlockState blockstate = this.getBlock().getStateForPlacement(p_40613_);
        return blockstate != null && this.canPlace(p_40613_, blockstate) ? blockstate : null;

    }

    public String getDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("industrialupgrade", Registry.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem =  "industrialupgrade." +pathBuilder.toString();
            if (this.getElement().hasUniqueName()){
                this.nameItem = this.getElement().getUniqueName();
            }
        }

        return this.nameItem;
    }

    public boolean placeTeBlock(ItemStack stack, FakePlayerSpawner fakePlayer, Level world, BlockPos pos) {
        world.setBlock(pos, this.getBlock().defaultBlockState(), 11);
        this.getBlock().setPlacedBy(world, pos, this.getBlock().defaultBlockState(), fakePlayer, stack);
        return true;
    }
}
