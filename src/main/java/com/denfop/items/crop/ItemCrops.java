package com.denfop.items.crop;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.agriculture.ICrop;
import com.denfop.api.agriculture.ICropItem;
import com.denfop.api.agriculture.genetics.Genome;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IRegistryDelegate;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ItemCrops extends ItemSubTypes<ItemCrops.Types> implements IModelRegister, ICropItem {

    protected static final String NAME = "crops";

    public ItemCrops() {
        super(Types.class);
        this.setCreativeTab(IUCore.CropsTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void onUpdate(
            @Nonnull ItemStack itemStack,
            @Nonnull World p_77663_2_,
            @Nonnull Entity p_77663_3_,
            int p_77663_4_,
            boolean p_77663_5_
    ) {
        if (!(p_77663_3_ instanceof EntityPlayer)) {
            return;
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack p_77624_1_,
            @Nullable final World p_77624_2_,
            final List<String> p_77624_3_,
            final ITooltipFlag p_77624_4_
    ) {
        p_77624_3_.add(Localization.translate("iu.use_agriculture_analyzer") + Localization.translate(IUItem.agricultural_analyzer.getUnlocalizedName()));
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        ICrop crop = getCrop(0,p_77624_1_);
        if (crop.getId() != 3) {
            ItemStack soil = crop.getSoil().getStack();
            if (!soil.isEmpty()) {
                p_77624_3_.add(TextFormatting.YELLOW + Localization.translate("iu.crop.oneprobe.soil")+" " + soil.getDisplayName());
            }

            if (!crop.getDrops().isEmpty()) {
                ItemStack stack = crop.getDrops().get(0);
                if (!stack.isEmpty()) {
                    p_77624_3_.add(TextFormatting.AQUA + Localization.translate("iu.crop.oneprobe.drop")+" " + stack.getDisplayName());
                }
            }
            if (!crop.getCropCombine().isEmpty()){
                p_77624_3_.add(TextFormatting.GREEN + Localization.translate("iu.crop.breeding"));
                for (ICrop crop1 : crop.getCropCombine()){
                    p_77624_3_.add( Localization.translate("crop."+crop1.getName()));
                }
            }
        }
    }

    private int getIndex(Item item, int meta)
    {
        return Item.getIdFromItem(item) << 16 | meta;
    }
    Map<Integer,ModelResourceLocation> modelResourceLocationMap = new HashMap<>();
    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, final int meta, final String extraName) {
        ModelBakery.registerItemVariants(this,
                new ModelResourceLocation(
                        Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(),
                        null
                )
        );

            ModelLoader.setCustomMeshDefinition(this, stack -> {
                final NBTTagCompound nbt = ModUtils.nbt(stack);
                final boolean hasKey = nbt.hasKey("crop_id");
                if (!hasKey) {
                    return new ModelResourceLocation(
                            Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(),
                            null
                    );
                } else {
                    final ICrop crop = CropNetwork.instance.getCrop(nbt.getInteger("crop_id"));

                        final ModelManager modelManager = Minecraft.getMinecraft().modelManager;
                        boolean isShiftPressed = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
                        if (isShiftPressed && !crop.getDrops().isEmpty()) {
                            ItemStack stack1 = crop.getDrops().get(0);
                            ModelResourceLocation modelResourceLocation = modelResourceLocationMap.get(nbt.getInteger("crop_id"));
                            if (modelResourceLocation == null) {
                                final RegistrySimple<ModelResourceLocation, IBakedModel> registry = (RegistrySimple<ModelResourceLocation, IBakedModel>) modelManager.modelRegistry;
                                IBakedModel ibakedmodel = Minecraft
                                        .getMinecraft()
                                        .getItemRenderer().itemRenderer.getItemModelWithOverrides(
                                                stack1,
                                                null,
                                                null
                                        );
                                for (Map.Entry<ModelResourceLocation, IBakedModel> entry : registry.registryObjects.entrySet()) {
                                    if (entry.getValue() == ibakedmodel) {
                                        modelResourceLocationMap.put(nbt.getInteger("crop_id"),entry.getKey());
                                        return entry.getKey();
                                    }
                                }
                            }else{
                                return modelResourceLocation;
                            }
                        }

                    }
                    return new ModelResourceLocation(
                            Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(),
                            null
                    );

                }
            );

    }

    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final ICrop crop = CropNetwork.instance.getCrop(nbt.getInteger("crop_id"));
        return super.getItemStackDisplayName(stack) + ": " + Localization.translate("crop."+crop.getName());
    }

    @Override
    public ICrop getCrop(final int meta, final ItemStack stack) {
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        return CropNetwork.instance.getCrop(nbt.getInteger("crop_id"));
    }
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            CropNetwork.instance.getCropMap().forEach((id,crop) -> {
                if (id != 3) {
                    ItemStack stack = new ItemStack(this);
                    final NBTTagCompound nbt = ModUtils.nbt(stack);
                    nbt.setInteger("crop_id", id);
                    new Genome(stack);
                    subItems.add(stack);
                }
            });
        }
    }
    public enum Types implements ISubEnum {
        seeds,
        ;

        private final String name;
        private final int ID;

        Types() {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = this.ordinal();
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
