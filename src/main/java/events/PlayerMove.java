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

// Class PlayerMove r�agit lorsqu'un joueur modifie sa position ou son orientation
public class PlayerMove extends Event implements EventListener<DisplaceEntityEvent.Move.TargetPlayer> {
    // playerPositions contient la position des joueurs en fonction de leur UUID
    HashMap<UUID, Vector3i> playerPositions = new HashMap<UUID, Vector3i>();
    int stepDid = 0;

    // Constructeur de la class PlayerMove
    // Permet de r�cup�rer PluginCore
    public PlayerMove(PluginCore core) {
        super(core);
    }

    // R�cup�ration de l'�v�nement lorsqu'un joueur change de position
    public void handle(DisplaceEntityEvent.Move.TargetPlayer event) throws Exception {
        // R�cup�ration de la position du joueur
        String cause = event.getCause().toString();
        Entity targetEntity = event.getTargetEntity();
        Vector3i targetEntityPosition = targetEntity.getLocation().getBlockPosition();

        // R�cup�ration de l'UUID du joueur
        String playerName = getPlayerFromCause(cause);
        UUID playerUUID = core.getGame().getServer().getPlayer(playerName).get().getUniqueId();

        // Nouvelle position du joueur
        Vector3i playerPosition = targetEntityPosition;
        // Si aucune position n'est enregistr�e pour le joueur donn�, on r�cup�re null
        // sinon on r�cup�re la position qui �tait stock�e et on la remplace par la nouvelle position du joueur
        Vector3i oldPlayerPosition = playerPositions.putIfAbsent(playerUUID, playerPosition);

        // Si le joueur a chang� de bloc
        if (!(oldPlayerPosition == null || oldPlayerPosition.equals(playerPosition))) {
            // Achievement panorama
            Iterator itaratorPanorama = this.core.getPanoramas().iterator();
            // On parcoure tous les achievements de type Panorama et l'on v�rifie si le joueur est dans le bon monde
            // et � la bonne position. Si oui, on valide l'achievement.
            while (itaratorPanorama.hasNext()) {
                LocatedAchievement achiev = (LocatedAchievement) itaratorPanorama.next();
                if (achiev.getWorld().equals(targetEntity.getWorld().getUniqueId())) {
                    if (achiev.getPosition().equals(targetEntityPosition)) {
                        validAchievement(achiev, cause);
                        break;
                    }
                }
            }

            // Achievement d�couverte
            Iterator iteratorDiscovery = this.core.getDiscoveries().iterator();
            /* On parcoure tous les achievements de type d�couverte. Puis on v�rifie si le joueur a effectu�
            le nombre de pas n�cessaire. Si oui, on valide l'achievement */
            while (iteratorDiscovery.hasNext()) {
                AchievementDiscovery achiev = (AchievementDiscovery) iteratorDiscovery.next();
                // R�cup�rer le nombre n�cessaire de pas pour l'achievement
                int stepToDo = achiev.getStepNumber();
                // R�cup�rer le nombre de pas effectu�s par le joueur
                int stepDid = getDidStep(achiev, playerName);

                if (stepDid <= stepToDo) {
                    addDidStep(playerUUID.toString(), achiev.getBadgeID(), stepDid);
                    if (stepDid == stepToDo - 1) {
                        validAchievement(achiev, cause);
                        break;
                    }
                }
            }
            // On met � jour la position du joueur dans la liste
            playerPositions.put(playerUUID, playerPosition);
        }
    }

    // R�cup�rer le nombre de pas effectu�s par le joueur
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
