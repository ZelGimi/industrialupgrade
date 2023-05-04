package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.IIdProvider;
import ic2.api.item.IItemHudInfo;
import ic2.api.upgrade.*;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.*;

public class ItemUpgradeModule extends ItemMulti<ItemUpgradeModule.Types> implements IModelRegister, IProcessingUpgrade,
        IUpgradeItem, IEnergyStorageUpgrade,
        ITransformerUpgrade, IItemHudInfo {

    protected static final String NAME = "upgrades";
    private static final DecimalFormat decimalformat = new DecimalFormat("0.##");

    public ItemUpgradeModule() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.UpgradeTab);
        IUItem.overclockerUpgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.Overclocker1.ordinal()));
        IUItem.overclockerUpgrade1 = UpgradeRegistry.register(new ItemStack(this, 1, Type.Overclocker2.ordinal()));
        IUItem.tranformerUpgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.transformer.ordinal()));
        IUItem.tranformerUpgrade1 = UpgradeRegistry.register(new ItemStack(this, 1, Type.transformer1.ordinal()));
        IUItem.lap_energystorage_upgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.storage.ordinal()));
        IUItem.adv_lap_energystorage_upgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.adv_storage.ordinal()));
        IUItem.imp_lap_energystorage_upgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.imp_storage.ordinal()));
        IUItem.per_lap_energystorage_upgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.per_storage.ordinal()));
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public static Type getType(int meta) {
        if (meta < 0 || meta >= Type.Values.length) {
            return null;
        }
        return Type.Values[meta];
    }


    @Override
    public List<String> getHudInfo(final ItemStack itemStack, final boolean b) {
        List<String> info = new LinkedList<>();
        info.add("Machine Upgrade");
        return info;
    }

    @Override
    public boolean isSuitableFor(final ItemStack stack, final Set<UpgradableProperty> types) {
        Type type = getType(stack.getItemDamage());
        if (type == null) {
            return false;
        }
        switch (type) {

            case Overclocker1:
            case Overclocker2:
                return (types.contains(UpgradableProperty.Processing) || types.contains(UpgradableProperty.Augmentable));
            case transformer:
            case transformer1:
                return types.contains(UpgradableProperty.Transformer);
            case storage:
            case adv_storage:
            case imp_storage:
            case per_storage:
                return types.contains(UpgradableProperty.EnergyStorage);

        }
        return false;
    }

    @Override
    public boolean onTick(final ItemStack stack, final IUpgradableBlock types) {

        return true;


    }

    @Override
    public Collection<ItemStack> onProcessEnd(
            final ItemStack stack,
            final IUpgradableBlock iUpgradableBlock,
            final Collection<ItemStack> output
    ) {
        return output;
    }

    @Override
    public int getExtraTier(final ItemStack itemStack, final IUpgradableBlock iUpgradableBlock) {
        ItemUpgradeModule.Type type = getType(itemStack.getItemDamage());
        if (type == null) {
            return 0;
        } else {
            switch (type) {
                case transformer:
                    return 2;
                case transformer1:
                    return 4;
                default:
                    return 0;
            }
        }
    }

    @Override
    public int getExtraProcessTime(final ItemStack itemStack, final IUpgradableBlock iUpgradableBlock) {
        return 0;
    }

    @Override
    public double getProcessTimeMultiplier(final ItemStack itemStack, final IUpgradableBlock iUpgradableBlock) {
        Type type = getType(itemStack.getItemDamage());
        if (type == null) {
            return 1.0D;
        }
        switch (type) {
            case Overclocker1:
                return 0.5D;
            case Overclocker2:
                return 0.4D;
        }
        return 1.0D;
    }

    public int getExtraEnergyStorage(ItemStack stack, IUpgradableBlock parent) {
        Types type = this.getType(stack);
        if (type == null) {
            return 0;
        } else {
            switch (type) {
                case storageUpgrade:
                    return 100000;
                case adv_storageUpgrade:
                    return 1000000;
                case imp_storageUpgrade:
                    return 10000000;
                case per_storageUpgrade:
                    return 100000000;
                default:
                    return 0;
            }
        }
    }

    @Override
    public double getEnergyStorageMultiplier(final ItemStack itemStack, final IUpgradableBlock iUpgradableBlock) {
        return 1;
    }

    @Override
    public int getExtraEnergyDemand(final ItemStack itemStack, final IUpgradableBlock iUpgradableBlock) {
        return 0;
    }

    @Override
    public double getEnergyDemandMultiplier(final ItemStack itemStack, final IUpgradableBlock iUpgradableBlock) {
        Type type = getType(itemStack.getItemDamage());
        if (type == null) {
            return 1.0D;
        }
        switch (type) {
            case Overclocker1:
                return 1.3D;
            case Overclocker2:
                return 1.2D;
        }
        return 1.0D;
    }

    @Override
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            @Nonnull final List<String> list,
            @Nonnull final ITooltipFlag flagIn
    ) {
        Type type = getType(stack.getItemDamage());
        if (type == null) {
            return;
        }
        super.addInformation(stack, worldIn, list, flagIn);
        switch (type) {
            case Overclocker1:
            case Overclocker2:
                list.add(Localization.translate(
                        "ic2.tooltip.upgrade.overclocker.time",
                        decimalformat.format(100.0D
                                * Math.pow(getProcessTimeMultiplier(stack, null), stack.getCount()))
                ));
                list.add(Localization.translate(
                        "ic2.tooltip.upgrade.overclocker.power",
                        decimalformat.format(100.0D
                                * Math.pow(getEnergyDemandMultiplier(stack, null), stack.getCount()))
                ));
                break;
            case transformer:
            case transformer1:
                list.add(Localization.translate(
                        "ic2.tooltip.upgrade.transformer",
                        this.getExtraTier(stack, null) * stack.getCount()
                ));
                break;
            case storage:
            case adv_storage:
            case imp_storage:
            case per_storage:
                list.add(Localization.translate(
                        "ic2.tooltip.upgrade.storage",
                        this.getExtraEnergyStorage(stack, null) * StackUtil.getSize(stack)
                ));
                break;

        }
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    private enum Type {
        Overclocker1,
        Overclocker2,
        transformer,
        transformer1,

        storage,

        adv_storage,

        imp_storage,

        per_storage;

        public static final Type[] Values = values();

    }

    public enum Types implements IIdProvider {
        overclockerUpgrade1(0),
        overclockerUpgrade2(1),
        transformerUpgrade1(2),
        transformerUpgrade2(3),

        storageUpgrade(4),
        adv_storageUpgrade(5),
        imp_storageUpgrade(6),
        per_storageUpgrade(7),

        ;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.ID;
        }
    }

}
