package com.solucoesremotas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SolutionApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthAndProjectEndpointsReturnOk() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));

        mockMvc.perform(get("/api/project"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationName").value("NovaTech Serviços Corporativos Ltda."));
    }

    @Test
    void qosAndCostAndSecurityEndpointsWork() throws Exception {
        mockMvc.perform(post("/api/network/qos-simulate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "latencyMs": 40,
                                  "jitterMs": 8,
                                  "packetLossPercent": 0.2,
                                  "bandwidthMbps": 25,
                                  "qosPrioritizationEnabled": true,
                                  "serviceType": "VOIP"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.qualityStatus").value("Excelente"));

        mockMvc.perform(post("/api/costs/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "remoteUsers": 140,
                                  "hardwareUpgradePerUserBrl": 650,
                                  "vpnAndSecurityMonthlyPerUserBrl": 32,
                                  "cloudAndVdiMonthlyPerUserBrl": 65,
                                  "voipAndCollabMonthlyPerUserBrl": 37,
                                  "officeSavingsMonthlyPerUserBrl": 320,
                                  "networkSetupCapexBrl": 42000
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remoteUsers").value(140));

        mockMvc.perform(post("/api/security/assess")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firewallNgfw": true,
                                  "vpnEncrypted": true,
                                  "mfaEnabled": true,
                                  "vlanSegmentation": true,
                                  "rbacZeroTrust": true,
                                  "siemMonitoring": true,
                                  "endpointEncryption": true,
                                  "securityPolicyTrained": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scorePercent").value(100));
    }
}
