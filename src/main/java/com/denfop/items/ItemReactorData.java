package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.register.Register;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemReactorData extends Item implements IModelRegister {

    private final String name;

    public ItemReactorData() {
        super();
        this.setMaxStackSize(1);
        this.canRepair = false;
        this.name = "reactor_data_item";
        this.setCreativeTab(IUCore.ItemTab);
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

    public String getUnlocalizedName(ItemStack stack) {
        return "iu." + super.getUnlocalizedName().substring(5);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World world,
            final List<String> list,
            final ITooltipFlag p_77624_4_
    ) {
        super.addInformation(stack, world, list, p_77624_4_);
    }

    @Override
    public EnumActionResult onItemUse(
            final EntityPlayer player,
            final World world,
            final BlockPos pos,
            final EnumHand hand,
            final EnumFacing facing,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (world.isRemote) {
            return EnumActionResult.PASS;
        }
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileMultiBlockBase && tileEntity instanceof IAdvReactor) {
            TileMultiBlockBase tileMultiBlockBase = (TileMultiBlockBase) tileEntity;
            final NBTTagCompound nbt = ModUtils.nbt(player.getHeldItem(hand));
            nbt.setInteger("x", tileMultiBlockBase.getPos().getX());
            nbt.setInteger("y", tileMultiBlockBase.getPos().getY());
            nbt.setInteger("z", tileMultiBlockBase.getPos().getZ());
            nbt.setString("name", tileMultiBlockBase.getPickBlock(player, null).getDisplayName());
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public void registerModels() {
        registerModel(this, 0, this.name);
    }

}
