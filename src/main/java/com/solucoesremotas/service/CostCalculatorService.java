package com.solucoesremotas.service;

import com.solucoesremotas.model.CostRequest;
import com.solucoesremotas.model.CostSummary;
import org.springframework.stereotype.Service;

@Service
public class CostCalculatorService {

    public CostSummary calculate(CostRequest req) {
        int users = Math.max(1, req.remoteUsers());
        double totalCapex = (users * Math.max(0, req.hardwareUpgradePerUserBrl()))
                + Math.max(0, req.networkSetupCapexBrl());

        double monthlyPerUserOpex = Math.max(0, req.vpnAndSecurityMonthlyPerUserBrl())
                + Math.max(0, req.cloudAndVdiMonthlyPerUserBrl())
                + Math.max(0, req.voipAndCollabMonthlyPerUserBrl());

        double monthlyOpex = users * monthlyPerUserOpex;
        double annualOpex = monthlyOpex * 12.0;

        double monthlyGrossSavings = users * Math.max(0, req.officeSavingsMonthlyPerUserBrl());
        double monthlyNetBenefit = monthlyGrossSavings - monthlyOpex;

        double annualNetBenefit = monthlyNetBenefit * 12.0;
        double totalFirstYearInvestment = totalCapex + annualOpex;
        double annualRoi = totalFirstYearInvestment > 0
                ? ((annualNetBenefit - totalCapex) / totalFirstYearInvestment) * 100.0
                : 0.0;

        double paybackMonths = monthlyNetBenefit > 0
                ? totalCapex / monthlyNetBenefit
                : -1.0;

        String verdict;
        if (monthlyNetBenefit <= 0) {
            verdict = "Atenção: OPEX mensal supera a economia estimada de escritório. Recomenda-se otimizar licenças VDI/Cloud.";
        } else if (paybackMonths <= 12) {
            verdict = "Altamente Viável: Retorno do investimento (Payback) em menos de 1 ano com ganho operacional líquido.";
        } else {
            verdict = "Viável a Médio Prazo: Economia líquida positiva com retorno gradual do CAPEX inicial.";
        }

        return new CostSummary(
                users,
                round2(totalCapex),
                round2(monthlyOpex),
                round2(annualOpex),
                round2(monthlyGrossSavings),
                round2(monthlyNetBenefit),
                round2(annualRoi),
                paybackMonths > 0 ? round2(paybackMonths) : -1.0,
                verdict
        );
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
