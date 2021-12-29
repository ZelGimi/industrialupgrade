package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import com.denfop.tiles.mechanism.EnumUpgradesMultiMachine;
import ic2.core.IC2;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
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

public class ItemUpgradeMachinesKit extends ItemMulti<ItemUpgradeMachinesKit.Types> implements IModelRegister {

    protected static final String NAME = "upgradekitmachine";

    public ItemUpgradeMachinesKit() {
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
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileEntityElectricMachine) {
                TileEntityElectricMachine tile = (TileEntityElectricMachine) tileEntity;
                String name = tile.getName();
                if (IUItem.map4.containsKey(name)) {
                    EnumUpgradesMultiMachine machine = IUItem.map4.get(name);

                    if (stack.getItemDamage() == machine.meta_item) {
                        TileEntityBlock te = machine.blockstate;

                        if (te != null) {
                            NBTTagCompound nbt = new NBTTagCompound();
                            tile.writeToNBT(nbt);
                            te.readFromNBT(nbt);
                            world.setTileEntity(pos, te);
                            ((TileEntityMultiMachine) te).onUpgraded();
                            te.markDirty();
                            stack.setCount(stack.getCount() - 1);
                            return EnumActionResult.SUCCESS;
                        }

                    }
                }
            } else {
                if (tileEntity instanceof TileEntityMultiMachine) {
                    TileEntityMultiMachine tile1 = (TileEntityMultiMachine) tileEntity;

                    EnumMultiMachine type = tile1.getMachine();
                    if (type.upgrade == stack.getItemDamage()) {
                        TileEntityBlock state = IUItem.map5.get(type);
                        if (state != null) {
                            int module = tile1.module;
                            boolean rf = tile1.rf;
                            boolean quickly = tile1.quickly;
                            boolean modulesize = tile1.modulesize;
                            TileEntityBlock te = state;
                            if (te != null) {
                                NBTTagCompound nbt = new NBTTagCompound();
                                tile1.writeToNBT(nbt);
                                te.readFromNBT(nbt);
                                world.setTileEntity(pos, te);
                                ((TileEntityMultiMachine) te).onUpgraded();
                                te.markDirty();
                                ((TileEntityMultiMachine) te).rf = rf;
                                ((TileEntityMultiMachine) te).module = module;
                                ((TileEntityMultiMachine) te).quickly = quickly;
                                ((TileEntityMultiMachine) te).modulesize = modulesize;
                                stack.setCount(stack.getCount() - 1);
                                return EnumActionResult.SUCCESS;
                            }

                        }
                    }
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
