package events;

import core.PluginCore;
import achievements.Achievement;

// Class Event. Contient les arguments et méthodes utilisés par tous les types d'événements
public abstract class Event {
    protected final PluginCore core;

    // Constructeur
    protected Event(PluginCore core) {
        this.core = core;
    }

    // Permet de valider un achievement
    protected void validAchievement(Achievement achievement, String cause) {
        core.broadcastText(getPlayerFromCause(cause) + " a obtenu le " + achievement.getType() + " " + achievement.getName());
    }

    // Récupère le nom du joueur grâce à la cause de l'évènement générée par Sponge
    public String getPlayerFromCause(String cause) {
        int pos1 = cause.indexOf("['");
        int pos2 = cause.indexOf("'/");
        return cause.substring(pos1 + 2, pos2);
    }
}
