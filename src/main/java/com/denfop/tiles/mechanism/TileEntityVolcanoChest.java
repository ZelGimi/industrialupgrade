package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.IULoots;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.BlockVolcanoChest;
import com.denfop.container.ContainerVolcanoChest;
import com.denfop.gui.GuiVolcanoChest;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;

public class TileEntityVolcanoChest extends TileEntityInventory {

    public final InvSlot invSlot;

    public TileEntityVolcanoChest() {
        this.invSlot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT_OUTPUT, 27);

    }

    public List<ItemStack> generateLoot() {
        List<ItemStack> stacks = new LinkedList<>();
        final LootContext.Builder lootcontext$builder = (new LootContext.Builder((WorldServer) this.world));
        IULoots.VOLCANO_LOOT_POOL.generateLoot(stacks, this.getWorld().rand, lootcontext$builder.build());
        return stacks;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiVolcanoChest(getGuiContainer(var1));
    }

    @Override
    public ContainerVolcanoChest getGuiContainer(final EntityPlayer var1) {
        return new ContainerVolcanoChest(this, var1);
    }

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        if (this.getWorld().isRemote) {
            return;
        }
        List<ItemStack> stacks = generateLoot();
        for (ItemStack stack1 : stacks) {
            int index;
            do {
                index = world.rand.nextInt(27);
            } while (!this.invSlot.get(index).isEmpty());
            this.invSlot.put(index, stack1);
        }
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.volcanoChest;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockVolcanoChest.volcano_chest;
    }

}
