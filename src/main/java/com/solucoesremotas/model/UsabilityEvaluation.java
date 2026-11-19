package com.solucoesremotas.model;

public record UsabilityEvaluation(
        String id,
        String evaluatorName,
        String userProfile,
        int efficiencyScore,
        int effectivenessScore,
        int learnabilityScore,
        int satisfactionScore,
        int accessibilityScore,
        String heuristicIssue,
        String severity,
        String createdAt
) {}
