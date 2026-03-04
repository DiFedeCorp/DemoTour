package com.demotour;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportServiceTest {

    @Test
    void generateReportIncludesAccountAndBalance() {
        ReportService service = new ReportService();

        String report = service.generateReport("ACC-001", 1234.5, 1);

        assertTrue(report.contains("Compte: ACC-001"));
        assertTrue(report.contains("Solde: 1234.5"));
        assertTrue(report.contains("Type: Standard"));
    }

    @Test
    void generateReportUsesTypeLabels() {
        ReportService service = new ReportService();

        String premium = service.generateReport("ACC-001", 100.0, 2);
        String vip = service.generateReport("ACC-001", 100.0, 3);
        String unknown = service.generateReport("ACC-001", 100.0, 99);

        assertTrue(premium.contains("Type: Premium"));
        assertTrue(vip.contains("Type: VIP"));
        assertTrue(unknown.contains("Type: Inconnu"));
    }

    @Test
    void generateReportAddsStatusLinesBasedOnBalance() {
        ReportService service = new ReportService();

        String medium = service.generateReport("ACC-001", 20000.0, 1);
        String high = service.generateReport("ACC-001", 60000.0, 1);

        assertTrue(medium.contains("Statut: Eleve") || medium.contains("Statut: Moyen"));
        assertTrue(high.contains("Statut: Eleve"));
    }

    @Test
    void getReportLinesReturnsNonEmptyList() {
        ReportService service = new ReportService();

        List<String> lines = service.getReportLines("ACC-001", 10.0);

        assertFalse(lines.isEmpty());
        assertTrue(lines.contains("Ligne 1"));
        assertTrue(lines.contains("Ligne 2"));
        assertTrue(lines.stream().anyMatch(s -> s.contains("x")));
    }

    @Test
    void processAccountRunsWithoutThrowing() {
        ReportService service = new ReportService();

        service.processAccount("ACC-001");
        service.processAccount("UNKNOWN");
    }

    @Test
    void doStuffAppliesCurrentComputationLogic() {
        ReportService service = new ReportService();

        double resultLowZ = service.doStuff(10.0, 5.0, 50.0);
        double resultHighZ = service.doStuff(10.0, 5.0, 150.0);

        // logique actuelle : (x + y) * 1.05 puis +10 si z>100
        assertEquals(15.0 * 1.05, resultLowZ);
        assertEquals(15.0 * 1.05 + 10.0, resultHighZ);
    }

    @Test
    void formatAccountInfoContainsFields() {
        ReportService service = new ReportService();

        String info = service.formatAccountInfo("ACC-001", -50.0);

        assertTrue(info.contains("Compte: ACC-001"));
        assertTrue(info.contains("Solde: -50.0"));
        assertTrue(info.contains("Negatif"));
    }
}

