package com.solucoesremotas.service;

import com.solucoesremotas.model.SecurityAssessmentRequest;
import com.solucoesremotas.model.SecurityAssessmentResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityService {

    public SecurityAssessmentResult assess(SecurityAssessmentRequest req) {
        List<String> active = new ArrayList<>();
        List<String> missing = new ArrayList<>();
        int score = 0;

        score += evaluateControl(req.firewallNgfw(), 15, "Firewall NGFW com inspeção de pacotes", active, missing);
        score += evaluateControl(req.vpnEncrypted(), 15, "VPN com criptografia forte (WireGuard/IPsec AES-256)", active, missing);
        score += evaluateControl(req.mfaEnabled(), 20, "Autenticação Multifator (MFA) obrigatória", active, missing);
        score += evaluateControl(req.vlanSegmentation(), 10, "Segmentação de redes via VLANs (Dados/Voz/DMZ)", active, missing);
        score += evaluateControl(req.rbacZeroTrust(), 15, "Controle de acesso RBAC e modelo Zero Trust", active, missing);
        score += evaluateControl(req.siemMonitoring(), 10, "Monitoramento contínuo e correlação de logs (SIEM)", active, missing);
        score += evaluateControl(req.endpointEncryption(), 10, "Criptografia de disco e proteção de endpoints", active, missing);
        score += evaluateControl(req.securityPolicyTrained(), 5, "Política de Segurança e treinamento LGPD", active, missing);

        String level;
        if (score >= 90) {
            level = "Maturidade Alta (Zero Trust Consolidado)";
        } else if (score >= 70) {
            level = "Maturidade Adequada (Recomendado Reforço Pontual)";
        } else if (score >= 45) {
            level = "Maturidade Parcial (Risco Moderado)";
        } else {
            level = "Exposição Crítica (Ação Imediata Necessária)";
        }

        return new SecurityAssessmentResult(score, level, active, missing);
    }

    private int evaluateControl(boolean enabled, int weight, String label, List<String> active, List<String> missing) {
        if (enabled) {
            active.add(label);
            return weight;
        } else {
            missing.add(label);
            return 0;
        }
    }
}
