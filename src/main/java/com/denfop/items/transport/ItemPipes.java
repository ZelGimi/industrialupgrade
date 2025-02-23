package com.denfop.items.transport;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.items.block.ISubItem;
import com.denfop.items.block.ItemBlockTileEntity;
import com.denfop.register.Register;
import com.denfop.tiles.transport.tiles.TileEntityHeatPipes;
import com.denfop.tiles.transport.types.HeatType;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemPipes extends Item implements ISubItem<HeatType>, IModelRegister {

    public static final List<ItemStack> variants = new ArrayList<>();
    protected static final String NAME = "pipes_iu_item";
    String[] name = {"itempipes", "itempipes1", "itempipes2", "itempipes3", "itempipes4",};


    public ItemPipes() {
        super();
        this.setHasSubtypes(true);
        HeatType[] var1 = HeatType.values;


        for (HeatType type : var1) {
            for (int insulation = 0; insulation <= type.maxInsulation; ++insulation) {
                variants.add(getCable(type));
            }
        }
        this.setCreativeTab(IUCore.ItemTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(ItemStack stack) {
        final String loc = Constants.MOD_ID +
                ':' +
                "pipes" + "/" + getName(stack);

        return new ModelResourceLocation(loc, null);
    }

    private static HeatType getCableType(ItemStack stack) {
        int type = stack.getItemDamage();
        return type < HeatType.values.length ? HeatType.values[type] : HeatType.pipes;
    }

    private static int getInsulation(ItemStack stack) {
        HeatType type = getCableType(stack);
        return Math.min(0, type.maxInsulation);
    }

    private static String getName(ItemStack stack) {
        HeatType type = getCableType(stack);
        return type.getName();
    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack));
    }

    @SideOnly(Side.CLIENT)
    public void registerModels() {

        ModelLoader.setCustomMeshDefinition(this, ItemPipes::getModelLocation);
        for (final ItemStack stack : variants) {
            ModelLoader.setCustomModelResourceLocation(
                    this,
                    stack.getItemDamage(),
                    getModelLocation(stack)
            );
        }
        for (final ItemStack stack : variants) {
            ModelBakery.registerItemVariants(this, getModelLocation(stack));
        }

    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(
                        Constants.MOD_ID + ":" + NAME + "/" + HeatType.values[meta].getName(),
                        null
                )
        );
    }

    public ItemStack getItemStack(HeatType type) {
        return getCable(type);
    }

    public ItemStack getItemStack(String variant) {
        int pos = 0;
        HeatType type = null;

        int insulation;
        int nextPos;
        for (insulation = 0; pos < variant.length(); pos = nextPos + 1) {
            nextPos = variant.indexOf(44, pos);
            if (nextPos == -1) {
                nextPos = variant.length();
            }

            int sepPos = variant.indexOf(58, pos);
            if (sepPos == -1 || sepPos >= nextPos) {
                return null;
            }

            String key = variant.substring(pos, sepPos);
            String value = variant.substring(sepPos + 1, nextPos);
            if (key.equals("type")) {
                type = HeatType.get(value);

            } else if (key.equals("insulation")) {
                try {
                    insulation = Integer.parseInt(value);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if (type == null) {
            return null;
        } else if (insulation >= 0 && insulation <= type.maxInsulation) {
            return getCable(type);
        } else {
            return null;
        }
    }

    public String getVariant(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null stack");
        } else if (stack.getItem() != this) {
            throw new IllegalArgumentException("The stack " + stack + " doesn't match " + this);
        } else {
            HeatType type = getCableType(stack);
            int insulation = getInsulation(stack);
            return "type:" + type.getName() + ",insulation:" + insulation;
        }
    }

    public ItemStack getCable(HeatType type) {
        return new ItemStack(this, 1, type.getId());
    }

    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        if (meta >= name.length) {
            meta = 0;
        }
        return "iu." + name[meta];
    }


    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, World world, List<String> info, @Nonnull ITooltipFlag b) {
        HeatType type = getCableType(stack);
        double capacity;
        capacity = type.capacity;
        info.add(Localization.translate("iu.transport.heat") + ModUtils.getString(capacity) + " °C");
    }

    @Nonnull
    public EnumActionResult onItemUse(
            @Nonnull EntityPlayer player,
            World world,
            @Nonnull BlockPos pos,
            @Nonnull EnumHand hand,
            @Nonnull EnumFacing side,
            float hitX,
            float hitY,
            float hitZ
    ) {
        ItemStack stack = ModUtils.get(player, hand);
        IBlockState oldState = world.getBlockState(pos);
        Block oldBlock = oldState.getBlock();
        if (!oldBlock.isReplaceable(world, pos)) {
            pos = pos.offset(side);
        }

        Block newBlock = IUItem.invalid;
        if (!ModUtils.isEmpty(stack) && player.canPlayerEdit(pos, side, stack) && world.mayPlace(
                newBlock,
                pos,
                false,
                side,
                player
        ) && ((BlockTileEntity) newBlock).canReplace(world, pos, side, IUItem.invalid.getItemStack(MultiTileBlock.invalid))) {
            newBlock.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, 0, player, hand);
            HeatType type = getCableType(stack);

            TileEntityHeatPipes te;
            te = TileEntityHeatPipes.delegate(type);

            if (ItemBlockTileEntity.placeTeBlock(stack, player, world, pos, side, te)) {
                SoundType soundtype = newBlock.getSoundType(world.getBlockState(pos), world, pos, player);
                world.playSound(
                        player,
                        pos,
                        soundtype.getPlaceSound(),
                        SoundCategory.BLOCKS,
                        (soundtype.getVolume() + 1.0F) / 2.0F,
                        soundtype.getPitch() * 0.8F
                );
                player.getHeldItem(hand).shrink(1);

            }

            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.PASS;
        }
    }

    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> itemList) {
        if (this.isInCreativeTab(tab)) {
            List<ItemStack> variants = new ArrayList<>(ItemPipes.variants);


            itemList.addAll(variants);
        }
    }

    public Set<HeatType> getAllTypes() {
        return EnumSet.allOf(HeatType.class);
    }

    public Set<ItemStack> getAllStacks() {
        return new HashSet<>(variants);
    }

    public boolean canBeStoredInToolbox(ItemStack itemstack) {
        return true;
    }


}
