package com.denfop.items.modules;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.cool.EnumCoolUpgrade;
import com.denfop.api.cool.ICoolItem;
import com.denfop.blocks.ISubEnum;
import com.denfop.componets.CoolComponent;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ItemCoolingUpgrade extends ItemSubTypes<ItemCoolingUpgrade.Types> implements IModelRegister, ICoolItem {

    protected static final String NAME = "itemcoolupgrade";

    public ItemCoolingUpgrade() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.ModuleTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("iu.iu", "iu.item"));
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
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
                if (block instanceof TileMultiMachine) {
                    TileMultiMachine multiMachine = (TileMultiMachine) block;
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

    public enum Types implements ISubEnum {
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
