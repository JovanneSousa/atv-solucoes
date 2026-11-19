package com.solucoesremotas.model;

import java.util.List;

public record SecurityAssessmentResult(
        int scorePercent,
        String postureLevel,
        List<String> activeControls,
        List<String> missingControls
) {}
