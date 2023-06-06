package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.tiles.base.TileEntityTeleporter;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemFrequencyTransmitter extends Item implements IModelRegister {

    private static final String targetSetNbt = "targetSet";
    private static final String targetJustSetNbt = "targetJustSet";
    private static final String targetXNbt = "targetX";
    private static final String targetYNbt = "targetY";
    private static final String targetZNbt = "targetZ";
    private final String name;

    public ItemFrequencyTransmitter() {
        this.setMaxStackSize(1);
        this.name = "frequency_transmitter";
        this.setCreativeTab(IUCore.ItemTab);
        IUCore.proxy.addIModelRegister(this);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);

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

    public @NotNull ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = StackUtil.get(player, hand);
        if (IC2.platform.isSimulating()) {
            NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
            boolean hadJustSet = nbtData.getBoolean("targetJustSet");
            if (nbtData.getBoolean("targetSet") && !hadJustSet) {
                nbtData.setBoolean("targetSet", false);
                IC2.platform.messagePlayer(player, "Frequency Transmitter unlinked");
            }

            if (hadJustSet) {
                nbtData.setBoolean("targetJustSet", false);
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    public @NotNull EnumActionResult onItemUseFirst(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumFacing side,
            float hitX,
            float hitY,
            float hitZ,
            EnumHand hand
    ) {
        if (world.isRemote) {
            return EnumActionResult.PASS;
        } else {
            TileEntity te = world.getTileEntity(pos);
            if (!(te instanceof TileEntityTeleporter)) {
                return EnumActionResult.PASS;
            } else {
                TileEntityTeleporter tp = (TileEntityTeleporter) te;
                NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(StackUtil.get(player, hand));
                boolean targetSet = nbtData.getBoolean("targetSet");
                boolean justSetTarget = true;
                BlockPos target = new BlockPos(
                        nbtData.getInteger("targetX"),
                        nbtData.getInteger("targetY"),
                        nbtData.getInteger("targetZ")
                );
                if (!targetSet) {
                    targetSet = true;
                    target = tp.getPos();
                    IC2.platform.messagePlayer(player, "Frequency Transmitter linked to Teleporter.");
                } else if (tp.getPos().equals(target)) {
                    IC2.platform.messagePlayer(player, "Can't link Teleporter to itself.");
                } else if (tp.hasTarget() && tp.getTarget().equals(target)) {
                    IC2.platform.messagePlayer(player, "Teleportation link unchanged.");
                } else {
                    TileEntity targetTe = world.getTileEntity(target);
                    if (targetTe instanceof TileEntityTeleporter) {
                        tp.setTarget(target);
                        ((TileEntityTeleporter) targetTe).setTarget(pos);
                        IC2.platform.messagePlayer(player, "Teleportation link established.");
                    } else {
                        justSetTarget = false;
                        targetSet = false;
                    }
                }

                nbtData.setBoolean("targetSet", targetSet);
                nbtData.setBoolean("targetJustSet", justSetTarget);
                nbtData.setInteger("targetX", target.getX());
                nbtData.setInteger("targetY", target.getY());
                nbtData.setInteger("targetZ", target.getZ());
                return EnumActionResult.SUCCESS;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        if (nbtData.getBoolean("targetSet")) {
            tooltip.add(Localization.translate(
                    "ic2.frequency_transmitter.tooltip.target",
                    nbtData.getInteger("targetX"), nbtData.getInteger("targetY"), nbtData.getInteger("targetZ")
            ));
        } else {
            tooltip.add(Localization.translate("ic2.frequency_transmitter.tooltip.blank"));
        }

    }

    @SideOnly(Side.CLIENT)
    public void registerModels(String name) {
        this.registerModel(0, name, null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name, String extraName) {
        registerModel(this, meta, name);
    }

    @Override
    public void registerModels() {
        this.registerModels(this.name);
    }

}
