package com.denfop.items.modules;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.componets.AdvEnergy;
import com.denfop.tiles.base.TileEntityElectricBlock;
import com.denfop.tiles.mechanism.TileEntityAnalyzerChest;
import com.denfop.utils.ModUtils;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.Energy;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ItemAdditionModule extends ItemMulti<ItemAdditionModule.CraftingTypes> implements IModelRegister {

    protected static final String NAME = "additionmodule";

    public ItemAdditionModule() {
        super(null, CraftingTypes.class);
        this.setCreativeTab(IUCore.ModuleTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":additionmodule/" + CraftingTypes.getFromID(meta).getName(), null)
        );
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUseFirst(
            final EntityPlayer player,
            @Nonnull final World world,
            @Nonnull final BlockPos pos,
            @Nonnull final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ,
            @Nonnull final EnumHand hand
    ) {


        if (player.getHeldItem(hand).getItemDamage() == 10) {
            if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityBlock) {
                TileEntityBlock tile = (TileEntityBlock) world.getTileEntity(pos);
                assert tile != null;
                if (tile.getComponent(Energy.class) != null) {
                    NBTTagCompound nbttagcompound = ModUtils.nbt(player.getHeldItem(hand));
                    boolean charge = nbttagcompound.getBoolean("change");
                    if (tile instanceof TileEntityElectricBlock && charge) {
                        return EnumActionResult.PASS;
                    }
                    nbttagcompound.setInteger("Xcoord", tile.getPos().getX());
                    nbttagcompound.setInteger("Ycoord", tile.getPos().getY());
                    nbttagcompound.setInteger("Zcoord", tile.getPos().getZ());
                    nbttagcompound.setInteger("tier", tile.getComponent(Energy.class).getSinkTier());
                    nbttagcompound.setInteger("World1", tile.getWorld().provider.getDimension());
                    nbttagcompound.setString("World", tile.getWorld().provider.getDimensionType().getName());
                    nbttagcompound.setString(
                            "Name",
                            Localization.translate(Objects.requireNonNull(tile.getDisplayName()).getFormattedText())
                    );
                    return EnumActionResult.SUCCESS;
                } else if (tile.getComponent(AdvEnergy.class) != null) {
                    NBTTagCompound nbttagcompound = ModUtils.nbt(player.getHeldItem(hand));
                    boolean charge = nbttagcompound.getBoolean("change");
                    if (tile instanceof TileEntityElectricBlock && charge) {
                        return EnumActionResult.PASS;
                    }
                    nbttagcompound.setInteger("Xcoord", tile.getPos().getX());
                    nbttagcompound.setInteger("Ycoord", tile.getPos().getY());
                    nbttagcompound.setInteger("Zcoord", tile.getPos().getZ());
                    nbttagcompound.setInteger("tier", tile.getComponent(AdvEnergy.class).getSinkTier());
                    nbttagcompound.setInteger("World1", tile.getWorld().provider.getDimension());
                    nbttagcompound.setString("World", tile.getWorld().provider.getDimensionType().getName());
                    nbttagcompound.setString(
                            "Name",
                            Localization.translate(Objects.requireNonNull(tile.getDisplayName()).getFormattedText())
                    );
                    return EnumActionResult.SUCCESS;
                } else if (tile instanceof TileEntityAnalyzerChest) {
                    NBTTagCompound nbttagcompound = ModUtils.nbt(player.getHeldItem(hand));

                    nbttagcompound.setInteger("Xcoord", tile.getPos().getX());
                    nbttagcompound.setInteger("Ycoord", tile.getPos().getY());
                    nbttagcompound.setInteger("Zcoord", tile.getPos().getZ());
                    nbttagcompound.setInteger("tier", 0);
                    nbttagcompound.setInteger("World1", tile.getWorld().provider.getDimension());
                    nbttagcompound.setString("World", tile.getWorld().provider.getDimensionType().getName());
                    nbttagcompound.setString(
                            "Name",
                            Localization.translate(Objects.requireNonNull(tile.getDisplayName()).getFormattedText())
                    );
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);

    }

    @Override
    public void addInformation(
            @Nonnull final ItemStack itemStack,
            @Nullable final World worldIn,
            @Nonnull final List<String> info,
            @Nonnull final ITooltipFlag flagIn
    ) {
        super.addInformation(itemStack, worldIn, info, flagIn);
        int meta = itemStack.getItemDamage();
        switch (meta) {
            case 0:
                info.add(Localization.translate("iu.modules"));
                info.add(Localization.translate("personality"));
                info.add(Localization.translate("personality1"));
                for (int i = 0; i < 9; i++) {
                    NBTTagCompound nbt = ModUtils.nbt(itemStack);
                    String name = "player_" + i;
                    if (!nbt.getString(name).isEmpty()) {
                        info.add(nbt.getString(name));
                    }
                }
                break;
            case 1:
                info.add(Localization.translate("iu.modules"));
                info.add(Localization.translate("transformator"));
                break;
            case 2:
                info.add(Localization.translate("iu.modules"));
                info.add(Localization.translate("transformator1"));
                break;
            case 3:
                info.add(Localization.translate("modulechange"));

                break;
            case 4:
                info.add(Localization.translate("modulerfinfo"));
                info.add(Localization.translate("modulerfinfo1"));
                break;
            case 5:
            case 8:
            case 7:
            case 6:
                info.add(Localization.translate("storagemodul"));
                info.add(Localization.translate("storagemodul1"));
                break;
            case 10:
                NBTTagCompound nbttagcompound = ModUtils.nbt(itemStack);
                info.add(Localization.translate("iu.modules"));
                info.add(Localization.translate("wirelles"));
                info.add(Localization.translate("iu.World") + ": " + nbttagcompound.getString("World"));

                info.add(Localization.translate("iu.tier") + nbttagcompound.getInteger("tier"));
                info.add(Localization.translate("iu.Xcoord") + ": " + nbttagcompound.getInteger("Xcoord"));
                info.add(Localization.translate("iu.Ycoord") + ": " + nbttagcompound.getInteger("Ycoord"));
                info.add(Localization.translate("iu.Zcoord") + ": " + nbttagcompound.getInteger("Zcoord"));
                if (nbttagcompound.getBoolean("change")) {
                    info.add(Localization.translate("mode.storage"));

                } else {
                    info.add(Localization.translate("mode.panel"));

                }
        }
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    public enum CraftingTypes implements IIdProvider {
        personality(0),
        tier_in(1),
        tier_de(2),
        charger(3),
        rf(4),
        mov_charge_eu(5),
        mov_charge_eu_item(6),
        mov_charge_rf(7),
        mov_charge_rf_item(8),
        purifier(9),
        wireless(10),
        ;

        private final String name;
        private final int ID;

        CraftingTypes(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static CraftingTypes getFromID(final int ID) {
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
