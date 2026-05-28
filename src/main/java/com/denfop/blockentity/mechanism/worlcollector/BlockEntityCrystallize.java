package com.denfop.blockentity.mechanism.worlcollector;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.base.BlockEntityBaseWorldCollector;
import com.denfop.blockentity.base.EnumTypeCollector;
import com.denfop.blockentity.base.IIsMolecular;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.ClientHooks;

import java.io.IOException;

import static net.minecraft.world.item.ItemDisplayContext.GROUND;

public class BlockEntityCrystallize extends BlockEntityBaseWorldCollector implements IIsMolecular {

    protected ItemStack output_stack;
    @OnlyIn(Dist.CLIENT)
    private BakedModel bakedModel;
    @OnlyIn(Dist.CLIENT)
    private BakedModel transformedModel;

    public BlockEntityCrystallize(BlockPos pos, BlockState state) {
        super(EnumTypeCollector.DEFAULT, BlockBaseMachine3Entity.crystallize, pos, state);
    }

    public void init() {

        addRecipe(IUItem.diamondDust, 0.125, new ItemStack(Items.DIAMOND));


        addRecipe(IUItem.lapiDust, 0.0625, new ItemStack(Items.LAPIS_LAZULI));

        addRecipe(new ItemStack(Items.DIAMOND), 0.25, new ItemStack(Items.EMERALD));
        addRecipe(new ItemStack(Items.EMERALD), 0.25, new ItemStack(Items.DIAMOND));

        addRecipe(new ItemStack(IUItem.preciousgem.getStack(0)), 0.25, new ItemStack(Items.EMERALD));
        addRecipe(new ItemStack(IUItem.preciousgem.getStack(1)), 0.25, new ItemStack(Items.EMERALD));

        addRecipe(IUItem.iridiumOre, 20, new ItemStack(IUItem.iuingot.getStack(17)));


        addRecipe(new ItemStack(Items.GOLDEN_APPLE), 4, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));

        addRecipe(new ItemStack(Items.ROTTEN_FLESH), 4, new ItemStack(Items.PORKCHOP));
        addRecipe(new ItemStack(Items.SPIDER_EYE), 4, new ItemStack(Items.BEEF));
        addRecipe(new ItemStack(Items.STRING), 20, new ItemStack(Blocks.COBWEB));
        addRecipe(IUItem.latex, 10, new ItemStack(Items.SLIME_BALL));

        addRecipe("c:ores/Diamond", 4, new ItemStack(Items.DIAMOND, 8));
        addRecipe("c:ores/Coal", 4, new ItemStack(Items.COAL, 10));

        addRecipe("c:ores/emerald", 4, new ItemStack(Items.EMERALD, 8));
        addRecipe("c:ores/Quartz", 4, new ItemStack(Items.QUARTZ, 8));
        addRecipe("c:ores/Lapis", 4, new ItemStack(Items.LAPIS_LAZULI, 10));
        addRecipe("c:ores/Sulfur", 4, new ItemStack(IUItem.iudust.getStack(31), 8));
        addRecipe("c:ores/Redstone", 4, new ItemStack(Items.REDSTONE, 10));


        addRecipe("c:ores/ruby", 4, new ItemStack(IUItem.preciousgem.getStack(0), 8));
        addRecipe("c:ores/sapphire", 4, new ItemStack(IUItem.preciousgem.getStack(1), 8));
        addRecipe("c:ores/topaz", 4, new ItemStack(IUItem.preciousgem.getStack(2), 8));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.crystallize;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
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
                    this.bakedModel = Minecraft.getInstance().getItemRenderer().getModel(
                            output_stack,
                            this.getWorld(),
                            null, 0
                    );
                    this.transformedModel = ClientHooks.handleCameraTransforms(new PoseStack(),
                            this.bakedModel,
                            GROUND,
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
            this.bakedModel = Minecraft.getInstance().getItemRenderer().getModel(
                    output_stack,
                    this.getWorld(),
                    null, 0
            );
            this.transformedModel = ClientHooks.handleCameraTransforms(new PoseStack(),
                    this.bakedModel,
                    GROUND,
                    false
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public BakedModel getTransformedModel() {
        return this.transformedModel;
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
    public BlockEntityBase getEntityBlock() {
        return this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BakedModel getBakedModel() {
        return bakedModel;
    }

}
