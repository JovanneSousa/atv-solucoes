package com.solucoesremotas.model;

public record CostRequest(
        int remoteUsers,
        double hardwareUpgradePerUserBrl,
        double vpnAndSecurityMonthlyPerUserBrl,
        double cloudAndVdiMonthlyPerUserBrl,
        double voipAndCollabMonthlyPerUserBrl,
        double officeSavingsMonthlyPerUserBrl,
        double networkSetupCapexBrl
) {}
