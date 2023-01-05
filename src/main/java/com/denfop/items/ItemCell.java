package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import ic2.core.crop.TileEntityCrop;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import ic2.core.util.LiquidUtil;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemCell extends ItemMulti<CellType> implements IModelRegister {

    protected static final String NAME = "itemcell";
    public static IItemUseHandler emptyCellFill = (stack, player, pos, hand, side) -> {
        assert stack.getItem() == IUItem.cell_all;

        World world = player.getEntityWorld();
        RayTraceResult position = Util.traceBlocks(player, true);
        if (position == null) {
            return EnumActionResult.FAIL;
        } else {
            if (position.typeOfHit == RayTraceResult.Type.BLOCK) {
                pos = position.getBlockPos();
                if (!world.canMineBlockBody(player, pos)) {
                    return EnumActionResult.FAIL;
                }

                if (!player.canPlayerEdit(pos, position.sideHit, player.getHeldItem(hand))) {
                    return EnumActionResult.FAIL;
                }

                LiquidUtil.LiquidData data = LiquidUtil.getLiquid(world, pos);
                if (data != null && data.isSource) {
                    if (IUItem.celltype.containsKey(data.liquid) && stack.getItemDamage() != 0) {
                        if (IUItem.celltype.containsKey(data.liquid) && StackUtil.storeInventoryItem(
                                new ItemStack(IUItem.cell_all, 1, IUItem.celltype.get(data.liquid)),
                                player,
                                true
                        )) {
                            world.setBlockToAir(pos);
                            StackUtil.consumeOrError(player, hand, 1);
                            StackUtil.storeInventoryItem(new ItemStack(IUItem.cell_all, 1, IUItem.celltype.get(data.liquid)),
                                    player, false
                            );
                            return EnumActionResult.SUCCESS;
                        }
                    }


                }

            }

            return EnumActionResult.PASS;
        }
    };

    public ItemCell() {
        super(null, CellType.class);
        this.setCreativeTab(IUCore.ItemTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);

        IUItem.uuMatterCell = new ItemStack(this, 1, 1);
        IUItem.HeliumCell = new ItemStack(this, 1, 2);
        IUItem.NeftCell = new ItemStack(this, 1, 3);
        IUItem.BenzCell = new ItemStack(this, 1, 4);
        IUItem.DizelCell = new ItemStack(this, 1, 5);
        IUItem.PolyethCell = new ItemStack(this, 1, 6);
        IUItem.PolypropCell = new ItemStack(this, 1, 7);
        IUItem.OxyCell = new ItemStack(this, 1, 8);
        IUItem.HybCell = new ItemStack(this, 1, 9);
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    @Override
    public EnumActionResult onItemUse(
            final EntityPlayer player,
            final World world,
            final BlockPos pos,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItemDamage() != 0) {
            Fluid fluid = IUItem.celltype1.get(stack.getItemDamage());
            if (StackUtil.storeInventoryItem(new ItemStack(IUItem.cell_all), player, true)) {
                if (LiquidUtil.fillBlock(new FluidStack(fluid, 1000), world, pos, false)) {
                    StackUtil.consumeOrError(player, hand, 1);
                    StackUtil.storeInventoryItem(new ItemStack(IUItem.cell_all, 1),
                            player, false
                    );
                    return EnumActionResult.SUCCESS;
                } else if (LiquidUtil.fillBlock(new FluidStack(fluid, 1000), world, pos.offset(EnumFacing.UP), false)) {
                    StackUtil.consumeOrError(player, hand, 1);
                    StackUtil.storeInventoryItem(new ItemStack(IUItem.cell_all, 1),
                            player, false
                    );
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);

    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
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
        ItemStack stack = StackUtil.get(player, hand);
        CellType type = this.getType(stack);
        if (type.hasCropAction()) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityCrop) {
                return type.doCropAction();
            }
        }

        return EnumActionResult.PASS;
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(
                        Constants.MOD_ID + ":" + NAME + "/" + "itemcell" + CellType.getFromID(meta).getName(),
                        null
                )
        );
    }

    public int getItemStackLimit(@Nonnull ItemStack stack) {
        CellType type = this.getType(stack);
        return type != null ? type.getStackSize() : 0;
    }

    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        return this.getType(stack).getUsage() > 0;
    }

    public double getDurabilityForDisplay(@Nonnull ItemStack stack) {
        CellType type = this.getType(stack);
        return (double) type.getUsage() / (double) type.getMaximum();
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<String> tooltip, ITooltipFlag advanced) {
        CellType type = this.getType(stack);
        if (type.getStackSize() == 1 && advanced.isAdvanced()) {
            int max = type.getMaximum();
            tooltip.add(Localization.translate("item.durability", max - type.getUsage(), max));
        }

    }

}
