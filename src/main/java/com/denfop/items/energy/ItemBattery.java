//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.items.BaseElectricItem;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.init.Localization;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBattery extends BaseElectricItem implements IModelRegister {

    private static final int maxLevel = 4;
    private final String name1;
    public final boolean wirellescharge;

    public ItemBattery(String name, double maxCharge, double transferLimit, int tier, boolean wirellescharge) {
        super(name, maxCharge, transferLimit, tier);
        this.setMaxDamage(16);
        this.wirellescharge = wirellescharge;
        this.name1 = name;
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void getSubItems(final CreativeTabs p_150895_1_, final NonNullList<ItemStack> var3) {
        if (this.isInCreativeTab(p_150895_1_)) {
            final ItemStack var4 = new ItemStack(this, 1);
            ElectricItem.manager.charge(var4, 2.147483647E9, Integer.MAX_VALUE, true, false);
            var3.add(var4);
            var3.add(new ItemStack(this, 1, this.getMaxDamage()));
        }
    }

    @Override
    public void registerModels() {
        registerModels(this.name1);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        StringBuilder loc = new StringBuilder();
        loc.append(Constants.MOD_ID);
        loc.append(':');
        loc.append("battery").append("/").append(name + extraName);

        return new ModelResourceLocation(loc.toString(), null);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            int damage = stack.getItemDamage();
            int maxDamage = stack.getMaxDamage() - 1;
            int level;
            if (maxDamage > 0) {
                level = Util.limit((damage * ItemBattery.maxLevel + maxDamage / 2) / maxDamage, 0, ItemBattery.maxLevel);
            } else {
                level = 0;
            }

            return getModelLocation1(name1, Integer.toString(ItemBattery.maxLevel - level));
        });

        for (int level = 0; level <= maxLevel; ++level) {
            ModelBakery.registerItemVariants(this, getModelLocation1(name1, Integer.toString(level)));
        }

    }

    @Override
    public void onUpdate(ItemStack itemStack, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
        if (!(p_77663_3_ instanceof EntityPlayer)) {
            return;
        }
        if (wirellescharge) {
            int mode = ModUtils.NBTGetInteger(itemStack, "mode");
            EntityPlayer entityplayer = (EntityPlayer) p_77663_3_;
            if (mode == 1) {
                if (IC2.platform.isSimulating()) {
                    boolean transferred = false;
                    for (int i = 0; i < 9; i++) {
                        ItemStack stack = entityplayer.inventory.mainInventory.get(i);
                        if (stack != null && !(stack.getItem() instanceof ic2.core.item.ItemBattery)) {
                            double transfer = ElectricItem.manager.discharge(
                                    itemStack,
                                    2.0D * this.transferLimit,
                                    2147483647,
                                    true,
                                    true,
                                    true
                            );
                            if (!(transfer <= 0.0D)) {
                                transfer = ElectricItem.manager.charge(stack, transfer, 2147483647, true, false);
                                if (!(transfer <= 0.0D)) {
                                    ElectricItem.manager.discharge(itemStack, transfer, 2147483647, true, true, false);
                                    transferred = true;
                                }
                            }
                        }
                    }
                    if (transferred && !IC2.platform.isRendering()) {
                        entityplayer.openContainer.detectAndSendChanges();
                    }
                }
            } else if (mode == 2) {
                if (IC2.platform.isSimulating()) {
                    boolean transferred = false;
                    for (int i = 0; i < entityplayer.inventory.mainInventory.size(); i++) {
                        ItemStack stack = entityplayer.inventory.mainInventory.get(i);
                        if (stack != null && !(stack.getItem() instanceof ic2.core.item.ItemBattery)) {
                            double transfer = ElectricItem.manager.discharge(
                                    itemStack,
                                    2.0D * this.transferLimit,
                                    2147483647,
                                    true,
                                    true,
                                    true
                            );
                            if (!(transfer <= 0.0D)) {
                                transfer = ElectricItem.manager.charge(stack, transfer, 2147483647, true, false);
                                if (!(transfer <= 0.0D)) {
                                    ElectricItem.manager.discharge(itemStack, transfer, 2147483647, true, true, false);
                                    transferred = true;
                                }
                            }
                        }
                    }
                    if (transferred && !IC2.platform.isRendering()) {
                        entityplayer.openContainer.detectAndSendChanges();
                    }
                }

            } else if (mode == 3) {
                if (IC2.platform.isSimulating()) {
                    boolean transferred = false;
                    for (int i = 0; i < entityplayer.inventory.armorInventory.size(); i++) {
                        ItemStack stack = entityplayer.inventory.armorInventory.get(i);
                        if (stack != null && !(stack.getItem() instanceof ic2.core.item.ItemBattery)) {
                            double transfer = ElectricItem.manager.discharge(
                                    itemStack,
                                    2.0D * this.transferLimit,
                                    2147483647,
                                    true,
                                    true,
                                    true
                            );
                            if (!(transfer <= 0.0D)) {
                                transfer = ElectricItem.manager.charge(stack, transfer, 2147483647, true, false);
                                if (!(transfer <= 0.0D)) {
                                    ElectricItem.manager.discharge(itemStack, transfer, 2147483647, true, true, false);
                                    transferred = true;
                                }
                            }
                        }
                    }
                    if (transferred && !IC2.platform.isRendering()) {
                        entityplayer.openContainer.detectAndSendChanges();
                    }
                }

            } else if (mode == 4) {
                if (IC2.platform.isSimulating()) {
                    boolean transferred = false;
                    for (int i = 0; i < entityplayer.inventory.armorInventory.size(); i++) {
                        ItemStack stack = entityplayer.inventory.armorInventory.get(i);
                        if (!stack.isEmpty() && !(stack.getItem() instanceof ic2.core.item.ItemBattery)) {
                            double transfer = ElectricItem.manager.discharge(
                                    itemStack,
                                    2.0D * this.transferLimit,
                                    2147483647,
                                    true,
                                    true,
                                    true
                            );
                            if (!(transfer <= 0.0D)) {
                                transfer = ElectricItem.manager.charge(stack, transfer, 2147483647, true, false);
                                if (!(transfer <= 0.0D)) {
                                    ElectricItem.manager.discharge(itemStack, transfer, 2147483647, true, true, false);
                                    transferred = true;
                                }
                            }
                        }
                    }
                    if (transferred && !IC2.platform.isRendering()) {
                        entityplayer.openContainer.detectAndSendChanges();
                    }


                }
                if (IC2.platform.isSimulating()) {
                    boolean transferred = false;
                    for (int i = 0; i < entityplayer.inventory.mainInventory.size(); i++) {
                        ItemStack stack = entityplayer.inventory.mainInventory.get(i);
                        if (stack != null && !(stack.getItem() instanceof ic2.core.item.ItemBattery)) {
                            double transfer = ElectricItem.manager.discharge(
                                    itemStack,
                                    2.0D * this.transferLimit,
                                    2147483647,
                                    true,
                                    true,
                                    true
                            );
                            if (!(transfer <= 0.0D)) {
                                transfer = ElectricItem.manager.charge(stack, transfer, 2147483647, true, false);
                                if (!(transfer <= 0.0D)) {
                                    ElectricItem.manager.discharge(itemStack, transfer, 2147483647, true, true, false);
                                    transferred = true;
                                }
                            }
                        }
                    }
                    if (transferred && !IC2.platform.isRendering()) {
                        entityplayer.openContainer.detectAndSendChanges();
                    }
                }
            }

        }


    }

    public boolean canProvideEnergy(ItemStack stack) {
        return true;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (IC2.platform.isSimulating() && wirellescharge) {
            int mode = ModUtils.NBTGetInteger(player.getHeldItem(hand), "mode");
            mode++;
            if (mode > 4 || mode < 0) {
                mode = 0;
            }

            ModUtils.NBTSetInteger(player.getHeldItem(hand), "mode", mode);
            IC2.platform.messagePlayer(
                    player,
                    TextFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                            + Localization.translate("message.battery.mode." + mode)
            );
        }

        ItemStack stack = StackUtil.get(player, hand);
        if (!world.isRemote && StackUtil.getSize(stack) == 1) {
            if (ElectricItem.manager.getCharge(stack) > 0.0D) {
                boolean transferred = false;

                for (int i = 0; i < 9; ++i) {
                    ItemStack target = player.inventory.mainInventory.get(i);
                    if (target != null && target != stack && !(ElectricItem.manager.discharge(
                            target,
                            1.0D / 0.0,
                            2147483647,
                            true,
                            true,
                            true
                    ) > 0.0D)) {
                        double transfer = ElectricItem.manager.discharge(
                                stack,
                                2.0D * this.transferLimit,
                                2147483647,
                                true,
                                true,
                                true
                        );
                        if (!(transfer <= 0.0D)) {
                            transfer = ElectricItem.manager.charge(target, transfer, this.tier, true, false);
                            if (!(transfer <= 0.0D)) {
                                ElectricItem.manager.discharge(stack, transfer, 2147483647, true, true, false);
                                transferred = true;
                            }
                        }
                    }
                }

                if (transferred && !world.isRemote) {
                    player.openContainer.detectAndSendChanges();
                }
            }

            return new ActionResult(EnumActionResult.SUCCESS, stack);
        } else {
            return new ActionResult(EnumActionResult.PASS, stack);
        }
    }

}
