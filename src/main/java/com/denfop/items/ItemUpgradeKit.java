package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.tiles.base.TileEntityElectricBlock;
import com.denfop.tiles.wiring.EnumElectricBlock;
import com.denfop.utils.ModUtils;
import ic2.core.IC2;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
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
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
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
    public void addInformation(
            @Nonnull final ItemStack p_77624_1_,
            @Nullable final World p_77624_2_,
            final List<String> p_77624_3_,
            @Nonnull final ITooltipFlag p_77624_4_
    ) {
        p_77624_3_.add(Localization.translate("waring_kit"));
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);

    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    @Nonnull
    public EnumActionResult onItemUseFirst(
            @Nonnull EntityPlayer player,
            @Nonnull World world,
            @Nonnull BlockPos pos,
            @Nonnull EnumFacing side,
            float hitX,
            float hitY,
            float hitZ,
            @Nonnull EnumHand hand
    ) {
        if (IC2.platform.isSimulating()) {
            ItemStack stack = player.getHeldItem(hand);
            int meta = stack.getItemDamage();
            TileEntity tileEntity = world.getTileEntity(pos);


            if (tileEntity instanceof TileEntityElectricBlock) {
                TileEntityElectricBlock tile = (TileEntityElectricBlock) tileEntity;
                final EnumElectricBlock enumblock = TileEntityElectricBlock.getElectricBlock();
                if (enumblock != null && enumblock.kit_meta == meta) {
                    ItemStack stack1;
                    if (tile.getElectricBlock().chargepad) {
                        stack1 = new ItemStack(IUItem.Chargepadelectricblock, 1, TileEntityElectricBlock.getElectricBlock().meta);
                    } else {
                        stack1 = new ItemStack(IUItem.electricblock, 1, TileEntityElectricBlock.getElectricBlock().meta);
                    }
                    final NBTTagCompound nbt = ModUtils.nbt(stack1);
                    nbt.setDouble("energy", tile.energy.getEnergy());
                    nbt.setDouble("energy2", tile.energy2);

                    world.removeTileEntity(pos);
                    world.setBlockToAir(pos);

                    EntityItem item = new EntityItem(world);
                    item.setItem(stack1);
                    if (!player.getEntityWorld().isRemote) {
                        item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                        item.setPickupDelay(0);
                        world.spawnEntity(item);

                    }
                    List<ItemStack> list = tile.getDrop();
                    EntityItem[] item1 = new EntityItem[list.size()];

                    for (ItemStack stack2 : list) {
                        item1[list.indexOf(stack2)] = new EntityItem(world);
                        item1[list.indexOf(stack2)].setItem(stack2);

                        if (!player.getEntityWorld().isRemote) {
                            item1[list.indexOf(stack2)].setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                            item1[list.indexOf(stack2)].setPickupDelay(0);
                            world.spawnEntity(item1[list.indexOf(stack2)]);
                        }
                    }

                    stack.setCount(stack.getCount() - 1);

                }
                return EnumActionResult.PASS;

            }

        }
        return EnumActionResult.PASS;
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
