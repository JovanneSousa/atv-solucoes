package com.solucoesremotas;

import com.solucoesremotas.model.CostRequest;
import com.solucoesremotas.model.CostSummary;
import com.solucoesremotas.model.QosSimulationRequest;
import com.solucoesremotas.model.QosSimulationResult;
import com.solucoesremotas.model.SecurityAssessmentRequest;
import com.solucoesremotas.model.SecurityAssessmentResult;
import com.solucoesremotas.model.UsabilityEvaluation;
import com.solucoesremotas.service.CostCalculatorService;
import com.solucoesremotas.service.ProjectDataService;
import com.solucoesremotas.service.QosCalculatorService;
import com.solucoesremotas.service.SecurityService;
import com.solucoesremotas.service.UsabilityService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolutionServicesTest {

    @Test
    void projectOverviewContainsNeedsPersonasToolsAndPolicies() {
        var service = new ProjectDataService();
        var overview = service.getOverview();

        assertEquals(180, overview.totalEmployees());
        assertEquals(140, overview.remoteEmployees());
        assertFalse(overview.needs().isEmpty());
        assertFalse(overview.personas().isEmpty());
        assertFalse(overview.infrastructure().isEmpty());
        assertFalse(overview.tools().isEmpty());
        assertFalse(overview.policies().isEmpty());
    }

    @Test
    void costCalculatorComputesViablePaybackAndRoi() {
        var service = new CostCalculatorService();
        CostSummary summary = service.calculate(new CostRequest(
                100, 500, 30, 60, 30, 300, 30000
        ));

        assertEquals(80000.0, summary.totalCapexBrl());
        assertEquals(12000.0, summary.monthlyOpexBrl());
        assertEquals(18000.0, summary.monthlyNetBenefitBrl());
        assertTrue(summary.paybackMonths() > 0 && summary.paybackMonths() < 12);
        assertTrue(summary.annualRoiPercent() > 0);
    }

    @Test
    void qosSimulatorReflectsDegradationWhenNetworkIsCongested() {
        var service = new QosCalculatorService();
        QosSimulationResult good = service.simulate(new QosSimulationRequest(35, 8, 0.2, 30, true, "VOIP"));
        QosSimulationResult bad = service.simulate(new QosSimulationRequest(280, 55, 4.0, 1.5, false, "VIDEOCONFERENCIA"));

        assertTrue(good.mosScore() >= 4.0);
        assertEquals("Excelente", good.qualityStatus());
        assertTrue(bad.mosScore() < 3.0);
        assertEquals("Crítica", bad.qualityStatus());
        assertFalse(bad.recommendations().isEmpty());
    }

    @Test
    void usabilityServiceAddsEvaluationAndComputesSusScore() {
        var service = new UsabilityService();
        int initialCount = service.getSummary().totalEvaluations();

        service.addEvaluation(new UsabilityEvaluation(
                null, "Teste Unitário", "TI", 5, 5, 5, 5, 5, "Excelente", "Baixa", null
        ));

        var updated = service.getSummary();
        assertEquals(initialCount + 1, updated.totalEvaluations());
        assertTrue(updated.averageSusScore() > 75.0);
    }

    @Test
    void securityServiceScoresControlsProperly() {
        var service = new SecurityService();
        SecurityAssessmentResult full = service.assess(new SecurityAssessmentRequest(
                true, true, true, true, true, true, true, true
        ));
        SecurityAssessmentResult partial = service.assess(new SecurityAssessmentRequest(
                true, false, false, false, false, false, false, false
        ));

        assertEquals(100, full.scorePercent());
        assertTrue(full.missingControls().isEmpty());
        assertEquals(15, partial.scorePercent());
        assertEquals(7, partial.missingControls().size());
    }
}
