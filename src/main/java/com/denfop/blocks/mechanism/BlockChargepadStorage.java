package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IElectricBlock;
import com.denfop.api.IStorage;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileElectricBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.wiring.chargepad.TileChargepadAdvMFSU;
import com.denfop.tiles.wiring.chargepad.TileChargepadBarMFSU;
import com.denfop.tiles.wiring.chargepad.TileChargepadBatBox;
import com.denfop.tiles.wiring.chargepad.TileChargepadCESU;
import com.denfop.tiles.wiring.chargepad.TileChargepadGraMFSU;
import com.denfop.tiles.wiring.chargepad.TileChargepadHadrMFSU;
import com.denfop.tiles.wiring.chargepad.TileChargepadKvrMFSU;
import com.denfop.tiles.wiring.chargepad.TileChargepadMFE;
import com.denfop.tiles.wiring.chargepad.TileChargepadMFSU;
import com.denfop.tiles.wiring.chargepad.TileChargepadPerMFSU;
import com.denfop.tiles.wiring.chargepad.TileChargepadUltMFSU;
import com.denfop.utils.ModUtils;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum BlockChargepadStorage implements IMultiTileBlock, IElectricBlock {
    adv_mfsu_chargepad(TileChargepadAdvMFSU.class, 0),
    ult_mfsu_chargepad(TileChargepadUltMFSU.class, 1),
    batbox_iu_chargepad(TileChargepadBatBox.class, 2),
    cesu_iu_chargepad(TileChargepadCESU.class, 3),
    mfe_iu_chargepad(TileChargepadMFE.class, 4),
    mfsu_iu_chargepad(TileChargepadMFSU.class, 5),
    per_mfsu_chargepad(TileChargepadPerMFSU.class, 6),
    bar_mfsu_chargepad(TileChargepadBarMFSU.class, 7),
    had_mfsu_chargepad(TileChargepadHadrMFSU.class, 8),
    gra_mfsu_chargepad(TileChargepadGraMFSU.class, 9),
    qua_mfsu_chargepad(TileChargepadKvrMFSU.class, 10),

    ;


    //
    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("wiring_chargepad");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockChargepadStorage(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockChargepadStorage(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }
    int idBlock;
    public  int getIDBlock(){
        return idBlock;
    };

    public void setIdBlock(int id){
        idBlock = id;
    };
    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockChargepadStorage block : BlockChargepadStorage.values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception e) {

                }
            }
        }
    }

    @Override
    public boolean hasOtherVersion() {
        return true;
    }

    @Override
    public List<ItemStack> getOtherVersion(ItemStack stack) {
        final List<ItemStack> list = new ArrayList<>();
        stack = stack.copy();
        if (this.dummyTe == null) {
            try {
                this.dummyTe = this.teClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        ModUtils.nbt(stack).setDouble(
                "energy",
                ((IStorage) (((IElectricBlock) this).getDummyElec())).getEUCapacity()
        );
        list.add(stack);
        return list;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public int getId() {
        return this.itemMeta;
    }

    @Override
    @Nonnull
    public ResourceLocation getIdentifier() {
        return BlockChargepadStorage.IDENTITY;
    }

    @Override
    public boolean hasItem() {
        return true;
    }

    @Override
    public Class<? extends TileEntityBlock> getTeClass() {
        return this.teClass;
    }

    @Override
    public boolean hasActive() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    @Nonnull
    public Set<EnumFacing> getSupportedFacings() {
        return ModUtils.downSideFacings;
    }

    @Override
    public float getHardness() {
        return 3.0f;
    }

    @Override
    @Nonnull
    public MultiTileBlock.HarvestTool getHarvestTool() {
        return MultiTileBlock.HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public MultiTileBlock.DefaultDrop getDefaultDrop() {
        return MultiTileBlock.DefaultDrop.Self;
    }

    @Override
    public boolean allowWrenchRotating() {
        return true;
    }

    @Override
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public TileElectricBlock getDummyElec() {
        return (TileElectricBlock) this.dummyTe;
    }
}
