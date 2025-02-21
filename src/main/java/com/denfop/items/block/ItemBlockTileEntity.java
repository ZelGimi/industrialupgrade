package com.denfop.items.block;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.network.packet.PacketUpdateTile;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
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

    @Override
    public Block getBlock() {
        return super.getBlock();
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        TileEntity tile = worldIn.getTileEntity(pos);
        if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(facing);
        }

        ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(this.block, pos, false,
                facing, player) && (tile == null || player.isSneaking() || (tile != null && tile instanceof TileEntityBlock && !((TileEntityBlock) tile).onActivated(player,hand,facing,hitX,hitY,hitZ))))
        {
            int i = this.getMetadata(itemstack.getMetadata());
            IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);

            if (placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1))
            {
                iblockstate1 = worldIn.getBlockState(pos);
                SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
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
        if (te.canPlace(te, pos, world)) {
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
        return false;
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
            block.getDummyTe().addInformation(stack, tooltip);
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
        return stack == null ? null : !((BlockTileEntity) this.block).teInfo.getIdMap().isEmpty() ?
                ((BlockTileEntity) this.block).teInfo.getIdMap().get(stack.getItemDamage()) : null;
    }

}
