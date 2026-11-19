package com.solucoesremotas.controller;

import com.solucoesremotas.model.CostRequest;
import com.solucoesremotas.model.CostSummary;
import com.solucoesremotas.model.ProjectOverview;
import com.solucoesremotas.model.QosSimulationRequest;
import com.solucoesremotas.model.QosSimulationResult;
import com.solucoesremotas.model.SecurityAssessmentRequest;
import com.solucoesremotas.model.SecurityAssessmentResult;
import com.solucoesremotas.model.UsabilityEvaluation;
import com.solucoesremotas.model.UsabilitySummary;
import com.solucoesremotas.service.CostCalculatorService;
import com.solucoesremotas.service.ProjectDataService;
import com.solucoesremotas.service.QosCalculatorService;
import com.solucoesremotas.service.SecurityService;
import com.solucoesremotas.service.UsabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SolutionApiController {

    private final ProjectDataService projectDataService;
    private final CostCalculatorService costCalculatorService;
    private final QosCalculatorService qosCalculatorService;
    private final UsabilityService usabilityService;
    private final SecurityService securityService;

    public SolutionApiController(
            ProjectDataService projectDataService,
            CostCalculatorService costCalculatorService,
            QosCalculatorService qosCalculatorService,
            UsabilityService usabilityService,
            SecurityService securityService
    ) {
        this.projectDataService = projectDataService;
        this.costCalculatorService = costCalculatorService;
        this.qosCalculatorService = qosCalculatorService;
        this.usabilityService = usabilityService;
        this.securityService = securityService;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "application", "NexusRemote - Projeto Integrador",
                "availability", "99.95% SLA"
        );
    }

    @GetMapping("/project")
    public ProjectOverview getProjectOverview() {
        return projectDataService.getOverview();
    }

    @PostMapping("/costs/calculate")
    public CostSummary calculateCosts(@RequestBody CostRequest request) {
        return costCalculatorService.calculate(request);
    }

    @PostMapping("/network/qos-simulate")
    public QosSimulationResult simulateQos(@RequestBody QosSimulationRequest request) {
        return qosCalculatorService.simulate(request);
    }

    @GetMapping("/usability/evaluations")
    public UsabilitySummary getUsabilitySummary() {
        return usabilityService.getSummary();
    }

    @PostMapping("/usability/evaluations")
    public ResponseEntity<UsabilityEvaluation> addUsabilityEvaluation(@RequestBody UsabilityEvaluation evaluation) {
        return ResponseEntity.ok(usabilityService.addEvaluation(evaluation));
    }

    @PostMapping("/security/assess")
    public SecurityAssessmentResult assessSecurity(@RequestBody SecurityAssessmentRequest request) {
        return securityService.assess(request);
    }
}
