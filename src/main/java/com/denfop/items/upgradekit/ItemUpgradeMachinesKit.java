package com.denfop.items.upgradekit;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.IIdProvider;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class ItemUpgradeMachinesKit extends ItemMulti<ItemUpgradeMachinesKit.Types> implements IModelRegister {

    protected static final String NAME = "upgradekitmachine";
    public static int tick = 0;
    public static int[] inform = new int[4];

    public ItemUpgradeMachinesKit() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.UpgradeTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    @Override
    public void addInformation(
            @Nonnull final ItemStack p_77624_1_,
            @Nullable final World p_77624_2_,
            final List<String> p_77624_3_,
            @Nonnull final ITooltipFlag p_77624_4_
    ) {
        p_77624_3_.add(Localization.translate("waring_kit"));
        p_77624_3_.add(Localization.translate("using_kit"));
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            final List<ItemStack> list = IUItem.map_upgrades.get(p_77624_1_.getItemDamage());
            p_77624_3_.add(Localization.translate(list
                    .get(inform[p_77624_1_.getItemDamage()] % list.size())
                    .getUnlocalizedName()));
        } else {
            for (ItemStack name : IUItem.map_upgrades.get(p_77624_1_.getItemDamage())) {
                p_77624_3_.add(Localization.translate(name
                        .getUnlocalizedName()));
            }
        }
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);

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
        if (!IC2.platform.isSimulating()) {
            return EnumActionResult.PASS;
        } else {
            final boolean hooks = ForgeHooks.onRightClickBlock(player, hand, pos, side, new Vec3d(hitX, hitY, hitZ)).isCanceled();
            if (hooks) {
                return EnumActionResult.PASS;
            }
            ItemStack stack = player.getHeldItem(hand);
            TileEntity tileEntity = world.getTileEntity(pos);

            /*if (stack.getItemDamage() != 3) {

                if (tileEntity instanceof TileEntityMultiMachine) {
                    TileEntityMultiMachine tile1 = (TileEntityMultiMachine) tileEntity;

                    EnumMultiMachine type = tile1.getMachine();
                    if (type.upgrade == stack.getItemDamage()) {
                        List<ItemStack> list = tile1.getWrenchDrops(player, 100);
                        list.remove(0);


                        world.removeTileEntity(pos);
                        world.setBlockToAir(pos);
                        final ItemStack stack1 = new ItemStack(type.block_new, 1, type.meta_new);
                        EntityItem item = new EntityItem(world);
                        item.setItem(stack1);
                        if (!player.getEntityWorld().isRemote) {
                            item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                            item.setPickupDelay(0);
                            world.spawnEntity(item);

                        }


                        for (ItemStack stack2 : list) {
                            final EntityItem entityItem = new EntityItem(world);
                            entityItem.setItem(stack2);

                            if (!player.getEntityWorld().isRemote) {
                                entityItem.setLocationAndAngles(
                                        player.posX,
                                        player.posY,
                                        player.posZ,
                                        0.0F,
                                        0.0F
                                );
                                entityItem.setPickupDelay(0);
                                world.spawnEntity(entityItem);
                            }
                        }
                        stack.setCount(stack.getCount() - 1);
                        return EnumActionResult.SUCCESS;


                    }
                }

            } else {

                if (tileEntity instanceof TileEntityMultiMachine) {
                    TileEntityMultiMachine tile1 = (TileEntityMultiMachine) tileEntity;

                    EnumMultiMachine type = tile1.getMachine();
                    if (type.upgrade == -1) {
                        return EnumActionResult.PASS;
                    }
                    List<ItemStack> list = tile1.getWrenchDrops(player, 100);
                    list.remove(0);

                    world.removeTileEntity(pos);
                    world.setBlockToAir(pos);
                    final ItemStack stack1 = new ItemStack(type.type.block, 1, type.type.meta);
                    EntityItem item = new EntityItem(world);
                    item.setItem(stack1);
                    if (!player.getEntityWorld().isRemote) {
                        item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                        item.setPickupDelay(0);
                        world.spawnEntity(item);

                    }

                    for (ItemStack stack2 : list) {
                        final EntityItem entityItem = new EntityItem(world);
                        entityItem.setItem(stack2);

                        if (!player.getEntityWorld().isRemote) {
                            entityItem.setLocationAndAngles(
                                    player.posX,
                                    player.posY,
                                    player.posZ,
                                    0.0F,
                                    0.0F
                            );
                            entityItem.setPickupDelay(0);
                            world.spawnEntity(entityItem);
                        }
                    }
                    stack.setCount(stack.getCount() - 1);
                    return EnumActionResult.SUCCESS;


                }
            }
        }*/
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
        upgradepanelkitmachine(0),
        upgradepanelkitmachine1(1),
        upgradepanelkitmachine2(2),
        upgradepanelkitmachine3(3),
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
