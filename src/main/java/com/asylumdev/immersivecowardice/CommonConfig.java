package com.asylumdev.immersivecowardice;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

//Server config is currently defunct, so everything shall be common.
public class CommonConfig {
    public static CommonConfig INSTANCE;

    private static final String CATEGORY_GENERAL = "general";

    public static boolean arcFurnaceEnable = true;

    public static void readConfig() {
        Configuration cfg = ImmersiveCowardice.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
        } catch (Exception e1) {
            ImmersiveCowardice.LOGGER.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General Configuration");
        arcFurnaceEnable = cfg.getBoolean("arcFurnaceEnable",CATEGORY_GENERAL,arcFurnaceEnable, "Whether Arc Furnace should allow electrodes to be piped in.");
    }





}
