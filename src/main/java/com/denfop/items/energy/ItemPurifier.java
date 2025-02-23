package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.audio.EnumSound;
import com.denfop.componets.AbstractComponent;
import com.denfop.items.BaseEnergyItem;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.base.TileMultiMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemPurifier extends BaseEnergyItem implements IModelRegister, IUpgradeItem {


    public ItemPurifier(String name, double maxCharge, double transferLimit, int tier) {
        super(name, maxCharge, transferLimit, tier);
        setMaxStackSize(1);

        IUCore.proxy.addIModelRegister(this);
        UpgradeSystem.system.addRecipe(this, EnumUpgrades.PURIFIER.list);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "energy" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return EnumUpgrades.PURIFIER.list;
    }

    public int getItemEnchantability() {
        return 0;
    }

    public boolean isBookEnchantable(@Nonnull ItemStack stack, @Nonnull ItemStack book) {
        return false;
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
        registerModel(this, meta, name);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name, String extraName) {
        registerModel(this, meta, name);
    }

    public boolean canProvideEnergy(ItemStack stack) {
        return true;
    }

    public void onUpdate(
            @Nonnull ItemStack itemStack,
            @Nonnull World p_77663_2_,
            @Nonnull Entity p_77663_3_,
            int p_77663_4_,
            boolean p_77663_5_
    ) {
        NBTTagCompound nbt = ModUtils.nbt(itemStack);

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(p_77663_2_, this, itemStack));
        }
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUseFirst(
            final EntityPlayer player,
            final World world,
            @Nonnull final BlockPos pos,
            @Nonnull final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ,
            @Nonnull final EnumHand hand
    ) {

        TileEntity tile = world.getTileEntity(pos);
        ItemStack itemstack = player.getHeldItem(hand);
        if (!(tile instanceof TileEntityInventory) && !(tile instanceof IManufacturerBlock)) {
            return EnumActionResult.PASS;
        }
        double coef = 1D - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, player.getHeldItem(hand)) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, player.getHeldItem(hand)).number * 0.25D : 0);

        if (tile instanceof TileEntityBlock) {
            TileEntityBlock base = (TileEntityBlock) tile;
            double energy = 10000;
            if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PURIFIER, itemstack)) {
                energy = 0;
            }
            if (!base.canEntityDestroy(player)) {
                return EnumActionResult.FAIL;
            }
            for (AbstractComponent component : base.getComponentList()) {
                if (component.canUsePurifier(player) && ElectricItem.manager.canUse(itemstack, energy * coef)) {
                    component.workPurifier();
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        if (tile instanceof TileMultiMachine) {
            if (!ElectricItem.manager.canUse(itemstack, 500 * coef)) {
                return EnumActionResult.PASS;
            }
            if (!player.isSneaking()) {
                TileMultiMachine base = (TileMultiMachine) tile;
                ItemStack stack_quickly = ItemStack.EMPTY;
                ItemStack stack_modulesize = ItemStack.EMPTY;
                ItemStack stack_modulestorage = ItemStack.EMPTY;
                ItemStack panel = ItemStack.EMPTY;
                ItemStack module_infinity_water = ItemStack.EMPTY;
                ItemStack module_separate = ItemStack.EMPTY;
                if (base.multi_process.quickly) {
                    stack_quickly = new ItemStack(IUItem.module_quickly);
                }
                if (base.multi_process.modulesize) {
                    stack_modulesize = new ItemStack(IUItem.module_stack);
                }
                if (base.multi_process.modulestorage) {
                    stack_modulestorage = new ItemStack(IUItem.module_storage);
                }
                if (base.multi_process.modulestorage) {
                    module_infinity_water = new ItemStack(IUItem.module_infinity_water);
                }
                if (base.multi_process.module_separate) {
                    module_separate = new ItemStack(IUItem.module_separate);
                }
                if (base.solartype != null) {
                    panel = new ItemStack(IUItem.module6, 1, base.solartype.meta);
                }
                if (!stack_quickly.isEmpty() || !stack_modulesize.isEmpty() || !panel.isEmpty() || !module_infinity_water.isEmpty() || !module_separate.isEmpty()) {
                    final EntityItem item = new EntityItem(world);
                    if (!stack_quickly.isEmpty()) {
                        item.setItem(stack_quickly);
                        base.multi_process.shrinkModule(1);
                        base.multi_process.setQuickly(false);
                    } else if (!stack_modulesize.isEmpty()) {
                        item.setItem(stack_modulesize);
                        base.multi_process.setModulesize(false);
                        base.multi_process.shrinkModule(1);
                    } else if (!module_infinity_water.isEmpty()) {
                        item.setItem(module_infinity_water);
                        base.multi_process.module_infinity_water = false;
                        base.multi_process.shrinkModule(1);
                    } else if (!module_separate.isEmpty()) {
                        item.setItem(module_separate);
                        base.multi_process.module_separate = false;
                        base.multi_process.shrinkModule(1);
                    } else if (!panel.isEmpty()) {
                        item.setItem(panel);
                        base.solartype = null;
                    } else if (!stack_modulestorage.isEmpty()) {
                        item.setItem(stack_modulestorage);
                        base.multi_process.setModulestorage(false);
                        base.multi_process.shrinkModule(1);
                    }
                    if (!player.getEntityWorld().isRemote) {
                        item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                        item.setPickupDelay(0);
                        world.spawnEntity(item);
                        ElectricItem.manager.use(itemstack, 500 * coef, player);
                        if (IUCore.proxy.isRendering()) {
                            player.playSound(EnumSound.purifier.getSoundEvent(), 1F, 1);

                        }
                        return EnumActionResult.SUCCESS;
                    }
                }
            } else {
                TileMultiMachine base = (TileMultiMachine) tile;
                List<ItemStack> stack_list = new ArrayList<>();
                if (base.multi_process.quickly) {
                    stack_list.add(new ItemStack(IUItem.module_quickly));
                    base.multi_process.setQuickly(false);
                    base.multi_process.shrinkModule(1);
                }
                if (base.multi_process.modulesize) {
                    stack_list.add(new ItemStack(IUItem.module_stack));
                    base.multi_process.setModulesize(false);
                    base.multi_process.shrinkModule(1);
                }
                if (base.solartype != null) {
                    stack_list.add(new ItemStack(IUItem.module6, 1, base.solartype.meta));
                    base.solartype = null;
                }
                if (base.multi_process.modulestorage) {
                    stack_list.add(new ItemStack(IUItem.module_storage));
                    base.multi_process.setModulestorage(false);
                    base.multi_process.shrinkModule(1);

                }
                if (base.multi_process.module_infinity_water) {
                    stack_list.add(new ItemStack(IUItem.module_infinity_water));
                    base.multi_process.module_infinity_water = false;
                    base.multi_process.shrinkModule(1);

                }
                if (base.multi_process.module_separate) {
                    stack_list.add(new ItemStack(IUItem.module_separate));
                    base.multi_process.module_separate = false;
                    base.multi_process.shrinkModule(1);

                }
                for (ItemStack stack : stack_list) {
                    final EntityItem item = new EntityItem(world);
                    if (!player.getEntityWorld().isRemote) {
                        item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                        item.setPickupDelay(0);
                        item.setItem(stack);
                        world.spawnEntity(item);
                        if (IUCore.proxy.isRendering()) {
                            player.playSound(EnumSound.purifier.getSoundEvent(), 1F, 1);

                        }

                    }
                }
                ElectricItem.manager.use(itemstack, 500 * coef, player);
                return EnumActionResult.SUCCESS;
            }
        } else if (tile instanceof IManufacturerBlock) {
            IManufacturerBlock base = (IManufacturerBlock) tile;
            if (player.isSneaking()) {
                int level = base.getLevel();
                if (level == 0) {
                    return EnumActionResult.PASS;
                }
                final ItemStack stack = new ItemStack(IUItem.upgrade_speed_creation, level);
                base.setLevel(0);
                final EntityItem item = new EntityItem(world);
                item.setItem(stack);
                if (!player.getEntityWorld().isRemote) {
                    item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                    item.setPickupDelay(0);
                    world.spawnEntity(item);
                    ElectricItem.manager.use(itemstack, 500, player);
                    if (IUCore.proxy.isRendering()) {
                        player.playSound(EnumSound.purifier.getSoundEvent(), 1F, 1);

                    }
                    return EnumActionResult.SUCCESS;
                }
            } else {
                int level = base.getLevel();
                if (level == 0) {
                    return EnumActionResult.PASS;
                }
                final ItemStack stack = new ItemStack(IUItem.upgrade_speed_creation, 1);
                base.removeLevel(1);
                final EntityItem item = new EntityItem(world);
                item.setItem(stack);
                if (!player.getEntityWorld().isRemote) {
                    item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                    item.setPickupDelay(0);
                    world.spawnEntity(item);
                    ElectricItem.manager.use(itemstack, 500, player);
                    if (IUCore.proxy.isRendering()) {
                        player.playSound(EnumSound.purifier.getSoundEvent(), 1F, 1);

                    }
                    return EnumActionResult.SUCCESS;
                }

            }

        }
        return EnumActionResult.PASS;
    }

}
