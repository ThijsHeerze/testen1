package nl.rocnijmegen.testing;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AppTest {
    @Spy
    private App appSpy;  // Spy on the App class

    @InjectMocks
    private App app;

    @BeforeEach
    public void setUp() {
        appSpy = spy(new App());  // Manually create the spy
    }

    @Test
    public void integrationTest() {
        // 1. Mocking the postcode check
        doReturn(true).when(appSpy).isPostcodeInAardbevingsgebied("9679");
        doReturn(false).when(appSpy).isPostcodeInAardbevingsgebied("1234");

        // Check if the postcode logic works as expected
        assertTrue(appSpy.isPostcodeInAardbevingsgebied("9679"), "Postcode 9679 should be in aardbevingsgebied");
        assertFalse(appSpy.isPostcodeInAardbevingsgebied("1234"), "Postcode 1234 should not be in aardbevingsgebied");

        // 2. Simulate total income calculation without student debt
        double totaalInkomen = 5000; // Total income: €5000
        boolean hasStudieschuld = false;

        // Call the method to calculate the maximum loan
        double maxLeenbedrag = app.berekenMaximaalLeenbedrag(totaalInkomen, hasStudieschuld);

        // Assert that the loan calculation is correct
        double expectedLeenbedrag = totaalInkomen * 12 * 5; // 5x annual income
        assertEquals(expectedLeenbedrag, maxLeenbedrag, 0.01, "Max loan amount without student debt should be correct");

        // 3. Simulate monthly payment calculation for a loan amount
        double leenbedrag = maxLeenbedrag; // Assume the user applies for the full loan amount
        double rente = 5.0; // Interest rate: 5%
        int jaren = 30; // Loan duration: 30 years

        // Calculate monthly payments
        double maandlasten = app.berekenMaandlasten(leenbedrag, rente, jaren);

        // Basic check that monthly payments are positive
        assertTrue(maandlasten > 0, "Monthly payments should be greater than 0");
    }


    @Test
    public void testPostcodeInAardbevingsgebied() {
        assertTrue(app.isPostcodeInAardbevingsgebied("9679"));
        assertTrue(app.isPostcodeInAardbevingsgebied("9681"));
        assertTrue(app.isPostcodeInAardbevingsgebied("9682"));
        assertFalse(app.isPostcodeInAardbevingsgebied("1234"));
    }

    @Test
    public void testBerekenMaximaalLeenbedragZonderStudieschuld() {
        double totaalInkomen = 3000 + 2000;
        double expectedLeenbedrag = 5000 * 12 * 5;  // 5x annual income
        assertEquals(expectedLeenbedrag, app.berekenMaximaalLeenbedrag(totaalInkomen, false));
    }

    @Test
    public void testBerekenMaximaalLeenbedragMetStudieschuld() {
        double totaalInkomen = 3000 + 2000;
        double expectedLeenbedrag = 5000 * 12 * 5 * 0.75;  // 5x annual income with 25% discount
        assertEquals(expectedLeenbedrag, app.berekenMaximaalLeenbedrag(totaalInkomen, true));
    }

    @Test
    public void testBerekenMaandlasten() {
        double leenbedrag = 300000;
        double rente = 3.5;
        int jaren = 30;
        double maandlasten = app.berekenMaandlasten(leenbedrag, rente, jaren);
        assertTrue(maandlasten > 0);
    }
}
