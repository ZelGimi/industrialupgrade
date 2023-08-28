package com.denfop.blocks;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.items.block.ItemBlockIU;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

public abstract class BlockBase extends Block implements IModelRegister {

    protected BlockBase(String name, Material material) {
        this(name, material, ItemBlockIU.supplier);
    }

    protected BlockBase(String name, Material material, Class<? extends ItemBlock> itemClass) {
        this(name, material, createItemBlockSupplier(itemClass));
    }

    protected BlockBase(String name, Material material, Function<Block, Item> itemSupplier) {
        super(material);
        this.setCreativeTab(IUCore.IUTab);
        if (name != null) {
            this.register(name, IUCore.getIdentifier(name), itemSupplier);
        }

        IUCore.proxy.addIModelRegister(this);
    }

    protected static Function<Block, Item> createItemBlockSupplier(final Class<? extends ItemBlock> cls) {
        if (cls == null) {
            throw new NullPointerException("null item class");
        } else {
            return input -> {
                try {
                    return (Item) cls.getConstructor(Block.class).newInstance(input);
                } catch (Exception var3) {
                    throw new RuntimeException(var3);
                }
            };
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerDefaultItemModel(Block block) {
        registerItemModels(block, Collections.singletonList(block.getDefaultState()));
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModels(Block block, Iterable<IBlockState> states) {
        registerItemModels(block, states, null);
    }

    @SideOnly(Side.CLIENT)
    public static void registerItemModels(Block block, Iterable<IBlockState> states, IStateMapper mapper) {
        Item item = Item.getItemFromBlock(block);
        if (item != Items.AIR) {
            ResourceLocation loc = ModUtils.getName(item);
            if (loc != null) {
                Map<IBlockState, ModelResourceLocation> locations = mapper != null ? mapper.putStateModelLocations(block) : null;

                for (final IBlockState state : states) {
                    int meta = block.getMetaFromState(state);
                    ModelResourceLocation location = locations != null
                            ? locations.get(state)
                            : new ModelResourceLocation(loc, new DefaultStateMapper().getPropertyString(state.getProperties()));
                    if (location == null) {
                        throw new RuntimeException("can't map state " + state);
                    }

                    ModelLoader.setCustomModelResourceLocation(item, meta, location);
                }

            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerDefaultVanillaItemModel(Block block, String path) {
        Item item = Item.getItemFromBlock(block);
        if (item != Items.AIR) {
            ResourceLocation loc = ModUtils.getName(item);
            if (loc != null) {
                if (path != null && !path.isEmpty()) {
                    path = path + '/' + loc;
                } else {
                    path = loc.toString();
                }

                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(path, null));
            }
        }
    }

    protected void register(String name, ResourceLocation identifier, Function<Block, Item> itemSupplier) {
        this.setUnlocalizedName(name);
        Register.registerBlock(this, identifier);
        if (itemSupplier != null) {
            Register.registerItem(itemSupplier.apply(this), identifier);
        }

    }

    protected void register(String name, ResourceLocation identifier, Item itemSupplier) {
        this.setUnlocalizedName(name);
        Register.registerBlock(this, identifier);
        if (itemSupplier != null) {
            Register.registerItem(itemSupplier, identifier);
        }

    }

    @SideOnly(Side.CLIENT)
    public void registerModels() {
        registerDefaultItemModel(this);
    }

    public String getUnlocalizedName() {
        return Constants.ABBREVIATION + "." + super.getUnlocalizedName().substring(5);
    }

    public String getLocalizedName() {
        return Localization.translate(this.getUnlocalizedName());
    }

    public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }


}
