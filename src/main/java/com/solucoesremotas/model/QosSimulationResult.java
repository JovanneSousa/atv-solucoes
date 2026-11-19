package com.solucoesremotas.model;

import java.util.List;

public record QosSimulationResult(
        double rFactor,
        double mosScore,
        String qualityStatus,
        String voiceImpact,
        String videoImpact,
        List<String> recommendations
) {}
