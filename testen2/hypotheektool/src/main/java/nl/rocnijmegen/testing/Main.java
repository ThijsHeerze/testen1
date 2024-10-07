package nl.rocnijmegen.testing;

import javax.swing.JOptionPane;
import java.text.NumberFormat;

public class Main {

    public static void main(String[] args) {
        App app = new App(); // Create an instance of the App class
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        // Ask for postcode and validate
        String postcode = JOptionPane.showInputDialog("Wat is uw postcode?");
        if (app.isPostcodeInAardbevingsgebied(postcode)) {
            JOptionPane.showMessageDialog(null, "Hypotheekberekeningen voor deze postcode zijn niet toegestaan vanwege het aardbevingsgebied.");
            System.exit(0);
        }

        // Ask for the customer's monthly income
        double inkomen = Double.parseDouble(JOptionPane.showInputDialog("Wat is uw maandinkomen? €"));

        // Ask if the customer has a partner and if yes, ask for the partner's income
        double partnerInkomen = 0;
        int keuze = JOptionPane.showConfirmDialog(null, "Heeft u een partner?", "Partner", JOptionPane.YES_NO_OPTION);
        if (keuze == JOptionPane.YES_OPTION) {
            partnerInkomen = Double.parseDouble(JOptionPane.showInputDialog("Wat is het maandinkomen van uw partner? €"));
        }

        // Ask if the customer has student debt
        keuze = JOptionPane.showConfirmDialog(null, "Heeft u een studieschuld?", "Studieschuld", JOptionPane.YES_NO_OPTION);
        boolean heeftStudieschuld = (keuze == JOptionPane.YES_OPTION);

        // Ask for the fixed-interest period and validate it
        int jaren = Integer.parseInt(JOptionPane.showInputDialog("Kies een rentevaste periode: 1, 5, 10, 20 of 30 jaar"));
        double rente = 0;

        switch (jaren) {
            case 1:
                rente = 2.0;
                break;
            case 5:
                rente = 3.0;
                break;
            case 10:
                rente = 3.5;
                break;
            case 20:
                rente = 4.5;
                break;
            case 30:
                rente = 5.0;
                break;
            default:
                JOptionPane.showMessageDialog(null, "Ongeldige rentevaste periode. Kies uit 1, 5, 10, 20 of 30 jaar.");
                System.exit(0);
        }

        // Calculate the total monthly income
        double totaalInkomen = inkomen + partnerInkomen;

        // Calculate the maximum loan amount
        double maximaalLeenbedrag = app.berekenMaximaalLeenbedrag(totaalInkomen, heeftStudieschuld);

        // Calculate the monthly mortgage payments
        double maandlasten = app.berekenMaandlasten(maximaalLeenbedrag, rente, jaren);

        // Calculate total payment and total interest
        int aantalBetalingen = jaren * 12;
        double totaalBetaling = maandlasten * aantalBetalingen;
        double totaleRente = app.berekenTotaleRente(totaalBetaling, maximaalLeenbedrag);

        // Display the results
        JOptionPane.showMessageDialog(null, "Op basis van uw inkomen kunt u maximaal lenen: " + currencyFormat.format(maximaalLeenbedrag)
                + "\nMaandelijkse lasten: " + currencyFormat.format(maandlasten)
                + "\nMaandelijks te betalen rente: " + currencyFormat.format(totaleRente / aantalBetalingen)
                + "\nMaandelijks af te lossen bedrag: " + currencyFormat.format(maandlasten - (totaleRente / aantalBetalingen))
                + "\nTotale betaling over " + jaren + " jaar: " + currencyFormat.format(totaalBetaling));
    }
}
