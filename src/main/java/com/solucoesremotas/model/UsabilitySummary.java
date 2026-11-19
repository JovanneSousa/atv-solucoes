package com.solucoesremotas.model;

import java.util.List;

public record UsabilitySummary(
        int totalEvaluations,
        double averageSusScore,
        String maturityGrade,
        double avgEfficiency,
        double avgEffectiveness,
        double avgLearnability,
        double avgSatisfaction,
        double avgAccessibility,
        List<UsabilityEvaluation> evaluations
) {}
