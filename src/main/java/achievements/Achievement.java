package achievements;

// Class Achievement, fournit les méthodes et arguments de base pour un achievement
public abstract class Achievement {
    // Nom, type et description de l'achievement
    protected String type;
    protected String name;
    protected String description;

    protected Achievement(String name, String description) {
        this.name = name;
        this.description = description;
        this.type = "Aucun";
    }

    // Récupérer le type de l'achievement
    public String getType() {
        return this.type;
    }
    // Récupérer le nom de l'achievement
    public String getName() {
        return this.name;
    }
    // Récupérer la description de l'achievement
    public String getDescription() { return this.description; }
}
