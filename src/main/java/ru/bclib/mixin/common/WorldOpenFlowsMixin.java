package ru.bclib.mixin.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.WorldStem;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.bclib.api.LifeCycleAPI;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.api.dataexchange.DataExchangeAPI;
import ru.bclib.api.datafixer.DataFixerAPI;
import ru.bclib.config.Configs;

@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixin {

    @Shadow @Final private LevelStorageSource levelSource;

    @Shadow protected abstract void doLoadLevel(Screen screen, String levelID, boolean safeMode, boolean canAskForBackup);

    @Inject(method = "loadLevel", cancellable = true, at = @At("HEAD"))
    private void bcl_callFixerOnLoad(Screen screen, String levelID, CallbackInfo ci) {
        DataExchangeAPI.prepareServerside();
        BiomeAPI.prepareNewLevel();

        if (DataFixerAPI.fixData(this.levelSource, levelID, true, (appliedFixes) -> {
            LifeCycleAPI._runBeforeLevelLoad();
            this.doLoadLevel(screen, levelID, false, false);
        })) {
            //cancel call when fix-screen is presented
            ci.cancel();
        }
        else {
            LifeCycleAPI._runBeforeLevelLoad();
            if (Configs.CLIENT_CONFIG.suppressExperimentalDialog()) {
                this.doLoadLevel(screen, levelID, false, false);
                //cancel call as we manually start the level load here
                ci.cancel();
            }
        }
    }

    @Inject(method="createFreshLevel", at=@At("HEAD"))
    public void bcl_createFreshLevel(String levelID,
                                     LevelSettings levelSettings,
                                     RegistryAccess registryAccess,
                                     WorldGenSettings worldGenSettings,
                                     CallbackInfo ci){
        DataExchangeAPI.prepareServerside();
        BiomeAPI.prepareNewLevel();

        DataFixerAPI.initializeWorldData(this.levelSource, levelID, true);
        LifeCycleAPI._runBeforeLevelLoad();
    }

    @Inject(method="createLevelFromExistingSettings", at=@At("HEAD"))
    public void bcl_createLevelFromExistingSettings(LevelStorageSource.LevelStorageAccess levelStorageAccess,
                                     ReloadableServerResources reloadableServerResources,
                                     RegistryAccess.Frozen frozen,
                                     WorldData worldData,
                                     CallbackInfo ci){
        DataExchangeAPI.prepareServerside();
        BiomeAPI.prepareNewLevel();

        DataFixerAPI.initializeWorldData(levelStorageAccess, true);
        LifeCycleAPI._runBeforeLevelLoad();
    }
}
