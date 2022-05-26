package com.denfop.blocks;


import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class BlockHeavyOre extends BlockCore implements IModelRegister {

    public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("type", Type.class);


    public BlockHeavyOre() {
        super(Material.ROCK, Constants.MOD_ID);
        setUnlocalizedName("heavyore");
        setCreativeTab(IUCore.OreTab);
        setHardness(3.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Type.Magnetite));
        setHarvestLevel("pickaxe", 2);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < (Type.values()).length; i++) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        if (meta >= (Type.values()).length) {
            meta = 0;
        }
        return "iu." + Type.values()[meta].getName() + ".name";
    }

    public EnumRarity getRarity(ItemStack stack) {
        int meta = stack.getItemDamage();
        if (meta >= (Type.values()).length) {
            return EnumRarity.COMMON;
        }
        return Type.values()[meta].getRarity();
    }

    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, Type.values()[meta]);
    }

    public int getMetaFromState(IBlockState state) {
        return ((Type) state.getValue((IProperty) VARIANT)).getMetadata();
    }

    public int damageDropped(IBlockState state) {
        return ((Type) state.getValue((IProperty) VARIANT)).getMetadata();
    }

    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return ((Type) state.getValue((IProperty) VARIANT)).getLight();
    }

    @SideOnly(Side.CLIENT)
    public void registerModels() {
        for (int i = 0; i < (Type.values()).length; i++) {
            ModelLoader.setCustomModelResourceLocation(
                    Item.getItemFromBlock(this),
                    i,
                    new ModelResourceLocation(this.modName + ":" + this.name, "type=" + Type.values()[i].getName())
            );
        }
    }

    public boolean preInit() {
        setRegistryName("heavyore");
        ForgeRegistries.BLOCKS.register(this);
        ItemBlockCore itemBlock = new ItemBlockCore(this);
        itemBlock.setRegistryName(getRegistryName());
        ForgeRegistries.ITEMS.register(itemBlock);
        IUCore.proxy.addIModelRegister(this);

        return true;
    }

    public boolean initialize() {

        return true;
    }

    public enum Type implements IStringSerializable {
        Magnetite(0),
        Calaverite(1),
        Galena(2),
        Nickelite(3),
        Pyrite(4),
        Quartzite(5),
        Uranite(6),
        Azurite(7),
        Rhodonite(8),
        Alfildit(9),
        Euxenite(10),
        Smithsonite(11),
        Ilmenite(12),
        Todorokite(13),
        Ferroaugite(14),
        Sheelite(15),
        ;

        private final int metadata;
        private final String name;

        Type(int metadata) {
            this.metadata = metadata;
            this.name = this.name().toLowerCase(Locale.US);
        }

        public static Type getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public int getMetadata() {
            return this.metadata;
        }

        public String getName() {
            return this.name;
        }

        public int getLight() {
            return 0;
        }

        public EnumRarity getRarity() {
            return EnumRarity.COMMON;
        }


    }

}
