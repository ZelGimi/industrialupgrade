package com.denfop.tiles.mechanism.worlcollector;

import com.denfop.IUItem;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.EnumTypeCollector;
import com.denfop.tiles.base.IIsMolecular;
import com.denfop.tiles.base.TileBaseWorldCollector;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class TileCrystallize extends TileBaseWorldCollector implements IIsMolecular {

    protected ItemStack output_stack;
    @SideOnly(Side.CLIENT)
    private IBakedModel bakedModel;
    @SideOnly(Side.CLIENT)
    private IBakedModel transformedModel;

    public TileCrystallize() {
        super(EnumTypeCollector.DEFAULT);
    }

    public void init() {

        addRecipe(IUItem.diamondDust, 0.125, new ItemStack(Items.DIAMOND));


        addRecipe(IUItem.lapiDust, 0.0625, new ItemStack(Items.DYE, 1, 4));
        addRecipe(new ItemStack(Items.DIAMOND), 0.25, new ItemStack(Items.EMERALD));
        addRecipe(new ItemStack(Items.EMERALD), 0.25, new ItemStack(Items.DIAMOND));
        addRecipe(new ItemStack(IUItem.preciousgem, 1, 0), 0.25, new ItemStack(Items.EMERALD));
        addRecipe(new ItemStack(IUItem.preciousgem, 1, 1), 0.25, new ItemStack(Items.EMERALD));
        addRecipe(IUItem.iridiumOre, 20, new ItemStack(IUItem.iuingot, 1, 17));
        addRecipe(new ItemStack(Blocks.DIAMOND_ORE), 4, new ItemStack(Items.DIAMOND, 2));
        addRecipe(new ItemStack(Blocks.EMERALD_ORE), 4, new ItemStack(Items.EMERALD, 2));
        addRecipe(new ItemStack(Blocks.REDSTONE_ORE), 1, new ItemStack(Items.REDSTONE, 4));
        addRecipe(new ItemStack(Blocks.LAPIS_ORE), 1, new ItemStack(Items.DYE, 4, 4));
        addRecipe(new ItemStack(Items.GOLDEN_APPLE), 4, new ItemStack(Items.GOLDEN_APPLE, 1, 1));
        addRecipe(new ItemStack(Items.ROTTEN_FLESH), 4, new ItemStack(Items.PORKCHOP));
        addRecipe(new ItemStack(Items.SPIDER_EYE), 4, new ItemStack(Items.BEEF));
        addRecipe(new ItemStack(Items.STRING), 20, new ItemStack(Blocks.WEB));
        addRecipe(IUItem.latex, 10, new ItemStack(Items.SLIME_BALL));

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.crystallize;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.machineRecipe = output;
        if (this.machineRecipe != null) {
            output_stack = this.machineRecipe.getRecipe().output.items.get(0);
        } else {
            output_stack = new ItemStack(Items.AIR);
        }
        new PacketUpdateFieldTile(this, "output", this.output_stack);
        this.setOverclockRates();

    }

    public MachineRecipe getOutput() {

        this.machineRecipe = this.inputSlot.process();
        if (this.machineRecipe != null) {
            output_stack = this.machineRecipe.getRecipe().output.items.get(0);
        } else {
            output_stack = new ItemStack(Items.AIR);
        }

        return this.machineRecipe;
    }


    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, output_stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        if (name.equals("output")) {
            try {
                this.output_stack = (ItemStack) DecoderHandler.decode(is);
                if (!output_stack.isEmpty()) {
                    this.bakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(
                            output_stack,
                            this.getWorld(),
                            null
                    );
                    this.transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                            this.bakedModel,
                            ItemCameraTransforms.TransformType.GROUND,
                            false
                    );
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            output_stack = (ItemStack) DecoderHandler.decode(customPacketBuffer);
            this.bakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(
                    output_stack,
                    this.getWorld(),
                    null
            );
            this.transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                    this.bakedModel,
                    ItemCameraTransforms.TransformType.GROUND,
                    false
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SideOnly(Side.CLIENT)
    public IBakedModel getTransformedModel() {
        return this.transformedModel;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public int getMode() {
        return 5;
    }

    @Override
    public ItemStack getItemStack() {
        return this.output_stack;
    }

    @Override
    public TileEntityBlock getEntityBlock() {
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getBakedModel() {
        return bakedModel;
    }

}
