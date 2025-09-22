package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.register.Register;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemDeplanner extends Item implements IModelRegister {

    private final String name;

    public ItemDeplanner() {
        super();
        this.setMaxStackSize(1);
        this.canRepair = false;
        this.name = "multiblock_deplanner";
        this.setCreativeTab(IUCore.EnergyTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "tools" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    @Override
    public EnumActionResult onItemUseFirst(
            final EntityPlayer player,
            final World world,
            final BlockPos pos,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ,
            final EnumHand hand
    ) {

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IMainMultiBlock) {
            IMainMultiBlock mainMultiBlock = (IMainMultiBlock) tile;
            List<ItemStack> itemStackList = new ArrayList<>();
            if (mainMultiBlock.isFull()) {

                for (Map.Entry<BlockPos, ItemStack> entry : mainMultiBlock.getMultiBlockStucture().ItemStackMap.entrySet()) {
                    BlockPos pos1;
                    if (entry.getKey().equals(BlockPos.ORIGIN)) {
                        continue;
                    }
                    if (entry.getValue().isEmpty()) {
                        continue;
                    }

                    switch (((TileMultiBlockBase) mainMultiBlock).getFacing()) {
                        case NORTH:
                            pos1 = new BlockPos(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ());
                            break;
                        case EAST:
                            pos1 = new BlockPos(entry.getKey().getZ() * -1, entry.getKey().getY(), entry.getKey().getX());
                            break;
                        case WEST:
                            pos1 = new BlockPos(entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX() * -1);
                            break;
                        case SOUTH:
                            pos1 = new BlockPos(entry.getKey().getX() * -1, entry.getKey().getY(), entry.getKey().getZ() * -1);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: ");
                    }
                    TileEntityBlock block = (TileEntityBlock) world.getTileEntity(pos.add(pos1));
                    itemStackList.add(block.getPickBlock(null,null));
                    world.removeTileEntity(pos.add(pos1));
                    world.setBlockToAir(pos.add(pos1));

                }
                itemStackList.add(mainMultiBlock.getMultiBlockStucture().ItemStackMap.get(BlockPos.ORIGIN).copy());

                ((TileMultiBlockBase) mainMultiBlock).onUnloaded();
                world.removeTileEntity(pos);
                world.setBlockToAir(pos);
                if (!world.isRemote) {
                    for (ItemStack stack : itemStackList) {
                        EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, stack);
                        item.setPickupDelay(0);
                        world.spawnEntity(item);
                    }
                }
                return EnumActionResult.SUCCESS;
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @Override
    public void registerModels() {
        registerModel(this, 0, this.name);
    }

}
