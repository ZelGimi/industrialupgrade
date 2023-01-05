package com.denfop.items.machines;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.tiles.mechanism.TileEntityOilRefiner;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ItemMulti;
import ic2.core.item.block.ItemBlockTileEntity;
import ic2.core.ref.BlockName;
import ic2.core.ref.ItemName;
import ic2.core.ref.TeBlock;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemsRefiner extends ItemMulti<ItemsRefiner.Types> implements IModelRegister {

    protected static final String NAME = "refiner_item";

    public ItemsRefiner() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.IUTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(
            final ItemStack stack,
            @Nullable final World world,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 2 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);

            int size = nbt.getInteger("size");
            List<FluidStack> fluidStackList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                FluidStack fluidStack =
                        FluidStack.loadFluidStackFromNBT((NBTTagCompound) stack.getTagCompound().getTag("fluid" + i));
                if (fluidStack != null) {
                    fluidStackList.add(fluidStack);
                }
            }
            if (fluidStackList.isEmpty()) {
                super.addInformation(stack, world, tooltip, flagIn);
                return;
            }
            if (fluidStackList.size() == 1) {
                tooltip.add(Localization.translate("iu.fluid.info") + fluidStackList.get(0).getLocalizedName());
                tooltip.add(Localization.translate("iu.fluid.info1") + fluidStackList.get(0).amount / 1000 + " B");
            } else {
                tooltip.add(Localization.translate("iu.fluid.info2"));
                for (FluidStack fluidStack : fluidStackList) {
                    tooltip.add(fluidStack.getLocalizedName() + " " + fluidStack.amount / 1000 + " B");
                }

            }

            super.addInformation(stack, world, tooltip, flagIn);
            return;
        }
        super.addInformation(stack, world, tooltip, flagIn);

    }

    public EnumActionResult onItemUse(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumHand hand,
            EnumFacing side,
            float hitX,
            float hitY,
            float hitZ
    ) {
        ItemStack stack = StackUtil.get(player, hand);
        IBlockState oldState = world.getBlockState(pos);
        Block oldBlock = oldState.getBlock();
        if (!oldBlock.isReplaceable(world, pos)) {
            pos = pos.offset(side);
        }

        Block newBlock = BlockName.te.getInstance();
        if (!StackUtil.isEmpty(stack) && player.canPlayerEdit(pos, side, stack) && world.mayPlace(
                newBlock,
                pos,
                false,
                side,
                player
        ) && ((BlockTileEntity) newBlock).canReplace(world, pos, side, BlockName.te.getItemStack(TeBlock.cable))) {
            newBlock.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, 0, player, hand);


            TileEntityOilRefiner te = new TileEntityOilRefiner();

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
                StackUtil.consumeOrError(player, hand, 1);

            }

            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.PASS;
        }
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
                new ModelResourceLocation(Constants.MOD_ID + ":" + Types.getFromID(meta).getName(), null)
        );
    }

    public enum Types implements IIdProvider {
        refiner_item(0),
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
