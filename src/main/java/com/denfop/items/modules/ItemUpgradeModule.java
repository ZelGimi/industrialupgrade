package com.denfop.items.modules;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.upgrade.UpgradeItemInform;
import com.denfop.blocks.IIdProvider;
import com.denfop.items.EnumInfoUpgradeModules;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
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
import java.util.List;
import java.util.Locale;

public class ItemUpgradeModule extends ItemMulti<ItemUpgradeModule.Types> implements IModelRegister {

    protected static final String NAME = "upgrademodules";

    public ItemUpgradeModule() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.ModuleTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public static EnumInfoUpgradeModules getType(int meta) {
        return EnumInfoUpgradeModules.getFromID(meta);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            @Nonnull final ITooltipFlag flagIn
    ) {
        final UpgradeItemInform upgrade = new UpgradeItemInform(getType(stack.getItemDamage()), 1);
        tooltip.add(upgrade.getName());
        tooltip.add(Localization.translate("iu.upgrade_item.info") + upgrade.upgrade.max);
        super.addInformation(stack, worldIn, tooltip, flagIn);
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

    public enum Types implements IIdProvider {
        upgrademodule(0),
        upgrademodule1(1),
        upgrademodule2(2),
        upgrademodule3(3),
        upgrademodule4(4),
        upgrademodule5(5),
        upgrademodule6(6),
        upgrademodule7(7),
        upgrademodule8(8),
        upgrademodule9(9),
        upgrademodule10(10),
        upgrademodule11(11),
        upgrademodule12(12),
        upgrademodule13(13),
        upgrademodule14(14),
        upgrademodule15(15),
        upgrademodule16(16),
        upgrademodule17(17),
        upgrademodule18(18),
        upgrademodule19(19),
        upgrademodule20(20),
        upgrademodule21(21),
        upgrademodule22(22),
        upgrademodule23(23),
        upgrademodule24(24),
        upgrademodule25(25),
        upgrademodule26(26),
        upgrademodule27(27),
        upgrademodule28(28),
        upgrademodule29(29),
        upgrademodule30(30),
        upgrademodule31(31),
        upgrademodule32(32),
        upgrademodule33(33),
        upgrademodule34(34),
        upgrademodule35(35),
        upgrademodule36(36),
        upgrademodule37(37),
        upgrademodule38(38),
        upgrademodule39(39),
        upgrademodule40(40),
        upgrademodule41(41),
        upgrademodule42(42),
        upgrademodule43(43),
        upgrademodule44(44),
        upgrademodule45(45),
        upgrademodule46(46),
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
