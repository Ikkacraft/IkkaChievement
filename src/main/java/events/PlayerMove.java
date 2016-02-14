package events;

import java.io.IOException;
import java.util.UUID;

import achievements.Achievement;
import achievements.AchievementKillMob;
import core.PluginCore;
import java.util.HashMap;
import java.util.Iterator;
import achievements.LocatedAchievement;
import achievements.AchievementDiscovery;
import org.spongepowered.api.entity.Entity;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.entity.DisplaceEntityEvent;
import ws.WebService;

// Class PlayerMove réagit lorsqu'un joueur modifie sa position ou son orientation
public class PlayerMove extends Event implements EventListener<DisplaceEntityEvent.Move.TargetPlayer> {
    // playerPositions contient la position des joueurs en fonction de leur UUID
    HashMap<UUID, Vector3i> playerPositions = new HashMap<UUID, Vector3i>();
    int stepDid = 0;

    // Constructeur de la class PlayerMove
    // Permet de récupérer PluginCore
    public PlayerMove(PluginCore core) {
        super(core);
    }

    // Récupération de l'évènement lorsqu'un joueur change de position
    public void handle(DisplaceEntityEvent.Move.TargetPlayer event) throws Exception {
        // Récupération de la position du joueur
        String cause = event.getCause().toString();
        Entity targetEntity = event.getTargetEntity();
        Vector3i targetEntityPosition = targetEntity.getLocation().getBlockPosition();

        // Récupération de l'UUID du joueur
        String playerName = getPlayerFromCause(cause);
        UUID playerUUID = core.getGame().getServer().getPlayer(playerName).get().getUniqueId();

        // Nouvelle position du joueur
        Vector3i playerPosition = targetEntityPosition;
        // Si aucune position n'est enregistrée pour le joueur donné, on récupère null
        // sinon on récupère la position qui était stockée et on la remplace par la nouvelle position du joueur
        Vector3i oldPlayerPosition = playerPositions.putIfAbsent(playerUUID, playerPosition);

        // Si le joueur a changé de bloc
        if (!(oldPlayerPosition == null || oldPlayerPosition.equals(playerPosition))) {
            // Achievement panorama
            Iterator itaratorPanorama = this.core.getPanoramas().iterator();
            // On parcoure tous les achievements de type Panorama et l'on vérifie si le joueur est dans le bon monde
            // et à la bonne position. Si oui, on valide l'achievement.
            while (itaratorPanorama.hasNext()) {
                LocatedAchievement achiev = (LocatedAchievement) itaratorPanorama.next();
                if (achiev.getWorld().equals(targetEntity.getWorld().getUniqueId())) {
                    if (achiev.getPosition().equals(targetEntityPosition)) {
                        validAchievement(achiev, cause);
                        break;
                    }
                }
            }

            // Achievement découverte
            Iterator iteratorDiscovery = this.core.getDiscoveries().iterator();
            /* On parcoure tous les achievements de type découverte. Puis on vérifie si le joueur a effectué
            le nombre de pas nécessaire. Si oui, on valide l'achievement */
            while (iteratorDiscovery.hasNext()) {
                AchievementDiscovery achiev = (AchievementDiscovery) iteratorDiscovery.next();
                // Récupérer le nombre nécessaire de pas pour l'achievement
                int stepToDo = achiev.getStepNumber();
                // Récupérer le nombre de pas effectués par le joueur
                int stepDid = getDidStep(achiev, playerName);

                if (stepDid <= stepToDo) {
                    addDidStep(playerUUID.toString(), achiev.getBadgeID(), stepDid);
                    if (stepDid == stepToDo - 1) {
                        validAchievement(achiev, cause);
                        break;
                    }
                }
            }
            // On met à jour la position du joueur dans la liste
            playerPositions.put(playerUUID, playerPosition);
        }
    }

    // Récupérer le nombre de pas effectués par le joueur
    private int getDidStep(AchievementDiscovery achiev, String userName) throws IOException {
        String userUUID = core.getGame().getServer().getPlayer(userName).get().getUniqueId().toString();
        WebService ws = new WebService(core);
        String badges = ws.getUserBadges(userUUID);
        if (badges.contains(achiev.getName())) {
            int indexStart = badges.indexOf(achiev.getName());
            int index = badges.indexOf("remaining", indexStart);
            String remaining = badges.substring(badges.indexOf(":", index)+1, badges.indexOf("}", index));
            int mobKilled = Integer.valueOf(remaining);
            core.broadcastText(String.valueOf(mobKilled));
            return mobKilled;
        }
        return 0;
    }

    // Ajouter un pas pour le joueur
    private void addDidStep(String userUUID, int badgeID, int stepDid) throws IOException {
            WebService ws = new WebService(core);
            ws.updateAchievement(userUUID, badgeID, stepDid+1);
    }
}
