package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.tiles.base.TileEntityElectricBlock;
import com.denfop.tiles.wiring.EnumElectricBlockState;
import ic2.core.IC2;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class ItemUpgradeKit extends ItemMulti<ItemUpgradeKit.Types> implements IModelRegister {

    protected static final String NAME = "upgradekitstorage";

    public ItemUpgradeKit() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.ItemTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    public EnumActionResult onItemUseFirst(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumFacing side,
            float hitX,
            float hitY,
            float hitZ,
            EnumHand hand
    ) {
        if (!IC2.platform.isSimulating()) {
            return EnumActionResult.PASS;
        } else {
            ItemStack stack = player.getHeldItem(hand);
            int meta = stack.getItemDamage();
            TileEntity tileEntity = world.getTileEntity(pos);


            int i;

            if (tileEntity instanceof TileEntityElectricBlock) {
                TileEntityElectricBlock tile = (TileEntityElectricBlock) tileEntity;
                EnumElectricBlockState enumblock = IUItem.map6.get(TileEntityElectricBlock.getElectricBlock().id);

                if (enumblock != null && enumblock.kit_meta == meta) {


                    TileEntityElectricBlock te = enumblock.state;


                    if (te != null) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        tile.writeToNBT(nbt);
                        te.readFromNBT(nbt);
                        world.setTileEntity(pos, te);
                        te.onUpgraded();
                        te.markDirty();
                        stack.setCount(stack.getCount() - 1);
                        return EnumActionResult.SUCCESS;
                    }


                }
                return EnumActionResult.PASS;

            }
            return EnumActionResult.PASS;

        }
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
        upgradekit(0),
        upgradekit1(1),
        upgradekit2(2),
        upgradekit3(3),

        upgradekit4(4),
        upgradekit5(5),
        upgradekit6(6),
        upgradekit7(7),
        upgradekit8(8),
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
