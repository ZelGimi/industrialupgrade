//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.items.BaseElectricItem;
import com.denfop.tiles.panels.entity.TileEntitySolarPanel;
import ic2.api.item.ElectricItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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

public class ItemPurifier extends BaseElectricItem implements IModelRegister {


    public ItemPurifier(String name, double maxCharge, double transferLimit, int tier) {
        super(name, maxCharge, transferLimit, tier);
        this.setMaxDamage(27);
        setMaxStackSize(1);

        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(String name) {
        this.registerModel(0, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name) {
        registerModel(this, meta, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name, String extraName) {
        registerModel(this, meta, name, extraName);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {
        StringBuilder loc = new StringBuilder();
        loc.append(Constants.MOD_ID);
        loc.append(':');
        loc.append("energy").append("/").append(name);

        return new ModelResourceLocation(loc.toString(), null);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name, String extraName) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }


    public boolean canProvideEnergy(ItemStack stack) {
        return true;
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
        ItemStack itemstack = player.getHeldItem(hand);
        if (!(tile instanceof TileEntitySolarPanel)) {
            return EnumActionResult.PASS;
        }
        TileEntitySolarPanel base = (TileEntitySolarPanel) tile;
        double energy = 10000;
        if (base.time > 0)
            energy = (double) 10000 / (double) (base.time / 20);
        if (base.time1 > 0 && base.time <= 0)
            energy += (double) 10000 / (double) (base.time1 / 20);
        if (base.time2 > 0 && base.time <= 0 && base.time1 <= 0)
            energy += ((double) 10000 / (double) (base.time2 / 20)) + 10000;
        if (ElectricItem.manager.canUse(itemstack, energy)) {
            base.time = 28800;
            base.time1 = 14400;
            base.time2 = 14400;
            base.work = true;
            base.work1 = true;
            base.work2 = true;
            ElectricItem.manager.use(itemstack, 1000, player);
            return  EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

}
