package core;

import commands.WorldUUID;
import events.*;
import achievements.*;

import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import java.util.ArrayList;
import com.google.inject.Inject;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.event.Listener;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.DisplaceEntityEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import ws.WebService;

import javax.xml.bind.JAXBException;

@Plugin(
        id = "IkkaChievement",
        name = "IkkaChievement",
        version = "1.0"
)
// La class PluginCore permet d'initialiser le plugin mais permet aussi la gestion des différents évènements
public class PluginCore {
    @Inject
    private Game game;
    private Logger logger;
    // Liste de tous les achievements
    private ArrayList<LocatedAchievement> panoramas = new ArrayList<LocatedAchievement>();
    private ArrayList<LocatedAchievement> chests = new ArrayList<LocatedAchievement>();
    private ArrayList<Achievement> mobs = new ArrayList<Achievement>();
    private ArrayList<Achievement> discoveries = new ArrayList<Achievement>();

    @Listener
    // Lorsque le serveur minecraft est lancé, on initialise le plugin
    public void onServerStart(GameStartedServerEvent event) {
        // Récupération de tous les achievements
        UUID ikkaWorldUUID = getGame().getServer().getWorld("IkkaWorld").get().getUniqueId();
        this.panoramas.add(new AchievementPanorama("EssaiP", "desc1", new Vector3i(5, 4, 10), ikkaWorldUUID));
        this.chests.add(new AchievementChest("EssaiC", "desc2", new Vector3i(3, 4, 10), ikkaWorldUUID));
        this.mobs.add(new AchievementKillMob("EssaiM", "desc3", "minecraft:zombie", 5));
        this.discoveries.add(new AchievementDiscovery("EssaiD", "desc4", 10));

        this.getLogger().info("IkkaChievement a recu l'instruction start");

        /* Enregistrement des events */
        // Evènement PlayerMove qui permet les achievements Panorama et Découverte
        EventListener<DisplaceEntityEvent.Move.TargetPlayer> listenerPlayerMove = new PlayerMove(this);
        Sponge.getEventManager().registerListener(this, DisplaceEntityEvent.Move.TargetPlayer.class, listenerPlayerMove);

        // Evènement OpenChest qui permet l'achievement coffre caché
        EventListener<InteractBlockEvent.Secondary> listenerOpenChest = new OpenChest(this);
        Sponge.getEventManager().registerListener(this, InteractBlockEvent.Secondary.class, listenerOpenChest);

        // Evènement KillMob qui permet l'achievement KillMob
        EventListener<DestructEntityEvent.Death> listenerKillMob = new KillMob(this);
        Sponge.getEventManager().registerListener(this, DestructEntityEvent.Death.class, listenerKillMob);

        /* Enregistrement des commandes */

        CommandSpec worldUUID = CommandSpec.builder()
                .description(Text.of("Get World UUID Command"))
                .permission("myplugin.command.getworlduuid")
                .executor(new WorldUUID(this))
                .build();
        Sponge.getCommandManager().register(this, worldUUID, "worlduuid");

        WebService test = new WebService(this);
        Post post = null;
        post = (Post) test.get("posts", Post.class);
        this.getLogger().info(post.toString());
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

    // Récupérer la liste des achievements de type panorama
    public ArrayList<LocatedAchievement> getPanoramas() {
        return this.panoramas;
    }

    // Récupérer la liste des achievements de type coffre caché
    public ArrayList<LocatedAchievement> getChests() {
        return this.chests;
    }

    // Récupérer la liste des achievements de type KillMob
    public ArrayList<Achievement> getMobs() { return this.mobs; }

    // Récupérer la liste des achievements de type Découverte
    public ArrayList<Achievement> getDiscoveries() { return this.discoveries; }

    // Permet d'envoyer un message à tous les joueurs du serveur
    public void broadcastText(String text) {
        getGame().getServer().getBroadcastChannel().send(Text.of(text));
    }
}

class Post{
    private int userId;
    private int id;
    private String title;
    private String body;
}