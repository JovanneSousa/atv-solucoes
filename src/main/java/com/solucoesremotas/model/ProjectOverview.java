package com.solucoesremotas.model;

import java.util.List;

public record ProjectOverview(
        String organizationName,
        String sector,
        int totalEmployees,
        int remoteEmployees,
        String executiveSummary,
        List<NeedItem> needs,
        List<UserPersona> personas,
        List<InfraLayer> infrastructure,
        List<ToolItem> tools,
        List<PolicyItem> policies
) {
    public record NeedItem(
            String id,
            String category,
            String problem,
            String impact,
            String proposedSolution,
            String priority
    ) {}

    public record UserPersona(
            String role,
            int headcount,
            String connectivityProfile,
            String primaryTools,
            String accessibilityNeeds,
            String accessTechnology
    ) {}

    public record InfraLayer(
            String layer,
            String components,
            String protocols,
            String redundancy
    ) {}

    public record ToolItem(
            String name,
            String category,
            String purpose,
            String protocolOrStandard,
            double monthlyCostPerUserBrl
    ) {}

    public record PolicyItem(
            String code,
            String title,
            String unitReference,
            String description,
            String enforcement
    ) {}
}
