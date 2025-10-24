package net.xiaoyu.target_lock.client;

import net.xiaoyu.target_lock.TargetLock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = TargetLock.MOD_ID)
public class ClientSetupHandler {
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            TargetLockHandler.init();
        });
    }
}