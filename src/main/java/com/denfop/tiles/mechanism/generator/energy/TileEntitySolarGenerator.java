package com.denfop.tiles.mechanism.generator.energy;

import com.denfop.api.inv.IHasGui;
import com.denfop.container.ContainerSolar;
import com.denfop.gui.GuiSolar;
import net.minecraft.block.material.MapColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.biome.Biome;

public class TileEntitySolarGenerator extends TileEntityBaseGenerator implements IHasGui {

    public boolean rain;
    public boolean noSunWorld;
    public boolean wetBiome;
    public boolean skyIsVisible;
    public boolean sunIsUp;
    private Biome biome;

    public TileEntitySolarGenerator() {
        super(1.0, 1, 32);
    }

    protected void onLoaded() {
        super.onLoaded();
        this.biome = this.world.getBiome(this.pos);
        this.noSunWorld = this.world.provider.isNether();
        this.updateSunVisibility();
    }

    public boolean gainEnergy() {
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            this.updateSunVisibility();
        }

        if (this.sunIsUp && skyIsVisible && !this.rain) {
            this.energy.addEnergy(1);
            return true;
        } else {
            return false;
        }
    }

    public boolean gainFuel() {
        return false;
    }

    public void updateSunVisibility() {
        this.wetBiome = this.biome.getRainfall() > 0.0F;
        this.rain = this.wetBiome && (this.world.isRaining() || this.world.isThundering());
        this.sunIsUp = this.world.isDaytime();
        this.skyIsVisible = this.world.canBlockSeeSky(this.pos.up()) &&
                (this.world.getBlockState(this.pos.up()).getMaterial().getMaterialMapColor() ==
                        MapColor.AIR) && !this.noSunWorld;
    }


    public boolean needsFuel() {
        return false;
    }

    @Override
    public ContainerSolar getGuiContainer(final EntityPlayer player) {
        return new ContainerSolar(this, player);
    }

    @Override
    public GuiSolar getGui(final EntityPlayer player, final boolean isAdmin) {
        return new GuiSolar(getGuiContainer(player));
    }

    protected boolean delayActiveUpdate() {
        return true;
    }

}
