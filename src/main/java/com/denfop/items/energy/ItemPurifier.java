package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.items.BaseElectricItem;
import com.denfop.tiles.base.TileEntityDoubleMolecular;
import com.denfop.tiles.base.TileEntityMolecularTransformer;
import com.denfop.tiles.base.TileEntityMultiMachine;
import com.denfop.tiles.panels.entity.TileEntitySolarPanel;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
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

public class ItemPurifier extends BaseElectricItem implements IModelRegister {


    public ItemPurifier(String name, double maxCharge, double transferLimit, int tier) {
        super(name, maxCharge, transferLimit, tier);
        this.setMaxDamage(27);
        setMaxStackSize(1);

        IUCore.proxy.addIModelRegister(this);
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
        if (!(tile instanceof TileEntitySolarPanel) && !(tile instanceof TileEntityMultiMachine) && !(tile instanceof TileEntityMolecularTransformer) && !(tile instanceof TileEntityDoubleMolecular)) {
            return EnumActionResult.PASS;
        }
        if (tile instanceof TileEntitySolarPanel) {
            TileEntitySolarPanel base = (TileEntitySolarPanel) tile;
            double energy = 10000;
            if (base.time > 0) {
                energy = (double) 10000 / (double) (base.time / 20);
            }
            if (base.time1 > 0 && base.time <= 0) {
                energy += (double) 10000 / (double) (base.time1 / 20);
            }
            if (base.time2 > 0 && base.time <= 0 && base.time1 <= 0) {
                energy += ((double) 10000 / (double) (base.time2 / 20)) + 10000;
            }
            if (ElectricItem.manager.canUse(itemstack, energy)) {
                base.time = 28800;
                base.time1 = 14400;
                base.time2 = 14400;
                base.work = true;
                base.work1 = true;
                base.work2 = true;
                ElectricItem.manager.use(itemstack, 1000, player);
                if (IC2.platform.isRendering()) {
                    IUCore.audioManager.playOnce(
                            player,
                            com.denfop.audio.PositionSpec.Hand,
                            "Tools/purifier.ogg",
                            true,
                            IC2.audioManager.getDefaultVolume()
                    );
                }
                return EnumActionResult.SUCCESS;
            }
            return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
        } else if (tile instanceof TileEntityMultiMachine) {
            if (!ElectricItem.manager.canUse(itemstack, 500)) {
                return EnumActionResult.PASS;
            }
            TileEntityMultiMachine base = (TileEntityMultiMachine) tile;
            ItemStack stack_rf = ItemStack.EMPTY;
            ItemStack stack_quickly = ItemStack.EMPTY;
            ItemStack stack_modulesize = ItemStack.EMPTY;
            ItemStack panel = ItemStack.EMPTY;
            if (base.rf) {
                stack_rf = new ItemStack(IUItem.module7, 1, 4);
            }
            if (base.quickly) {
                stack_quickly = new ItemStack(IUItem.module_quickly);
            }
            if (base.modulesize) {
                stack_modulesize = new ItemStack(IUItem.module_stack);
            }
            if (base.solartype != null) {
                panel = new ItemStack(IUItem.module6, 1, base.solartype.meta);
            }
            if (!stack_rf.isEmpty() || !stack_quickly.isEmpty() || !stack_modulesize.isEmpty() || !panel.isEmpty()) {
                final EntityItem item = new EntityItem(world);
                if (!stack_rf.isEmpty()) {
                    item.setItem(stack_rf);
                    base.module--;
                    base.rf = false;
                } else if (!stack_quickly.isEmpty()) {
                    item.setItem(stack_quickly);
                    base.module--;
                    base.quickly = false;
                } else if (!stack_modulesize.isEmpty()) {
                    item.setItem(stack_modulesize);
                    base.modulesize = false;
                    base.module--;
                } else if (!panel.isEmpty()) {
                    item.setItem(panel);
                    base.solartype = null;
                }
                if (!player.getEntityWorld().isRemote) {
                    item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                    item.setPickupDelay(0);
                    world.spawnEntity(item);
                    ElectricItem.manager.use(itemstack, 500, null);
                    if (IC2.platform.isRendering()) {
                        IUCore.audioManager.playOnce(
                                player,
                                com.denfop.audio.PositionSpec.Hand,
                                "Tools/purifier.ogg",
                                true,
                                IC2.audioManager.getDefaultVolume()
                        );
                    }
                    return EnumActionResult.SUCCESS;
                }

            }
        } else if (tile instanceof TileEntityMolecularTransformer) {
            TileEntityMolecularTransformer base = (TileEntityMolecularTransformer) tile;
            if (!ElectricItem.manager.canUse(itemstack, 500)) {
                return EnumActionResult.PASS;
            }
            if (base.rf) {
                final ItemStack stack_rf = new ItemStack(IUItem.module7, 1, 4);
                base.rf = false;
                final EntityItem item = new EntityItem(world);
                item.setItem(stack_rf);
                if (!player.getEntityWorld().isRemote) {
                    item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                    item.setPickupDelay(0);
                    world.spawnEntity(item);
                    ElectricItem.manager.use(itemstack, 500, null);
                    if (IC2.platform.isRendering()) {
                        IUCore.audioManager.playOnce(
                                player,
                                com.denfop.audio.PositionSpec.Hand,
                                "Tools/purifier.ogg",
                                true,
                                IC2.audioManager.getDefaultVolume()
                        );
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
        } else {
            TileEntityDoubleMolecular base = (TileEntityDoubleMolecular) tile;
            if (!ElectricItem.manager.canUse(itemstack, 500)) {
                return EnumActionResult.PASS;
            }
            if (base.rf) {
                final ItemStack stack_rf = new ItemStack(IUItem.module7, 1, 4);
                base.rf = false;
                final EntityItem item = new EntityItem(world);
                item.setItem(stack_rf);
                if (!player.getEntityWorld().isRemote) {
                    item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                    item.setPickupDelay(0);
                    world.spawnEntity(item);
                    ElectricItem.manager.use(itemstack, 500, null);
                    if (IC2.platform.isRendering()) {
                        IUCore.audioManager.playOnce(
                                player,
                                com.denfop.audio.PositionSpec.Hand,
                                "Tools/purifier.ogg",
                                true,
                                IC2.audioManager.getDefaultVolume()
                        );
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return EnumActionResult.PASS;
    }

}
