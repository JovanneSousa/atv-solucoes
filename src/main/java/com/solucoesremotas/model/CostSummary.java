package com.solucoesremotas.model;

public record CostSummary(
        int remoteUsers,
        double totalCapexBrl,
        double monthlyOpexBrl,
        double annualOpexBrl,
        double monthlyGrossSavingsBrl,
        double monthlyNetBenefitBrl,
        double annualRoiPercent,
        double paybackMonths,
        String financialVerdict
) {}
