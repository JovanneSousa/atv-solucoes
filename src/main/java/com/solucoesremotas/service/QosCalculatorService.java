package com.solucoesremotas.service;

import com.solucoesremotas.model.QosSimulationRequest;
import com.solucoesremotas.model.QosSimulationResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QosCalculatorService {

    public QosSimulationResult simulate(QosSimulationRequest req) {
        double effectiveLatency = req.latencyMs() + (req.jitterMs() * 2.0);
        if (req.qosPrioritizationEnabled()) {
            // Priorização DSCP (EF/AF41) reduz impacto de fila e jitter em redes congestionadas
            effectiveLatency *= 0.80;
        }

        double delayImpairment = effectiveLatency > 160.0
                ? (effectiveLatency - 160.0) * 0.12 + 4.0
                : effectiveLatency * 0.025;

        double lossFactor = req.qosPrioritizationEnabled()
                ? req.packetLossPercent() * 0.65
                : req.packetLossPercent();
        double lossImpairment = lossFactor * 8.5;

        double bandwidthPenalty = 0.0;
        if (req.bandwidthMbps() < 2.0) {
            bandwidthPenalty = 18.0;
        } else if (req.bandwidthMbps() < 5.0 && "VIDEOCONFERENCIA".equalsIgnoreCase(req.serviceType())) {
            bandwidthPenalty = 10.0;
        }

        double rFactor = Math.max(0.0, Math.min(93.2, 93.2 - delayImpairment - lossImpairment - bandwidthPenalty));
        double mos = computeMosFromRFactor(rFactor);

        String status;
        if (mos >= 4.0) {
            status = "Excelente";
        } else if (mos >= 3.6) {
            status = "Aceitável";
        } else if (mos >= 3.0) {
            status = "Degradada";
        } else {
            status = "Crítica";
        }

        String voiceImpact = mos >= 3.6
                ? "Áudio VoIP (SIP/RTP) claro, sem eco ou cortes perceptíveis."
                : "Áudio VoIP com picotes, atraso conversacional e perda de sílabas.";

        String videoImpact = (req.bandwidthMbps() >= 5.0 && mos >= 3.6)
                ? "Videoconferência HD estável com sincronismo áudio/vídeo."
                : "Risco de congelamento de quadros (freeze) e redução automática de resolução.";

        List<String> recommendations = buildRecommendations(req, mos);

        return new QosSimulationResult(
                round2(rFactor),
                round2(mos),
                status,
                voiceImpact,
                videoImpact,
                recommendations
        );
    }

    private double computeMosFromRFactor(double r) {
        if (r <= 0.0) {
            return 1.0;
        }
        if (r >= 100.0) {
            return 4.5;
        }
        double mos = 1.0 + (0.035 * r) + (r * (r - 60.0) * (100.0 - r) * 7.0e-6);
        return Math.max(1.0, Math.min(4.5, mos));
    }

    private List<String> buildRecommendations(QosSimulationRequest req, double mos) {
        List<String> recs = new ArrayList<>();
        if (!req.qosPrioritizationEnabled()) {
            recs.add("Ativar marcação DSCP/QoS (EF para VoIP RTP e AF41 para vídeo) nos roteadores e switches.");
        }
        if (req.latencyMs() > 150) {
            recs.add("Latência acima de 150ms: utilizar VPN Split-Tunnel para mídia em tempo real ou servidor SIP regional.");
        }
        if (req.jitterMs() > 30) {
            recs.add("Jitter acima de 30ms: configurar Jitter Buffer adaptativo e isolar tráfego de voz em VLAN dedicada.");
        }
        if (req.packetLossPercent() > 1.0) {
            recs.add("Perda de pacotes > 1%: verificar enlace WAN, habilitar FEC (Forward Error Correction) e link redundante.");
        }
        if (req.bandwidthMbps() < 5.0) {
            recs.add("Banda limitada (< 5 Mbps): ativar Modo Baixa Velocidade nas aplicações Web e codec Opus/G.729 no VoIP.");
        }
        if (recs.isEmpty() && mos >= 4.0) {
            recs.add("Parâmetros dentro das metas recomendadas pela ITU-T G.114 para redes convergentes.");
        }
        return recs;
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
