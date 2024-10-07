package nl.rocnijmegen.testing;

public class App {

    // Method to check if a postcode is in the aardbevingsgebied
    public boolean isPostcodeInAardbevingsgebied(String postcode) {
        return postcode.equals("9679") || postcode.equals("9681") || postcode.equals("9682");
    }

    // Method to calculate maximum loan amount
    public double berekenMaximaalLeenbedrag(double totaalInkomen, boolean heeftStudieschuld) {
        double maximaalLeenbedrag = totaalInkomen * 12 * 5; // Max loan is 5x annual income
        if (heeftStudieschuld) {
            maximaalLeenbedrag *= 0.75; // Apply 25% discount if there is student debt
        }
        return maximaalLeenbedrag;
    }

    // Method to calculate monthly mortgage payments
    public double berekenMaandlasten(double leenbedrag, double rente, int jaren) {
        double maandRente = rente / 100 / 12;
        int aantalBetalingen = jaren * 12;
        return leenbedrag * (maandRente * Math.pow(1 + maandRente, aantalBetalingen)) /
                (Math.pow(1 + maandRente, aantalBetalingen) - 1);
    }

    // Method to calculate total interest over the entire loan period
    public double berekenTotaleRente(double totaalBetaling, double maximaalLeenbedrag) {
        return totaalBetaling - maximaalLeenbedrag;
    }
}
