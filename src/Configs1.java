// 
// Decompiled by Procyon v0.5.36
// 

package com.Denfop.ssp;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.Denfop.ssp.tiles.TileEntitySpectral;
import com.Denfop.ssp.tiles.TileEntitySingular;
import com.Denfop.ssp.tiles.TileEntityphotonic;
import com.Denfop.ssp.tiles.TileEntityAdmin;
import com.chocohead.advsolar.items.ItemArmourSolarHelmet;
import com.chocohead.advsolar.renders.PrettyMolecularTransformerTESR;
import com.chocohead.advsolar.tiles.TileEntityQuantumGenerator;
import com.chocohead.advsolar.tiles.TileEntitySolarPanel;
import net.minecraftforge.common.config.Configuration;
import java.text.ParseException;
import java.io.File;

final class Configs1
{
    private static final String GENERAL = "general";
    private static final String SOLARS = "solars";
    private static final String QUANTUM_GENERATOR = "quantum generator";
    private static final String CRAFTING = "recipes settings";
    static boolean hardRecipes;
    static boolean easyASPRecipe;
    static boolean canCraftDoubleSlabs;
    static boolean canCraftMT;
    static boolean canCraftASP;
    static boolean canCraftHSP;
    static boolean canCraftUHSP;
    static boolean canCraftQSP;
    static boolean canCraftASH;
    static boolean canCraftHSH;
    static boolean canCraftUHSH;
    private static final String NEW_LINE;
    private static final String CONFIG_VERSION = "2.0";
    
    
    static void loadConfig(final File config, final boolean client) {
        SuperSolarPanels.log.info("Loading ASP Config from " + config.getAbsolutePath());
        loadNormalConfig(config, client);
        
        
    }
    
    private static void loadNormalConfig(final File configFile, final boolean client) {
        final Configuration config = new Configuration(configFile);
        try {
            config.load();
            TileEntitySpectral.settings = new TileEntitySolarPanel.SolarConfig(config.get("solars", "SpecrtalGenDay", 32768).getInt(32768), config.get("solars", "SpecrtalGenNight", 20000).getInt(20000), config.get("solars", "SpecrtalStorage", 100000000).getInt(100000000), config.get("solars", "SpecrtalTier", 6).getInt(6));
            TileEntitySingular.settings = new TileEntitySolarPanel.SolarConfig(config.get("solars", "SingularGenDay", 262144).getInt(262144), config.get("solars", "SingularGenNight", 196608).getInt(196608), config.get("solars", "SingularStorage", 1000000000).getInt(100000), config.get("solars", "SingularTier", 7).getInt(7));
            TileEntityAdmin.settings = new TileEntitySolarPanel.SolarConfig(config.get("solars", "AdminGenDay", 1048576).getInt(1048576), config.get("solars", "AdminGenNight", 1048576).getInt(1048576), config.get("solars", "AdminStorage", 1000000000).getInt(1000000), config.get("solars", "AdminPTier", 8).getInt(8));
            TileEntityphotonic.settings = new TileEntitySolarPanel.SolarConfig(config.get("solars", "PhotonicGenDay", 1000000000).getInt(1000000000), config.get("solars", "PhotonicGenNight", 1000000000).getInt(1000000000), config.get("solars", "PhotonicStorage", 999999999).getInt(999999999), config.get("solars", "PhotonicTier", 9).getInt(9));
            
        }
        catch (Exception e) {
            SuperSolarPanels.log.fatal("Fatal error reading config file.", (Throwable)e);
            throw new RuntimeException(e);
        }
        finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
    
   
  
    private static void write(final BufferedWriter writer, final String line) throws IOException {
        writer.write(line);
        writer.newLine();
    }
    
    static {
        NEW_LINE = System.getProperty("line.separator");
    }
}
