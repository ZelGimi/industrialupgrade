package com.denfop.render.panel;

import com.denfop.Constants;
import com.denfop.render.transport.ModelBaseCable;
import com.denfop.tiles.panels.entity.TileSolarPanel;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TileEntitySolarPanelRender<T extends TileSolarPanel> extends TileEntitySpecialRenderer<T> {

    public static final ResourceLocation texture = new ResourceLocation(
            Constants.TEXTURES,
            "textures/models/pollution.png"
    );
    private Map<BlockPos,DataPollution> entries = new HashMap<>();

    public void render(
            @Nonnull TileSolarPanel te,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {


        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        this.bindTexture(texture);
        DataPollution dataPollution = entries.get(te.getBlockPos());
        if(dataPollution == null){
            dataPollution = new DataPollution(te.timer.getIndexWork(), new PollutionModel(te.getWorld().rand,
                    te.timer.getIndexWork()));
            entries.put(te.getBlockPos(),dataPollution);
        }

        if(dataPollution.getIndex() != te.timer.getIndexWork()){
            dataPollution.setIndex( te.timer.getIndexWork());
            dataPollution.setModel(null);
        }
        if(dataPollution.getModel() == null){
            dataPollution.setModel(new PollutionModel(te.getWorld().rand,
                    te.timer.getIndexWork()));
        }
        dataPollution.getModel().render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1F);
        GlStateManager.popMatrix();

    }
}
