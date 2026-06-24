package org.gloomybanana.nature_rebirth;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.ModList;

@Mod(NatureRebirth.MOD_ID)
public class NatureRebirth {
    public static final String MOD_ID = "nature_rebirth";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NatureRebirth(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        // 注册配方序列化器
        ModRecipes.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Nature Rebirth loaded!");
        
        // 检测 craton 模组
        if (ModList.get().isLoaded("craton")) {
            LOGGER.info("========================================");
            LOGGER.info("  Hello Craton! This is Nature Rebirth~");
            LOGGER.info("  Craton blocks conversion rules added!");
            LOGGER.info("========================================");
        }
        
        // 检测 mekanism 模组
        if (ModList.get().isLoaded("mekanism")) {
            LOGGER.info("========================================");
            LOGGER.info("  Hello Mekanism! This is Nature Rebirth~");
            LOGGER.info("  Mekanism ores have been integrated!");
            LOGGER.info("========================================");
        }
        
        // 打印配置值以调试
        var stoneOreList = Config.STONE_ORE_CUSTOM_LIST.get();
        LOGGER.info("Stone ore config: " + stoneOreList);
        
        var deepslateOreList = Config.DEEPSLATE_ORE_CUSTOM_LIST.get();
        LOGGER.info("Deepslate ore config: " + deepslateOreList);
        
        var netherOreList = Config.NETHER_ORE_CUSTOM_LIST.get();
        LOGGER.info("Nether ore config: " + netherOreList);
    }
    
    public static Identifier location(String path) {
        return Identifier.parse(MOD_ID + ":" + path);
    }
}
