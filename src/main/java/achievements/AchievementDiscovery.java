package achievements;

// Achievement de type découverte
public class AchievementDiscovery extends Achievement {
    // nombre de pas à effectuer
    private int stepNumber;

    public AchievementDiscovery(String name, String description, int stepNumber, String urlPicture, int badgeID) {
        super(name, description, urlPicture, badgeID);
        this.type = 4;
        this.stepNumber = stepNumber;
    }

    // Récupérer le nombre de pas à faire
    public int getStepNumber() { return stepNumber; }
}
