package pluginCore;

import achievements.Achievement;
import achievements.AchievementChest;
import achievements.AchievementDiscovery;
import achievements.AchievementKillMob;
import achievements.AchievementPanorama;
import achievements.LocatedAchievement;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowpowered.math.vector.Vector3i;
import com.google.inject.Inject;
import commands.WorldUUID;
import events.KillMob;
import events.OpenChest;
import events.PlayerMove;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.DisplaceEntityEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import ws.Badge;
import ws.WebService;

@Plugin(id="IkkaChievement", name="IkkaChievement", version="1.1")
public class PluginCore {
    @Inject
    private Game game;
    private Logger logger;
    private ArrayList<LocatedAchievement> panoramas = new ArrayList();
    private ArrayList<LocatedAchievement> chests = new ArrayList();
    private ArrayList<Achievement> mobs = new ArrayList();
    private ArrayList<Achievement> discoveries = new ArrayList();

    @Listener
    public void onServerStart(GameStartedServerEvent event) throws IOException {
        this.getLogger().info("Debut du chargement");
        this.getLogger().info("Recuperation de tous les achievements");
        this.getAllAchievements();
        this.getLogger().info("Enregistrement des evenements et des commandes");
        this.registerEvents();
        this.registerCommands();
        this.getLogger().info("Chargement termine");
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

    public ArrayList<LocatedAchievement> getPanoramas() {
        return this.panoramas;
    }

    public ArrayList<LocatedAchievement> getChests() {
        return this.chests;
    }

    public ArrayList<Achievement> getMobs() {
        return this.mobs;
    }

    public ArrayList<Achievement> getDiscoveries() {
        return this.discoveries;
    }

    public void broadcastText(String text) {
        this.getGame().getServer().getBroadcastChannel().send(Text.of(text));
    }

    private void getAllAchievements() throws IOException {
        WebService ws = new WebService(this);
        List<Badge> badges = ws.getBadges();
        for (Badge badge : badges) {
            String worlduuid;
            int z;
            int y;
            Vector3i pos;
            int x;
            int cat = badge.getCategory_id();
            String parameters = badge.getParameters();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(parameters);
            if (cat == 1) {
                x = root.get("x").asInt();
                y = root.get("y").asInt();
                z = root.get("z").asInt();
                worlduuid = root.get("world").asText();
                pos = new Vector3i(x, y, z);
                this.chests.add(new AchievementChest(badge.getTitle(), badge.getDescription(), pos,
                        UUID.fromString(worlduuid), badge.getUrlImage(), badge.getBadge_id()));
            }
            else if (cat == 2) {
                x = root.get("x").asInt();
                y = root.get("y").asInt();
                z = root.get("z").asInt();
                worlduuid = root.get("world").asText();
                pos = new Vector3i(x, y, z);
                this.panoramas.add(new AchievementPanorama(badge.getTitle(), badge.getDescription(), pos,
                        UUID.fromString(worlduuid), badge.getUrlImage(), badge.getBadge_id()));
            }
            else if (cat == 3) {
                int mobs = root.get("mobNumber").asInt();
                String mobId = root.get("mobID").asText();
                this.mobs.add(new AchievementKillMob(badge.getTitle(), badge.getDescription(), mobId, mobs,
                        badge.getUrlImage(), badge.getBadge_id()));
            }
            else {
                int stepNumber = root.get("stepNumber").asInt();
                this.discoveries.add(new AchievementDiscovery(badge.getTitle(), badge.getDescription(), stepNumber,
                        badge.getUrlImage(), badge.getBadge_id()));
            }
        }
    }

    private void registerEvents() {
        PlayerMove listenerPlayerMove = new PlayerMove(this);
        Sponge.getEventManager().registerListener(this, (Class)DisplaceEntityEvent.Move.TargetPlayer.class, listenerPlayerMove);
        OpenChest listenerOpenChest = new OpenChest(this);
        Sponge.getEventManager().registerListener(this, (Class)InteractBlockEvent.Secondary.class, listenerOpenChest);
        KillMob listenerKillMob = new KillMob(this);
        Sponge.getEventManager().registerListener(this, (Class)DestructEntityEvent.Death.class, listenerKillMob);
    }

    private void registerCommands() {
        CommandSpec worldUUID = CommandSpec.builder().
                description(Text.of("Get World UUID Command")).
                permission("ikkachievement.command.getworlduuid").
                executor(new WorldUUID(this)).
                build();
        Sponge.getCommandManager().register(this, worldUUID, new String[]{"worlduuid"});
    }
}
