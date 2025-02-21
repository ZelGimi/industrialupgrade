package com.denfop.render.fluidcell;


import com.denfop.Constants;
import com.denfop.blocks.FluidName;
import com.denfop.render.base.MaskOverlayModel;
import com.denfop.render.base.ModelCuboidUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.Collections;

public class FluidCellModel extends MaskOverlayModel {

    private static final ResourceLocation baseModelLoc = new ResourceLocation(Constants.MOD_ID, "item/itemcell/itemcellempty");
    private static final ResourceLocation maskTextureLoc = new ResourceLocation(
            Constants.MOD_ID,
            "textures/items/fluid_background.png"
    );
    private final ItemOverrideList overrideHandler = new ItemOverrideList(Collections.emptyList()) {
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
            if (stack.isEmpty()) {
                return ModelCuboidUtil.getMissingModel();
            } else {
                FluidStack fs = FluidUtil.getFluidContained(stack);
                ResourceLocation spriteLoc;
                return fs != null && (spriteLoc = FluidName.isFluid(fs.getFluid()).getFlowing(fs)) != null ?
                        FluidCellModel.this.get(Minecraft
                                .getMinecraft()
                                .getTextureMapBlocks()
                                .getAtlasSprite(spriteLoc.toString()), fs.getFluid().getColor(fs)) : FluidCellModel.this.get();
            }
        }
    };

    public FluidCellModel() {
        super(baseModelLoc, maskTextureLoc, false, -0.1F);
    }

    public ItemOverrideList getOverrides() {
        return this.overrideHandler;
    }

}
