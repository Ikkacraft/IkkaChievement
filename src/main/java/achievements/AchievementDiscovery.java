package achievements;

// Achievement de type découverte
public class AchievementDiscovery extends Achievement {
    // nombre de pas à effectuer
    private int stepNumber;

    public AchievementDiscovery(String name, String description, int stepNumber) {
        super(name, description);
        this.type = "Discovery";
        this.stepNumber = stepNumber;
    }

    // Récupérer le nombre de pas à faire
    public int getStepNumber() { return stepNumber; }
}
