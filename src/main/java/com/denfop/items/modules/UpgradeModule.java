package com.denfop.items.modules;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.utils.EnumInfoUpgradeModules;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class UpgradeModule extends ItemMulti<UpgradeModule.Types> implements IModelRegister {

    protected static final String NAME = "upgrademodules";

    public UpgradeModule() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.tabssp1);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
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

    public static EnumInfoUpgradeModules getType(int meta) {
        switch (meta) {
            case 0:
                return EnumInfoUpgradeModules.GENDAY;
            case 1:
                return EnumInfoUpgradeModules.GENNIGHT;
            case 2:
                return EnumInfoUpgradeModules.PROTECTION;
            case 3:
                return EnumInfoUpgradeModules.EFFICIENCY;
            case 4:
                return EnumInfoUpgradeModules.BOWENERGY;
            case 5:
                return EnumInfoUpgradeModules.SABERENERGY;
            case 6:
                return EnumInfoUpgradeModules.DIG_DEPTH;
            case 7:
                return EnumInfoUpgradeModules.FIRE_PROTECTION;
            case 8:
                return EnumInfoUpgradeModules.WATER;
            case 9:
                return EnumInfoUpgradeModules.SPEED;
            case 10:
                return EnumInfoUpgradeModules.JUMP;
            case 11:
                return EnumInfoUpgradeModules.BOWDAMAGE;
            case 12:
                return EnumInfoUpgradeModules.SABER_DAMAGE;
            case 13:
                return EnumInfoUpgradeModules.AOE_DIG;
            case 14:
                return EnumInfoUpgradeModules.FLYSPEED;
            case 15:
                return EnumInfoUpgradeModules.STORAGE;
            case 16:
                return EnumInfoUpgradeModules.ENERGY;
            case 17:
                return EnumInfoUpgradeModules.VAMPIRES;
            case 18:
                return EnumInfoUpgradeModules.RESISTANCE;
            case 19:
                return EnumInfoUpgradeModules.POISON;
            case 20:
                return EnumInfoUpgradeModules.WITHER;
            case 21:
                return EnumInfoUpgradeModules.SILK_TOUCH;
            case 22:
                return EnumInfoUpgradeModules.INVISIBILITY;
            case 23:
                return EnumInfoUpgradeModules.LOOT;
            case 24:
                return EnumInfoUpgradeModules.FIRE;
            case 25:
                return EnumInfoUpgradeModules.REPAIRED;
        }
        return null;
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
