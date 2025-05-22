package com.denfop.items.energy;

import com.denfop.ElectricItem;
import com.denfop.IUItem;
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
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.denfop.IUCore.runnableListAfterRegisterItem;

public class ItemPurifier extends BaseEnergyItem implements IUpgradeItem {
    public ItemPurifier(double maxCharge, double transferLimit, int tier) {
        super(maxCharge, transferLimit, tier);

        runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.PURIFIER.list));
    }
    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", Registry.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "item."+pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }
    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return EnumUpgrades.PURIFIER.list;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level p_77663_2_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        super.inventoryTick(itemStack, p_77663_2_, p_41406_, p_41407_, p_41408_);
        CompoundTag nbt = ModUtils.nbt(itemStack);

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.putBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(p_77663_2_, this, itemStack));
        }
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        Direction side = context.getClickedFace();
        ItemStack itemstack = context.getItemInHand();

        if (player == null) {
            return InteractionResult.PASS;
        }

        BlockEntity tile = world.getBlockEntity(pos);
        if (!(tile instanceof TileEntityInventory) && !(tile instanceof IManufacturerBlock)) {
            return InteractionResult.PASS;
        }

        double coef = 1.0 - (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, itemstack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, itemstack).number * 0.25 : 0);

        if (tile instanceof TileEntityBlock base) {
            double energy = 10000;
            if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PURIFIER, itemstack)) {
                energy = 0;
            }
            if (!base.canEntityDestroy(player)) {
                return InteractionResult.FAIL;
            }
            for (AbstractComponent component : base.getComponentList()) {
                if (component.canUsePurifier(player) && ElectricItem.manager.canUse(itemstack, energy * coef)) {
                    component.workPurifier();
                    return InteractionResult.SUCCESS;
                }
            }
        }

        if (tile instanceof TileMultiMachine base) {
            if (!ElectricItem.manager.canUse(itemstack, 500 * coef)) {
                return InteractionResult.PASS;
            }

            if (!player.isCrouching()) {
                ItemStack stack_quickly = ItemStack.EMPTY;
                ItemStack stack_modulesize = ItemStack.EMPTY;
                ItemStack stack_modulestorage = ItemStack.EMPTY;
                ItemStack panel = ItemStack.EMPTY;
                ItemStack module_infinity_water = ItemStack.EMPTY;
                ItemStack module_separate = ItemStack.EMPTY;
                if (base.multi_process.quickly) {
                    stack_quickly = new ItemStack(IUItem.module_quickly.getItem());
                }
                if (base.multi_process.modulesize) {
                    stack_modulesize = new ItemStack(IUItem.module_stack.getItem());
                }
                if (base.multi_process.modulestorage) {
                    stack_modulestorage = new ItemStack(IUItem.module_storage.getItem());
                }
                if (base.multi_process.modulestorage) {
                    module_infinity_water = new ItemStack(IUItem.module_infinity_water.getItem());
                }
                if (base.multi_process.module_separate) {
                    module_separate = new ItemStack(IUItem.module_separate.getItem());
                }
                if (base.solartype != null) {
                    panel = new ItemStack(IUItem.module6.getStack( base.solartype.meta), 1);
                }
                if (!stack_quickly.isEmpty() || !stack_modulesize.isEmpty() || !panel.isEmpty() || !module_infinity_water.isEmpty() || !module_separate.isEmpty()) {
                    ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), ItemStack.EMPTY);

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
                    if (!player.getInventory().isEmpty()) {
                        item.setPickUpDelay(0);
                        world.addFreshEntity(item);
                        ElectricItem.manager.use(itemstack, 500 * coef, player);
                        if (player.getLevel().isClientSide) {
                            player.playSound(EnumSound.purifier.getSoundEvent(), 1F, 1);

                        }
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                List<ItemStack> stack_list = new ArrayList<>();
                if (base.multi_process.quickly) {
                    stack_list.add(new ItemStack(IUItem.module_quickly.getItem()));
                    base.multi_process.setQuickly(false);
                    base.multi_process.shrinkModule(1);
                }
                if (base.multi_process.modulesize) {
                    stack_list.add(new ItemStack(IUItem.module_stack.getItem()));
                    base.multi_process.setModulesize(false);
                    base.multi_process.shrinkModule(1);
                }
                if (base.solartype != null) {
                    stack_list.add(new ItemStack(IUItem.module6.getStack( base.solartype.meta), 1));
                    base.solartype = null;
                }
                if (base.multi_process.modulestorage) {
                    stack_list.add(new ItemStack(IUItem.module_storage.getItem()));
                    base.multi_process.setModulestorage(false);
                    base.multi_process.shrinkModule(1);

                }
                if (base.multi_process.module_infinity_water) {
                    stack_list.add(new ItemStack(IUItem.module_infinity_water.getItem()));
                    base.multi_process.module_infinity_water = false;
                    base.multi_process.shrinkModule(1);

                }
                if (base.multi_process.module_separate) {
                    stack_list.add(new ItemStack(IUItem.module_separate.getItem()));
                    base.multi_process.module_separate = false;
                    base.multi_process.shrinkModule(1);

                }
                for (ItemStack stack : stack_list) {
                    ItemEntity item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(),stack);
                    if (!player.getLevel().isClientSide) {
                        item.setPickUpDelay(0);
                        world.addFreshEntity(item);


                    }
                    if (player.getLevel().isClientSide) {
                        player.playSound(EnumSound.purifier.getSoundEvent(), 1F, 1);

                    }
                }
                ElectricItem.manager.use(itemstack, 500 * coef, player);
                return InteractionResult.SUCCESS;
            }
            ElectricItem.manager.use(itemstack, 500 * coef, player);
            return InteractionResult.SUCCESS;
        }

        if (tile instanceof IManufacturerBlock base) {
            if (player.isCrouching()) {
                dropUpgrade(world, player, base);
            } else {
                dropSingleUpgrade(world, player, base);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }




    private void dropUpgrade(Level world, Player player, IManufacturerBlock base) {
        int level = base.getLevelMechanism();
        if (level > 0) {
            dropItem(world, player, new ItemStack(IUItem.upgrade_speed_creation.getItem(), level));
            base.setLevelMech(0);
        }
    }

    private void dropSingleUpgrade(Level world, Player player, IManufacturerBlock base) {
        if (base.getLevelMechanism() > 0) {
            dropItem(world, player, new ItemStack(IUItem.upgrade_speed_creation.getItem(), 1));
            base.removeLevel(1);
        }
    }

    private void dropItem(Level world, Player player, ItemStack stack) {
        if (!stack.isEmpty() && !world.isClientSide) {
            ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), stack);
            itemEntity.setPickUpDelay(0);
            world.addFreshEntity(itemEntity);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
}
