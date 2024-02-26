package com.asylumdev.immersivecowardice;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = ImmersiveCowardice.MODID, name = ImmersiveCowardice.MODNAME, version = ImmersiveCowardice.VERSION,
     dependencies = "required-after:immersiveengineering@[0.12-98,);")
public class ImmersiveCowardice {
    public static final String MODID = "immersivecowardice";
    public static final String MODNAME = "ImmersiveCowardice";
    public static final  String VERSION = "2.0";

    public static Configuration config;

    public static final Logger LOGGER = LogManager.getLogger();

    public ImmersiveCowardice() {

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ArcFurnaceHandler());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "immersivecowardice.cfg"));
        CommonConfig.readConfig();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
    }


}
