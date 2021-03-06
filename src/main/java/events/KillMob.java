package events;

import org.spongepowered.api.text.Text;
import pluginCore.PluginCore;

import java.io.IOException;
import java.util.Iterator;
import achievements.AchievementKillMob;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import ws.WebService;

// Class KillMov r�agit lorsqu'une entit� meurt
public class KillMob extends Event implements EventListener<DestructEntityEvent.Death> {

    // Constructeur de la class KillMob
    // Permet de r�cup�rer PluginCore
    public KillMob(PluginCore core) {
        super(core);
    }

    // R�cup�ration de l'�v�nement lorsqu'une entit� meurt
    public void handle(DestructEntityEvent.Death event) throws Exception {
            String cause = event.getCause().toString();

            // Si la mort a �t� caus�e par un joueur gr�ce � une arme
            if(cause.contains("Name=player, Type=attack")) {
                // On r�cup�re l'id de l'entit� morte
                String mobId = event.getTargetEntity().getType().getId();

                // Achievement KillMob
                Iterator iterator = this.core.getMobs().iterator();
                // On parcoure la liste des achievements de type KillMob et on v�rifie si l'id de l'entit� tu�e
                // correspond � l'id enregistr�e dans l'achievement
                // Ensuite on v�rifie que le bon nombre d'entit� de ce type ont �t� tu�es
                // Si oui, on valide l'achievement
                while (iterator.hasNext()) {
                    AchievementKillMob achiev = (AchievementKillMob) iterator.next();
                    if (mobId.equals(achiev.getMobId())) {

                        String causePlayer = getPlayerFromCause(event.getCause().toString());
                        String userUUID = core.getGame().getServer().getPlayer(causePlayer).get().getUniqueId().toString();
                        if(!playerHasAchievment(userUUID, achiev.getName())) {
                            // R�cup�rer le nombre de mobs tu�s par le joueur
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

        // Permet de r�cup�rer le nombre de mobs tu�s
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

        // Ajoute un monstre tu� au compteur du joueur
        private void addKilledMob(String userUUID, int badgeID, int mobKilled) throws IOException {
            WebService ws = new WebService(core);
            ws.updateAchievement(userUUID, badgeID, mobKilled);
        }
}
