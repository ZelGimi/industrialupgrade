package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockRubWood;
import com.denfop.register.Register;
import com.denfop.utils.DamageHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
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

import java.util.List;

public class ItemTreetap extends Item implements IModelRegister {

    private final String name;

    public ItemTreetap() {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(16);
        this.setCreativeTab(IUCore.EnergyTab);
        this.name = "treetap";
        IUCore.proxy.addIModelRegister(this);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);

    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "tools" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name));
    }

    public static boolean attemptExtract(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumFacing side,
            IBlockState state,
            List<ItemStack> stacks
    ) {
        assert state.getBlock() == IUItem.rubWood;

        BlockRubWood.RubberWoodState rwState = state.getValue(BlockRubWood.stateProperty);
        if (!rwState.isPlain() && rwState.facing == side) {
            if (rwState.wet) {
                if (!world.isRemote) {
                    world.setBlockState(pos, state.withProperty(BlockRubWood.stateProperty, rwState.getDry()));
                    if (stacks != null) {

                    } else {
                        ejectResin(world, pos, side, world.rand.nextInt(3) + 1);
                    }


                }

                if (world.isRemote && player != null) {
                    player.playSound(EnumSound.Treetap.getSoundEvent(), 1F, 1);

                }

                return true;
            } else {
                if (!world.isRemote && world.rand.nextInt(5) == 0) {
                    world.setBlockState(
                            pos,
                            state.withProperty(BlockRubWood.stateProperty, BlockRubWood.RubberWoodState.plain_y)
                    );
                }

                if (world.rand.nextInt(5) == 0) {
                    if (!world.isRemote) {
                        ejectResin(world, pos, side, 1);
                        if (stacks != null) {
                        } else {
                            ejectResin(world, pos, side, 1);
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private static void ejectResin(World world, BlockPos pos, EnumFacing side, int quantity) {
        double ejectX = (double) pos.getX() + 0.5 + (double) side.getFrontOffsetX() * 0.3;
        double ejectY = (double) pos.getY() + 0.5 + (double) side.getFrontOffsetY() * 0.3;
        double ejectZ = (double) pos.getZ() + 0.5 + (double) side.getFrontOffsetZ() * 0.3;

        EntityItem entityitem = new EntityItem(
                world,
                ejectX,
                ejectY,
                ejectZ,
                IUItem.latex.copy()
        );
        entityitem.setDefaultPickupDelay();
        entityitem.getItem().setCount(quantity);
        world.spawnEntity(entityitem);

    }

    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedName(stack).replace("item.", "iu."));
    }

    @Override
    public void registerModels() {
        this.registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(String name) {
        this.registerModel(0, name);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(int meta, String name) {
        registerModel(this, meta, name);
    }

    public EnumActionResult onItemUse(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumHand hand,
            EnumFacing side,
            float xOffset,
            float yOffset,
            float zOffset
    ) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block == IUItem.rubWood) {
            if (attemptExtract(player, world, pos, side, state, null)) {
                if (!world.isRemote) {
                    DamageHandler.damage(player.getHeldItem(hand), 1, player);
                }

                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.FAIL;
            }
        } else {
            return EnumActionResult.PASS;
        }
    }


}
