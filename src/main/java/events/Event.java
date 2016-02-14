package events;

import core.PluginCore;
import achievements.Achievement;
import ws.Badge;
import ws.WebService;

import java.io.IOException;

// Class Event. Contient les arguments et méthodes utilisés par tous les types d'événements
public abstract class Event {
    protected final PluginCore core;

    // Constructeur
    protected Event(PluginCore core) {
        this.core = core;
    }

    // Permet de valider un achievement
    protected void validAchievement(Achievement achievement, String cause) throws IOException {
        WebService ws = new WebService(core);
        String playerName = getPlayerFromCause(cause);
        String playerUUID = core.getGame().getServer().getPlayer(playerName).get().getUniqueId().toString();
        String userBadges = ws.getUnlockUserBadges(playerUUID);
        if (!userBadges.contains(achievement.getName())) {
            core.broadcastText(playerName + " a obtenu " + achievement.getName());
            ws.validAchievement(playerUUID, achievement.getBadgeID());
        }
    }

    // Récupère le nom du joueur grâce à la cause de l'évènement générée par Sponge
    public String getPlayerFromCause(String cause) {
        int pos1 = cause.indexOf("['");
        int pos2 = cause.indexOf("'/");
        return cause.substring(pos1 + 2, pos2);
    }
}
