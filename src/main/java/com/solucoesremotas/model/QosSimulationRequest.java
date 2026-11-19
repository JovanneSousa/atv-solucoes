package com.solucoesremotas.model;

public record QosSimulationRequest(
        double latencyMs,
        double jitterMs,
        double packetLossPercent,
        double bandwidthMbps,
        boolean qosPrioritizationEnabled,
        String serviceType
) {}
