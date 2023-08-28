package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IEnergyItem;
import com.denfop.items.BaseEnergyItem;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
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
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemBattery extends BaseEnergyItem implements IModelRegister {

    private static final int maxLevel = 4;
    public final boolean wirelessCharge;
    private final String name1;

    public ItemBattery(String name, double maxCharge, double transferLimit, int tier, boolean wirelessCharge) {
        super(name, maxCharge, transferLimit, tier);
        this.wirelessCharge = wirelessCharge;
        this.name1 = name;
        IUCore.proxy.addIModelRegister(this);
    }

    public ItemBattery(String name, double maxCharge, double transferLimit, int tier) {
        super(name, maxCharge, transferLimit, tier);
        this.wirelessCharge = false;
        this.name1 = name;
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        final String loc = Constants.MOD_ID +
                ':' +
                "battery" + "/" + name + extraName;

        return new ModelResourceLocation(loc, null);
    }

    @Override
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {
        if (this.wirelessCharge) {
            int mode = ModUtils.NBTGetInteger(stack, "mode");
            if (mode > 4 || mode < 0) {
                mode = 0;
            }

            tooltip.add(
                    TextFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                            + Localization.translate("message.battery.mode." + mode)
            );
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("press.lshift"));
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                tooltip.add(Localization.translate("iu.changemode_key") + Localization.translate(
                        "iu.changemode_rcm1"));
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public int getItemEnchantability() {
        return 0;
    }

    public boolean isBookEnchantable(@Nonnull ItemStack stack, @Nonnull ItemStack book) {
        return false;
    }

    @Override
    public void getSubItems(@Nonnull final CreativeTabs p_150895_1_, @Nonnull final NonNullList<ItemStack> var3) {
        if (this.isInCreativeTab(p_150895_1_)) {
            final ItemStack var4 = new ItemStack(this, 1);
            ElectricItem.manager.charge(var4, 2.147483647E9, Integer.MAX_VALUE, true, false);
            var3.add(var4);
            var3.add(new ItemStack(this, 1));
        }
    }

    @Override
    public void registerModels() {
        registerModels(this.name1);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            double damage = ElectricItem.manager.getCharge(stack);
            double maxDamage = ElectricItem.manager.getMaxCharge(stack);
            int level = (int) (ItemBattery.maxLevel * damage / maxDamage);

            return getModelLocation1(name1, Integer.toString(level));
        });

        for (int level = 0; level <= maxLevel; ++level) {
            ModelBakery.registerItemVariants(this, getModelLocation1(name1, Integer.toString(level)));
        }

    }

    @Override
    public void onUpdate(
            @Nonnull ItemStack itemStack,
            @Nonnull World p_77663_2_,
            @Nonnull Entity p_77663_3_,
            int p_77663_4_,
            boolean p_77663_5_
    ) {
        if (!(p_77663_3_ instanceof EntityPlayer)) {
            return;
        }
        if (wirelessCharge) {
            int mode = ModUtils.NBTGetInteger(itemStack, "mode");
            EntityPlayer entityplayer = (EntityPlayer) p_77663_3_;
            if (mode == 1) {
                if (IUCore.proxy.isSimulating() && entityplayer.getEntityWorld().provider.getWorldTime() % 40 == 0) {
                    boolean transferred = false;
                    for (int i = 0; i < 9; i++) {
                        ItemStack stack = entityplayer.inventory.mainInventory.get(i);
                        if (!stack.isEmpty() && stack.getItem() instanceof IEnergyItem && !(stack.getItem() instanceof ItemBattery)) {
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
                    if (transferred && !IUCore.proxy.isRendering()) {
                        entityplayer.openContainer.detectAndSendChanges();
                    }
                }
            } else if (mode == 2) {
                if (IUCore.proxy.isSimulating() && entityplayer.getEntityWorld().provider.getWorldTime() % 40 == 0) {
                    boolean transferred = false;
                    for (int i = 0; i < entityplayer.inventory.mainInventory.size(); i++) {
                        ItemStack stack = entityplayer.inventory.mainInventory.get(i);
                        if (!stack.isEmpty() && stack.getItem() instanceof IEnergyItem && !(stack.getItem() instanceof ItemBattery)) {
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
                    if (transferred && !IUCore.proxy.isRendering()) {
                        entityplayer.openContainer.detectAndSendChanges();
                    }
                }

            } else if (mode == 3) {
                if (IUCore.proxy.isSimulating() && entityplayer.getEntityWorld().provider.getWorldTime() % 40 == 0) {
                    boolean transferred = false;
                    for (int i = 0; i < entityplayer.inventory.armorInventory.size(); i++) {
                        ItemStack stack = entityplayer.inventory.armorInventory.get(i);
                        if (!stack.isEmpty() && stack.getItem() instanceof IEnergyItem && !(stack.getItem() instanceof ItemBattery)) {
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
                    if (transferred && !IUCore.proxy.isRendering()) {
                        entityplayer.openContainer.detectAndSendChanges();
                    }
                }

            } else if (mode == 4) {
                if (IUCore.proxy.isSimulating() && entityplayer.getEntityWorld().provider.getWorldTime() % 40 == 0) {
                    boolean transferred = false;
                    for (int i = 0; i < entityplayer.inventory.armorInventory.size(); i++) {
                        ItemStack stack = entityplayer.inventory.armorInventory.get(i);
                        if (!stack.isEmpty() && stack.getItem() instanceof IEnergyItem && !(stack.getItem() instanceof ItemBattery)) {
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
                    if (transferred && !IUCore.proxy.isRendering()) {
                        entityplayer.openContainer.detectAndSendChanges();
                    }


                }
                if (IUCore.proxy.isSimulating() && entityplayer.getEntityWorld().provider.getWorldTime() % 40 == 0) {
                    boolean transferred = false;
                    for (int i = 0; i < entityplayer.inventory.mainInventory.size(); i++) {
                        ItemStack stack = entityplayer.inventory.mainInventory.get(i);
                        if (!stack.isEmpty() && stack.getItem() instanceof IEnergyItem && !(stack.getItem() instanceof ItemBattery)) {
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
                    if (transferred && !IUCore.proxy.isRendering()) {
                        entityplayer.openContainer.detectAndSendChanges();
                    }
                }
            }

        }


    }

    public boolean canProvideEnergy(ItemStack stack) {
        return true;
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        if (IUCore.proxy.isSimulating() && wirelessCharge) {
            int mode = ModUtils.NBTGetInteger(player.getHeldItem(hand), "mode");
            mode++;
            if (mode > 4 || mode < 0) {
                mode = 0;
            }

            ModUtils.NBTSetInteger(player.getHeldItem(hand), "mode", mode);
            IUCore.proxy.messagePlayer(
                    player,
                    TextFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                            + Localization.translate("message.battery.mode." + mode)
            );
        }

        ItemStack stack = ModUtils.get(player, hand);
        if (!world.isRemote && ModUtils.getSize(stack) == 1) {
            if (ElectricItem.manager.getCharge(stack) > 0.0D) {
                boolean transferred = false;

                for (int i = 0; i < 9; ++i) {
                    ItemStack target = player.inventory.mainInventory.get(i);
                    if (!target.isEmpty() && target.getItem() instanceof IEnergyItem && target != stack && !(ElectricItem.manager.discharge(
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

                if (transferred) {
                    player.openContainer.detectAndSendChanges();
                }
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        }
    }

}
