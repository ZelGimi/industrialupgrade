package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerAdditionGenStone;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.mechanism.TileBaseAdditionGenStone;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiAdditionGenStone extends GuiIU<ContainerAdditionGenStone> {

    public final ContainerAdditionGenStone container;

    public GuiAdditionGenStone(ContainerAdditionGenStone container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 10, 36, EnumTypeComponent.ENERGY,
                new Component<>(container.base.energy)
        ));
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 62 && x <= 79 && y >= 63 && y <= 80) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);

        new Area(this, 63, 64, 18, 18)
                .withTooltip(Localization.translate("message.text.mode") + ": " +
                        (this.container.base.getMode() == TileBaseAdditionGenStone.Mode.DIORITE ?
                                this.container.base.diorite.getDisplayName() :
                                this.container.base.getMode() == TileBaseAdditionGenStone.Mode.ANDESITE
                                        ?
                                        this.container.base.andesite.getDisplayName()
                                        : this.container.base.granite.getDisplayName()
                        ))
                .drawForeground(mouseX
                        , mouseY);
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        this.zLevel = 100.0F;
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (this.container.base.output == null) {
            itemRender.renderItemAndEffectIntoGUI(container.base.granite, xoffset + 64,
                    yoffset + 28
            );
        } else {
            switch (this.container.base.getMode()) {
                case GRANITE:
                    itemRender.renderItemAndEffectIntoGUI(container.base.granite, xoffset + 64,
                            yoffset + 28
                    );
                    break;
                case DIORITE:
                    itemRender.renderItemAndEffectIntoGUI(container.base.diorite, xoffset + 64,
                            yoffset + 28
                    );
                    break;
                case ANDESITE:
                    itemRender.renderItemAndEffectIntoGUI(container.base.andesite, xoffset + 64,
                            yoffset + 28
                    );
                    break;
            }
        }

        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();

        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glColor4f(0.1F, 1, 0.1F, 1);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        this.zLevel = 100.0F;
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        switch (this.container.base.getMode()) {
            case GRANITE:
                itemRender.renderItemAndEffectIntoGUI(container.base.granite, xoffset + 63,
                        yoffset + 64
                );
                break;
            case DIORITE:
                itemRender.renderItemAndEffectIntoGUI(container.base.diorite, xoffset + 63,
                        yoffset + 64
                );
                break;
            case ANDESITE:
                itemRender.renderItemAndEffectIntoGUI(container.base.andesite, xoffset + 63,
                        yoffset + 64
                );
                break;
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();

        RenderHelper.enableStandardItemLighting();
        GL11.glColor4f(0.1F, 1, 0.1F, 1);
        GL11.glPopMatrix();
        GL11.glColor4f(1F, 1, 1F, 1);
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiGenStone.png");
    }

}
