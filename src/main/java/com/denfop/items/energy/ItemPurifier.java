package com.denfop.items.energy;

import com.denfop.ElectricItem;
import com.denfop.IUItem;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
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
                dropModules(world, player, base);
            } else {
                dropAllModules(world, player, base);
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

    private void dropModules(Level world, Player player, TileMultiMachine base) {
        dropItem(world, player, base.multi_process.quickly ? new ItemStack(IUItem.module_quickly.getItem()) : ItemStack.EMPTY);
        dropItem(world, player, base.multi_process.modulesize ? new ItemStack(IUItem.module_stack.getItem()) : ItemStack.EMPTY);
        dropItem(world, player, base.solartype != null ? new ItemStack(IUItem.module6.getStack(base.solartype.meta), 1) : ItemStack.EMPTY);
        dropItem(world, player, base.multi_process.modulestorage ? new ItemStack(IUItem.module_storage.getItem()) : ItemStack.EMPTY);
        dropItem(world, player, base.multi_process.module_infinity_water ? new ItemStack(IUItem.module_infinity_water.getItem()) : ItemStack.EMPTY);
        dropItem(world, player, base.multi_process.module_separate ? new ItemStack(IUItem.module_separate.getItem()) : ItemStack.EMPTY);
    }

    private void dropAllModules(Level world, Player player, TileMultiMachine base) {
        dropModules(world, player, base);
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
