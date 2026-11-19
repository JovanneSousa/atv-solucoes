package com.solucoesremotas.service;

import com.solucoesremotas.model.UsabilityEvaluation;
import com.solucoesremotas.model.UsabilitySummary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class UsabilityService {

    private final List<UsabilityEvaluation> evaluations = new CopyOnWriteArrayList<>();

    public UsabilityService() {
        evaluations.add(new UsabilityEvaluation(
                "AV-001",
                "Ana Paula (UX Lead)",
                "Atendimento & Suporte B2B",
                4, 5, 4, 4, 5,
                "Nenhum bloqueio crítico; atalhos de teclado aceleram o atendimento.",
                "Baixa",
                "2026-11-10 14:20"
        ));
        evaluations.add(new UsabilityEvaluation(
                "AV-002",
                "Carlos Eduardo (PcD Visual)",
                "Colaboradores PcD",
                4, 4, 5, 5, 5,
                "Leitor de tela NVDA leu todos os landmarks ARIA e o modo Alto Contraste atendeu WCAG AA.",
                "Baixa",
                "2026-11-12 10:05"
        ));
        evaluations.add(new UsabilityEvaluation(
                "AV-003",
                "Mariana Silva (Analista Financeira)",
                "Administrativo & Financeiro",
                4, 4, 4, 4, 4,
                "Em conexão 3G/4G instável, o Modo Baixa Velocidade manteve o formulário responsivo.",
                "Média",
                "2026-11-15 16:40"
        ));
    }

    public UsabilitySummary getSummary() {
        List<UsabilityEvaluation> snapshot = new ArrayList<>(evaluations);
        if (snapshot.isEmpty()) {
            return new UsabilitySummary(0, 0, "Sem dados", 0, 0, 0, 0, 0, snapshot);
        }

        double avgEff = snapshot.stream().mapToInt(UsabilityEvaluation::efficiencyScore).average().orElse(0);
        double avgEfc = snapshot.stream().mapToInt(UsabilityEvaluation::effectivenessScore).average().orElse(0);
        double avgLrn = snapshot.stream().mapToInt(UsabilityEvaluation::learnabilityScore).average().orElse(0);
        double avgSat = snapshot.stream().mapToInt(UsabilityEvaluation::satisfactionScore).average().orElse(0);
        double avgAcc = snapshot.stream().mapToInt(UsabilityEvaluation::accessibilityScore).average().orElse(0);

        // Converte média de 5 dimensões (escala 1..5) para índice SUS equivalente (0..100)
        double mean5 = (avgEff + avgEfc + avgLrn + avgSat + avgAcc) / 5.0;
        double susScore = Math.max(0.0, Math.min(100.0, ((mean5 - 1.0) / 4.0) * 100.0));

        String grade;
        if (susScore >= 85) {
            grade = "A (Excelente)";
        } else if (susScore >= 72) {
            grade = "B (Bom / Aprovado)";
        } else if (susScore >= 55) {
            grade = "C (Regular — Requer Melhorias)";
        } else {
            grade = "D (Crítico — Revisar UX/UI)";
        }

        return new UsabilitySummary(
                snapshot.size(),
                round1(susScore),
                grade,
                round1(avgEff),
                round1(avgEfc),
                round1(avgLrn),
                round1(avgSat),
                round1(avgAcc),
                snapshot
        );
    }

    public UsabilityEvaluation addEvaluation(UsabilityEvaluation input) {
        String id = "AV-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        UsabilityEvaluation saved = new UsabilityEvaluation(
                id,
                safeText(input.evaluatorName(), "Avaliador Anônimo"),
                safeText(input.userProfile(), "Geral"),
                clamp1to5(input.efficiencyScore()),
                clamp1to5(input.effectivenessScore()),
                clamp1to5(input.learnabilityScore()),
                clamp1to5(input.satisfactionScore()),
                clamp1to5(input.accessibilityScore()),
                safeText(input.heuristicIssue(), "Sem observações registradas."),
                safeText(input.severity(), "Baixa"),
                now
        );
        evaluations.add(0, saved);
        return saved;
    }

    private int clamp1to5(int value) {
        return Math.max(1, Math.min(5, value));
    }

    private String safeText(String val, String fallback) {
        return (val == null || val.isBlank()) ? fallback : val.trim();
    }

    private double round1(double val) {
        return Math.round(val * 10.0) / 10.0;
    }
}
