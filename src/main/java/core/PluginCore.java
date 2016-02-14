package core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import commands.WorldUUID;
import events.*;
import achievements.*;

import java.io.IOException;
import java.util.List;
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
import ws.Badge;
import ws.WebService;

import javax.xml.bind.JAXBException;

@Plugin(
        id = "IkkaChievement",
        name = "IkkaChievement",
        version = "1.1"
)
// La class PluginCore permet d'initialiser le plugin mais permet aussi la gestion des différents évènements
public class PluginCore {
    @Inject
    private Game game;
    private Logger logger;
    // Liste de tous les achievements
    private ArrayList<LocatedAchievement> panoramas = new ArrayList<>();
    private ArrayList<LocatedAchievement> chests = new ArrayList<>();
    private ArrayList<Achievement> mobs = new ArrayList<>();
    private ArrayList<Achievement> discoveries = new ArrayList<>();

    @Listener
    // Lorsque le serveur minecraft est lancé, on initialise le plugin
    public void onServerStart(GameStartedServerEvent event) throws IOException {
        this.getLogger().info("Début du chargement");
        this.getLogger().info("Récupération de tous les achievements");
        getAllAchievements();
        this.getLogger().info("Enregistrement des évènements et des commandes");
        registerEvents();
        registerCommands();

        this.getLogger().info("Chargement terminé");
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

    private void getAllAchievements() throws IOException {
        WebService test = new WebService(this);
        List<Badge> badges = test.getBadges();
        for(Badge badge : badges) {
            int cat = badge.getCategory_id();
            String parameters = badge.getParameters();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(parameters);
            if(cat == 1) {
                int x = root.get("x").asInt();
                int y = root.get("y").asInt();
                int z = root.get("z").asInt();
                String worlduuid = root.get("world").asText();
                Vector3i pos = new Vector3i(x, y, z);
                chests.add(new AchievementChest(badge.getTitle(), badge.getDescription(), pos,
                        UUID.fromString(worlduuid), badge.getUrlImage(), badge.getBadge_id()));
            } else if(cat == 2) {
                int x = root.get("x").asInt();
                int y = root.get("y").asInt();
                int z = root.get("z").asInt();
                String worlduuid = root.get("world").asText();
                Vector3i pos = new Vector3i(x, y, z);
                panoramas.add(new AchievementPanorama(badge.getTitle(), badge.getDescription(), pos,
                        UUID.fromString(worlduuid), badge.getUrlImage(), badge.getBadge_id()));
            } else if(cat == 3) {
                int mobs = root.get("mobNumber").asInt();
                String mobId = root.get("mobID").asText();
                this.mobs.add(new AchievementKillMob(badge.getTitle(), badge.getDescription(), mobId,
                        mobs, badge.getUrlImage(), badge.getBadge_id()));
            } else {
                int stepNumber = root.get("stepNumber").asInt();
                discoveries.add(new AchievementDiscovery(badge.getTitle(), badge.getDescription(), stepNumber,
                        badge.getUrlImage(), badge.getBadge_id()));
            }
        }
    }

    private void registerEvents() {
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
    }

    private void registerCommands() {
        /* Enregistrement des commandes */
        CommandSpec worldUUID = CommandSpec.builder()
                .description(Text.of("Get World UUID Command"))
                .permission("ikkachievement.command.getworlduuid")
                .executor(new WorldUUID(this))
                .build();
        Sponge.getCommandManager().register(this, worldUUID, "worlduuid");
    }
}