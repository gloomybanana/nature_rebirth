package org.gloomybanana.nature_rebirth;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = NatureRebirth.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = NatureRebirth.MOD_ID, value = Dist.CLIENT)
public class NatureRebirthClient {
    public NatureRebirthClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
