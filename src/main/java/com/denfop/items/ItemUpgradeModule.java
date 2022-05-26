package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import ic2.api.item.IItemHudInfo;
import ic2.api.upgrade.IProcessingUpgrade;
import ic2.api.upgrade.ITransformerUpgrade;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.api.upgrade.UpgradeRegistry;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ItemUpgradeModule extends ItemMulti<ItemUpgradeModule.Types> implements IModelRegister, IProcessingUpgrade,
        IUpgradeItem,
        ITransformerUpgrade, IItemHudInfo {

    protected static final String NAME = "upgrades";
    private static final List<StackUtil.AdjacentInv> emptyInvList = Collections.emptyList();
    private static final DecimalFormat decimalformat = new DecimalFormat("0.##");
    private final List<String> itemNames;


    public ItemUpgradeModule() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.ItemTab);
        IUItem.overclockerUpgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.Overclocker1.ordinal()));
        IUItem.overclockerUpgrade1 = UpgradeRegistry.register(new ItemStack(this, 1, Type.Overclocker2.ordinal()));
        IUItem.tranformerUpgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.transformer.ordinal()));
        IUItem.tranformerUpgrade1 = UpgradeRegistry.register(new ItemStack(this, 1, Type.transformer1.ordinal()));
        this.itemNames = new ArrayList<>();
        this.addItemsNames();
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    private static List<StackUtil.AdjacentInv> getTargetInventories(ItemStack stack, TileEntity parent) {
        EnumFacing dir = getDirection(stack);
        if (dir == null) {
            return StackUtil.getAdjacentInventories(parent);
        } else {
            StackUtil.AdjacentInv inv = StackUtil.getAdjacentInventory(parent, dir);
            return inv == null ? emptyInvList : Collections.singletonList(inv);
        }
    }

    private static EnumFacing getDirection(ItemStack stack) {
        int rawDir = StackUtil.getOrCreateNbtData(stack).getByte("dir");
        return rawDir >= 1 && rawDir <= 6 ? EnumFacing.VALUES[rawDir - 1] : null;
    }

    public static Type getType(int meta) {
        if (meta < 0 || meta >= Type.Values.length) {
            return null;
        }
        return Type.Values[meta];
    }

    public void addItemsNames() {
        this.itemNames.add("overclockerUpgrade1");
        this.itemNames.add("overclockerUpgrade2");
        this.itemNames.add("transformerUpgrade1");
        this.itemNames.add("transformerUpgrade2");
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

        }
        return false;
    }

    @Override
    public boolean onTick(final ItemStack stack, final IUpgradableBlock types) {

        return false;


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
            final List<String> list,
            final ITooltipFlag flagIn
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
                                * Math.pow(getProcessTimeMultiplier(stack, null), stack.stackSize))
                ));
                list.add(Localization.translate(
                        "ic2.tooltip.upgrade.overclocker.power",
                        decimalformat.format(100.0D
                                * Math.pow(getEnergyDemandMultiplier(stack, null), stack.stackSize))
                ));
                break;
            case transformer:
            case transformer1:
                list.add(Localization.translate(
                        "ic2.tooltip.upgrade.transformer",
                        this.getExtraTier(stack, null) * stack.stackSize
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
        transformer1;

        public static final Type[] Values = values();

    }

    public enum Types implements IIdProvider {
        overclockerUpgrade1(0),
        overclockerUpgrade2(1),
        transformerUpgrade1(2),
        transformerUpgrade2(3),

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
