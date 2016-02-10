package events;

import core.PluginCore;
import java.util.Iterator;
import achievements.AchievementKillMob;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.entity.DestructEntityEvent;

// Class KillMov r�agit lorsqu'une entit� meurt
public class KillMob extends Event implements EventListener<DestructEntityEvent.Death> {
    private int mobKilled;

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
                        // R�cup�rer le nombre n�cessaire de kill pour l'achievement
                        int mobToKill = achiev.getMobNumber();
                        // R�cup�rer le nombre de mobs tu�s par le joueur
                        int mobKilled = getKilledMob();

                        if(mobKilled <= mobToKill) {
                            addKilledMob();
                            if(mobKilled == mobToKill-1) {
                                validAchievement(achiev, cause);
                                break;
                            }
                        }
                    }
                }
            }
        }

        // Permet de r�cup�rer le nombre de mobs tu�s
        private int getKilledMob() {
            return mobKilled;
        }

        // Ajoute un monstre tu� au compteur du joueur
        private void addKilledMob() {
            mobKilled++;
        }
}
