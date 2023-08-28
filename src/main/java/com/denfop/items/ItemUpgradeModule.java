package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.upgrades.IUpgradeItem;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.api.upgrades.UpgradeRegistry;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStackEvent;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ItemUpgradeModule extends ItemSubTypes<ItemUpgradeModule.Types> implements IModelRegister,
        IUpgradeItem, IItemStackInventory, IUpdatableItemStackEvent {

    protected static final String NAME = "upgrades";
    private static final DecimalFormat decimalformat = new DecimalFormat("0.##");


    public ItemUpgradeModule() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.UpgradeTab);
        IUItem.overclockerUpgrade_1 = UpgradeRegistry.register(new ItemStack(this, 1, Type.Overclocker1.ordinal()));
        IUItem.overclockerUpgrade1 = UpgradeRegistry.register(new ItemStack(this, 1, Type.Overclocker2.ordinal()));
        IUItem.tranformerUpgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.transformer.ordinal()));
        IUItem.tranformerUpgrade1 = UpgradeRegistry.register(new ItemStack(this, 1, Type.transformer1.ordinal()));
        IUItem.lap_energystorage_upgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.storage.ordinal()));
        IUItem.adv_lap_energystorage_upgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.adv_storage.ordinal()));
        IUItem.imp_lap_energystorage_upgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.imp_storage.ordinal()));
        IUItem.per_lap_energystorage_upgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.per_storage.ordinal()));
        IUItem.fluidpullingUpgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.fluid_pulling.ordinal()));
        IUItem.overclockerUpgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.overclocker.ordinal()));
        IUItem.transformerUpgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.transformer_simple.ordinal()));
        IUItem.energyStorageUpgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.energy_storage.ordinal()));
        IUItem.ejectorUpgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.ejector.ordinal()));
        IUItem.fluidEjectorUpgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.fluid_ejector.ordinal()));
        IUItem.pullingUpgrade = UpgradeRegistry.register(new ItemStack(this, 1, Type.pulling.ordinal()));

        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public static Type getType(int meta) {
        if (meta < 0 || meta >= Type.Values.length) {
            return null;
        }
        return Type.Values[meta];
    }

    private static EnumFacing getDirection(final ItemStack stack) {
        final int rawDir = ModUtils.nbt(stack).getByte("dir");
        if (rawDir < 1 || rawDir > 6) {
            return null;
        }
        return EnumFacing.VALUES[rawDir - 1];
    }

    private static String getSideName(final ItemStack stack) {
        final EnumFacing dir = getDirection(stack);
        if (dir == null) {
            return "iu.tooltip.upgrade.ejector.anyside";
        }
        switch (dir) {
            case WEST: {
                return "iu.dir.west";
            }
            case EAST: {
                return "iu.dir.east";
            }
            case DOWN: {
                return "iu.dir.bottom";
            }
            case UP: {
                return "iu.dir.top";
            }
            case NORTH: {
                return "iu.dir.north";
            }
            case SOUTH: {
                return "iu.dir.south";
            }
            default: {
                throw new RuntimeException("invalid dir: " + dir);
            }
        }
    }

    @Override
    public boolean isSuitableFor(final ItemStack stack, final Set<UpgradableProperty> types) {
        Type type = getType(stack.getItemDamage());
        if (type == null) {
            return false;
        }
        switch (type) {
            case overclocker:
            case Overclocker1:
            case Overclocker2:
                return (types.contains(UpgradableProperty.Processing));
            case transformer:
            case transformer1:
            case transformer_simple:
                return types.contains(UpgradableProperty.Transformer);
            case storage:
            case adv_storage:
            case imp_storage:
            case per_storage:
            case energy_storage:
                return types.contains(UpgradableProperty.EnergyStorage);
            case ejector:
                return types.contains(UpgradableProperty.ItemConsuming);
            case pulling:
                return types.contains(UpgradableProperty.ItemProducing);
            case fluid_ejector:
                return types.contains(UpgradableProperty.FluidConsuming);
            case fluid_pulling:
                return types.contains(UpgradableProperty.FluidProducing);

        }
        return false;
    }

    @Override
    public int getExtraTier(final ItemStack itemStack) {
        ItemUpgradeModule.Type type = getType(itemStack.getItemDamage());
        if (type == null) {
            return 0;
        } else {
            switch (type) {
                case transformer_simple:
                    return 1;
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
    public double getProcessTimeMultiplier(final ItemStack itemStack) {
        Type type = getType(itemStack.getItemDamage());
        if (type == null) {
            return 1.0D;
        }
        switch (type) {
            case overclocker:
                return 0.6D;
            case Overclocker1:
                return 0.5D;
            case Overclocker2:
                return 0.4D;
        }
        return 1.0D;
    }

    public double getExtraEnergyStorage(ItemStack stack) {
        Types type = this.getType(stack);
        if (type == null) {
            return 0;
        } else {
            switch (type) {
                case energy_storage:
                    return 10000;
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
    public double getEnergyDemandMultiplier(final ItemStack itemStack) {
        Type type = getType(itemStack.getItemDamage());
        if (type == null) {
            return 1.0D;
        }
        switch (type) {
            case overclocker:
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
            case overclocker:
            case Overclocker1:
            case Overclocker2:
                list.add(Localization.translate(
                        "iu.tooltip.upgrade.overclocker.time",
                        decimalformat.format(100.0D
                                * Math.pow(getProcessTimeMultiplier(stack), stack.getCount()))
                ));
                list.add(Localization.translate(
                        "iu.tooltip.upgrade.overclocker.power",
                        decimalformat.format(100.0D
                                * Math.pow(getEnergyDemandMultiplier(stack), stack.getCount()))
                ));
                break;
            case ejector:
            case fluid_ejector: {
                final String side = getSideName(stack);
                list.add(Localization.translate("iu.tooltip.upgrade.ejector", Localization.translate(side)));
                break;
            }
            case pulling:
            case fluid_pulling: {
                final String side = getSideName(stack);
                list.add(Localization.translate("iu.tooltip.upgrade.pulling", Localization.translate(side)));
                break;
            }
            case transformer_simple:
            case transformer:
            case transformer1:
                list.add(Localization.translate(
                        "iu.tooltip.upgrade.transformer",
                        this.getExtraTier(stack) * stack.getCount()
                ));
                break;
            case storage:
            case adv_storage:
            case imp_storage:
            case per_storage:
            case energy_storage:
                list.add(Localization.translate(
                        "iu.tooltip.upgrade.storage",
                        this.getExtraEnergyStorage(stack) * ModUtils.getSize(stack)
                ));
                break;

        }
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, final int meta, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    @Override
    public IAdvInventory getInventory(final EntityPlayer var1, final ItemStack var2) {
        if (var2.getItemDamage() < 11) {
            return null;
        } else {
            return new ItemStackUpgradeModules(var1, var2);
        }
    }

    public void save(ItemStack stack, EntityPlayer player) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setBoolean("open", true);
        nbt.setInteger("slot_inventory", player.inventory.currentItem);
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = ModUtils.get(player, hand);
        if (IUCore.proxy.isSimulating() && stack.getItemDamage() >= 11) {
            save(stack, player);
            player.openGui(IUCore.instance, 1, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));

        }
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer buffer, ItemStack stack) {

    }

    @Override
    public void updateEvent(int event, ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        byte event1 = (byte) event;
        nbt.setByte("dir", event1);
    }

    private enum Type {
        Overclocker1,
        Overclocker2,
        transformer,
        transformer1,

        storage,

        adv_storage,

        imp_storage,

        per_storage,
        overclocker,
        transformer_simple,
        energy_storage,
        ejector,
        pulling,
        fluid_ejector,
        fluid_pulling;

        public static final Type[] Values = values();

    }

    public enum Types implements ISubEnum {
        overclockerUpgrade1(0),
        overclockerUpgrade2(1),
        transformerUpgrade1(2),
        transformerUpgrade2(3),

        storageUpgrade(4),
        adv_storageUpgrade(5),
        imp_storageUpgrade(6),
        per_storageUpgrade(7),
        overclocker(8),
        transformer(9),
        energy_storage(10),
        ejector(11),
        pulling(12),
        fluid_ejector(13),
        fluid_pulling(14);

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
