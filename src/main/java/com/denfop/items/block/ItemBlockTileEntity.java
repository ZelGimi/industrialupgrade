package com.denfop.items.block;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.network.packet.PacketUpdateTile;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBlockTileEntity extends ItemBlockIU {

    public final ResourceLocation identifier;

    public ItemBlockTileEntity(Block block, ResourceLocation identifier) {
        super(block);
        this.setHasSubtypes(true);
        this.identifier = identifier;
    }

    public static boolean placeTeBlock(
            ItemStack stack,
            EntityLivingBase placer,
            World world,
            BlockPos pos,
            EnumFacing side,
            TileEntityBlock te
    ) {
        IBlockState oldState = world.getBlockState(pos);
        IBlockState newState = IUItem.invalid.getDefaultState();
        if (!world.setBlockState(pos, newState, 0)) {
            return false;
        } else {
            world.setTileEntity(pos, te);
            te.onPlaced(stack, placer, side);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), oldState, te.getBlockState(), 3);
            if (!world.isRemote) {
                new PacketUpdateTile(te);
            }

            return true;
        }
    }

    public String getUnlocalizedName(ItemStack stack) {
        IMultiTileBlock teBlock = this.getTeBlock(stack);
        String name = teBlock == null ? "invalid" : teBlock.getName();
        return super.getUnlocalizedName() + "." + name;
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        this.block.getSubBlocks(tab, items);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        IMultiTileBlock block = this.getTeBlock(stack);
        if (block != null && block.getDummyTe() != null) {
            block.getDummyTe().addInformation(stack, tooltip, advanced);
        }

    }

    public boolean placeBlockAt(
            ItemStack stack,
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumFacing side,
            float hitX,
            float hitY,
            float hitZ,
            IBlockState newState
    ) {
        assert newState.getBlock() == this.block;

        if (!((BlockTileEntity) this.block).canReplace(world, pos, side, stack)) {
            return false;
        } else {
            IMultiTileBlock teBlock = this.getTeBlock(stack);
            if (teBlock == null) {
                return false;
            } else {
                Class<? extends TileEntityBlock> teClass = teBlock.getTeClass();
                if (teClass == null) {
                    return false;
                } else {
                    TileEntityBlock te = TileEntityBlock.instantiate(teClass);
                    return placeTeBlock(stack, player, world, pos, side, te);
                }
            }
        }
    }


    public IMultiTileBlock getTeBlock(ItemStack stack) {
        return stack == null ? null : ((BlockTileEntity) this.block).teInfo.getIdMap().size() > 0 ?
                ((BlockTileEntity) this.block).teInfo.getIdMap().get(stack.getItemDamage()) : null;
    }

}
