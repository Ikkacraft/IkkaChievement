package events;

import org.spongepowered.api.text.Text;
import pluginCore.PluginCore;

import java.io.IOException;
import java.util.Iterator;
import achievements.AchievementKillMob;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import ws.WebService;

// Class KillMov réagit lorsqu'une entité meurt
public class KillMob extends Event implements EventListener<DestructEntityEvent.Death> {

    // Constructeur de la class KillMob
    // Permet de récupérer PluginCore
    public KillMob(PluginCore core) {
        super(core);
    }

    // Récupération de l'évènement lorsqu'une entité meurt
    public void handle(DestructEntityEvent.Death event) throws Exception {
            String cause = event.getCause().toString();

            // Si la mort a été causée par un joueur grâce à une arme
            if(cause.contains("Name=player, Type=attack")) {
                // On récupère l'id de l'entité morte
                String mobId = event.getTargetEntity().getType().getId();

                // Achievement KillMob
                Iterator iterator = this.core.getMobs().iterator();
                // On parcoure la liste des achievements de type KillMob et on vérifie si l'id de l'entité tuée
                // correspond à l'id enregistrée dans l'achievement
                // Ensuite on vérifie que le bon nombre d'entité de ce type ont été tuées
                // Si oui, on valide l'achievement
                while (iterator.hasNext()) {
                    AchievementKillMob achiev = (AchievementKillMob) iterator.next();
                    if (mobId.equals(achiev.getMobId())) {

                        String causePlayer = getPlayerFromCause(event.getCause().toString());
                        String userUUID = core.getGame().getServer().getPlayer(causePlayer).get().getUniqueId().toString();
                        if(!playerHasAchievment(userUUID, achiev.getName())) {
                            // Récupérer le nombre de mobs tués par le joueur
                            int mobToKill = getMobToKill(achiev, causePlayer);
                            addKilledMob(userUUID, achiev.getBadgeID(), mobToKill-1);
                            if(mobToKill == 0) {
                                validAchievement(achiev, cause);
                                break;
                            }
                        }
                    }
                }
            }
        }

        // Permet de récupérer le nombre de mobs tués
        private int getMobToKill(AchievementKillMob achiev, String userName) throws IOException {
            String userUUID = core.getGame().getServer().getPlayer(userName).get().getUniqueId().toString();
            WebService ws = new WebService(core);
            String badges = ws.getUserBadges(userUUID);
            if (badges.contains(achiev.getName())) {
                int indexStart = badges.indexOf(achiev.getName());
                int index = badges.indexOf("remaining", indexStart);
                String remaining = badges.substring(badges.indexOf(":", index)+1, badges.indexOf("}", index));
                int mobToKill = Integer.valueOf(remaining);
                return mobToKill;
            }
            return 0;
        }

        // Ajoute un monstre tué au compteur du joueur
        private void addKilledMob(String userUUID, int badgeID, int mobKilled) throws IOException {
            WebService ws = new WebService(core);
            ws.updateAchievement(userUUID, badgeID, mobKilled);
        }
}
