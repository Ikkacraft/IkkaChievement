package events;

import core.PluginCore;
import java.util.Iterator;
import achievements.AchievementKillMob;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.entity.DestructEntityEvent;

// Class KillMov réagit lorsqu'une entité meurt
public class KillMob extends Event implements EventListener<DestructEntityEvent.Death> {
    private int mobKilled;

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
                        // Récupérer le nombre nécessaire de kill pour l'achievement
                        int mobToKill = achiev.getMobNumber();
                        // Récupérer le nombre de mobs tués par le joueur
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

        // Permet de récupérer le nombre de mobs tués
        private int getKilledMob() {
            return mobKilled;
        }

        // Ajoute un monstre tué au compteur du joueur
        private void addKilledMob() {
            mobKilled++;
        }
}
