package com.denfop.items.modules;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.cool.EnumCoolUpgrade;
import com.denfop.api.cool.ICoolItem;
import com.denfop.blocks.IIdProvider;
import com.denfop.componets.CoolComponent;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ItemCoolingUpgrade extends ItemMulti<ItemCoolingUpgrade.Types> implements IModelRegister, ICoolItem {

    protected static final String NAME = "itemcoolupgrade";

    public ItemCoolingUpgrade() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.ModuleTab);
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

    @Nonnull
    @Override
    public EnumActionResult onItemUseFirst(
            @Nonnull final EntityPlayer player,
            final World world,
            @Nonnull final BlockPos pos,
            @Nonnull final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ,
            @Nonnull final EnumHand hand
    ) {
        if (world.getTileEntity(pos) instanceof TileEntityInventory) {
            TileEntityInventory block = (TileEntityInventory) world.getTileEntity(pos);
            assert block != null;
            if (block.hasComp(CoolComponent.class)) {
                if (block instanceof TileEntityMultiMachine) {
                    TileEntityMultiMachine multiMachine = (TileEntityMultiMachine) block;
                    final ItemStack stack = player.getHeldItem(hand);
                    CoolComponent coolComponent = block.getComp(CoolComponent.class);
                    if (multiMachine.multi_process.getSizeWorkingSlot() <= this
                            .getTypeUpgrade(stack)
                            .getLevel() && !coolComponent.upgrade) {
                        coolComponent.upgrade = true;
                        coolComponent.meta = stack.getItemDamage();
                        stack.shrink(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @Override
    public EnumCoolUpgrade getTypeUpgrade(final ItemStack stack) {
        switch (stack.getItemDamage()) {
            default:
                return EnumCoolUpgrade.AZOTE;
            case 1:
                return EnumCoolUpgrade.HYDROGEN;
            case 2:
                return EnumCoolUpgrade.HELIUM;
        }
    }

    public enum Types implements IIdProvider {
        azote(0),
        hydrogen(1),
        helium(2),
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
