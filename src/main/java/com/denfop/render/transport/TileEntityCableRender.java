package com.denfop.render.transport;

import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TileEntityCableRender<T extends TileEntityMultiCable> extends TileEntitySpecialRenderer<T> {

    public static List<ModelBaseCable> modelBaseCableMap = new LinkedList<>();
    public static ModelBaseCable standard = new ModelBaseCable(null);
    private ResourceLocation textures;

    public static <T> List<T> reverseOrder(List<T> list) {
        int size = list.size();
        return list.stream()
                .sorted((a, b) -> -list.indexOf(a) + list.indexOf(b))
                .collect(Collectors.toList());
    }

    public void render(
            @Nonnull TileEntityMultiCable te,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {

        this.textures = te.getTexture();

        if (modelBaseCableMap.isEmpty()) {
            final List<EnumFacing> var4 = reverseOrder(Arrays.stream(EnumFacing.VALUES).collect(Collectors.toList()));
            for (EnumFacing dir : var4) {
                modelBaseCableMap.add(new ModelBaseCable(dir));
            }
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        this.bindTexture(this.textures);
        standard.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1F);
        byte connect = te.connectivity;
        for (ModelBaseCable entry : modelBaseCableMap) {
            if ((connect & 1) == 1) {
                entry.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1F);
            }
            connect = (byte) (connect >> 1);
        }
        GlStateManager.popMatrix();

    }


}
