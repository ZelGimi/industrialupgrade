package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
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

public class ItemUpgradeMachinesKit extends ItemMulti<ItemUpgradeMachinesKit.Types> implements IModelRegister {

    protected static final String NAME = "upgradekitmachine";

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
            ItemStack stack = player.getHeldItem(hand);
            TileEntity tileEntity = world.getTileEntity(pos);
            if (stack.getItemDamage() != 3) {

                if (tileEntity instanceof TileEntityMultiMachine) {
                    TileEntityMultiMachine tile1 = (TileEntityMultiMachine) tileEntity;

                    EnumMultiMachine type = tile1.getMachine();
                    if (type.upgrade == stack.getItemDamage()) {
                        ItemStack stack_rf = null;
                        ItemStack stack_quickly = null;
                        ItemStack stack_modulesize = null;
                        ItemStack panel = null;

                        if (tile1.rf) {
                            stack_rf = new ItemStack(IUItem.module7, 1, 4);
                        }
                        if (tile1.quickly) {
                            stack_quickly = new ItemStack(IUItem.module_quickly);
                        }
                        if (tile1.modulesize) {
                            stack_modulesize = new ItemStack(IUItem.module_stack);
                        }
                        if (tile1.solartype != null) {
                            panel = new ItemStack(IUItem.module6, 1, tile1.solartype.meta);
                        }

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
                        List<ItemStack> list = tile1.getDrop();
                        EntityItem[] item1 = new EntityItem[list.size()];

                        for (ItemStack stack2 : list) {
                            item1[list.indexOf(stack2)] = new EntityItem(world);
                            item1[list.indexOf(stack2)].setItem(stack2);

                            if (!player.getEntityWorld().isRemote) {
                                item1[list.indexOf(stack2)].setLocationAndAngles(
                                        player.posX,
                                        player.posY,
                                        player.posZ,
                                        0.0F,
                                        0.0F
                                );
                                item1[list.indexOf(stack2)].setPickupDelay(0);
                                world.spawnEntity(item1[list.indexOf(stack2)]);
                            }
                        }
                        if (stack_rf != null || stack_quickly != null || stack_modulesize != null || panel != null) {
                            item = new EntityItem(world);
                            if (stack_rf != null) {
                                item.setItem(stack_rf);
                            }
                            if (stack_quickly != null) {
                                item.setItem(stack_quickly);
                            }
                            if (stack_modulesize != null) {
                                item.setItem(stack_modulesize);
                            }
                            if (panel != null) {
                                item.setItem(panel);
                            }

                            if (!player.getEntityWorld().isRemote) {
                                item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                                item.setPickupDelay(0);
                                world.spawnEntity(item);

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
                    ItemStack stack_rf = null;
                    ItemStack stack_quickly = null;
                    ItemStack stack_modulesize = null;
                    ItemStack panel = null;

                    if (tile1.rf) {
                        stack_rf = new ItemStack(IUItem.module7, 1, 4);
                    }
                    if (tile1.quickly) {
                        stack_quickly = new ItemStack(IUItem.module_quickly);
                    }
                    if (tile1.modulesize) {
                        stack_modulesize = new ItemStack(IUItem.module_stack);
                    }
                    if (tile1.solartype != null) {
                        panel = new ItemStack(IUItem.module6, 1, tile1.solartype.meta);
                    }

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
                    List<ItemStack> list = tile1.getDrop();
                    EntityItem[] item1 = new EntityItem[list.size()];

                    for (ItemStack stack2 : list) {
                        item1[list.indexOf(stack2)] = new EntityItem(world);
                        item1[list.indexOf(stack2)].setItem(stack2);

                        if (!player.getEntityWorld().isRemote) {
                            item1[list.indexOf(stack2)].setLocationAndAngles(
                                    player.posX,
                                    player.posY,
                                    player.posZ,
                                    0.0F,
                                    0.0F
                            );
                            item1[list.indexOf(stack2)].setPickupDelay(0);
                            world.spawnEntity(item1[list.indexOf(stack2)]);
                        }
                    }
                    if (stack_rf != null || stack_quickly != null || stack_modulesize != null || panel != null) {
                        item = new EntityItem(world);
                        if (stack_rf != null) {
                            item.setItem(stack_rf);
                        }
                        if (stack_quickly != null) {
                            item.setItem(stack_quickly);
                        }
                        if (stack_modulesize != null) {
                            item.setItem(stack_modulesize);
                        }
                        if (panel != null) {
                            item.setItem(panel);
                        }

                        if (!player.getEntityWorld().isRemote) {
                            item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                            item.setPickupDelay(0);
                            world.spawnEntity(item);

                        }
                    }

                    stack.setCount(stack.getCount() - 1);
                    return EnumActionResult.SUCCESS;


                }
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
