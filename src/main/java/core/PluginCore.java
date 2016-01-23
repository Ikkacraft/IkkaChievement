//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package core;

import achievements.Achievement;
import achievements.AchievementChest;
import achievements.AchievementPanorama;
import com.flowpowered.math.vector.Vector3i;
import com.google.inject.Inject;
import commands.InfosCommand;
import events.PlayerMove;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "firstplugin",
        name = "IkkaExample",
        version = "1.0"
)
public class PluginCore {
    @Inject
    private Game game;
    private Logger logger;
    private ArrayList<Achievement> panoramas = new ArrayList();
    private ArrayList<Achievement> chests = new ArrayList();

    @Subscribe
    public void onServerStart(ServerStartedEvent event) {
        this.panoramas.add(new AchievementPanorama("EssaiP", new Vector3i(0, 0, 0), "world"));
        this.chests.add(new AchievementChest("EssaiC", new Vector3i(0, 0, 0), "world"));
        this.getLogger().info("IkkaExample a re?u l\'instruction start");
    }

    @Inject
    public PluginCore(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public Game getGame() {
        return this.game;
    }

    public ArrayList<Achievement> getPanoramas() {
        return this.panoramas;
    }

    public ArrayList<Achievement> getChests() {
        return this.chests;
    }

    @Subscribe
    public void preInit(PreInitializationEvent event) {
        this.game = event.getGame();
        this.game.getCommandDispatcher().register(this, new InfosCommand(this.game), new String[]{"ikka"});
        this.game.getEventManager().register(this, new PlayerMove(this));
    }
}
