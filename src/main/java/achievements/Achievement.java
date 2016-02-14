package achievements;

// Class Achievement, fournit les méthodes et arguments de base pour un achievement
public abstract class Achievement {
    // Nom, type et description de l'achievement
    protected int type;
    protected String name;
    protected String description;
    protected String urlPicture;
    protected int badgeID;

    protected Achievement(String name, String description, String urlPicture, int badgeID) {
        this.name = name;
        this.description = description;
        this.badgeID = badgeID;
    }

    // Récupérer le type de l'achievement
    public int getType() {
        return this.type;
    }
    // Récupérer le nom de l'achievement
    public String getName() {
        return this.name;
    }
    // Récupérer la description de l'achievement
    public String getDescription() { return this.description; }
    public int getBadgeID() { return this.badgeID; }
    public String getUrlPicture() { return this.urlPicture; }
}
