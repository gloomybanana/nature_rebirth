package org.gloomybanana.nature_rebirth;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(NatureRebirth.MOD_ID)
public class NatureRebirth {
    public static final String MOD_ID = "nature_rebirth";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NatureRebirth(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Nature Rebirth loaded!");
    }
}
