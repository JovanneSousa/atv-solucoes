package com.solucoesremotas.model;

public record SecurityAssessmentRequest(
        boolean firewallNgfw,
        boolean vpnEncrypted,
        boolean mfaEnabled,
        boolean vlanSegmentation,
        boolean rbacZeroTrust,
        boolean siemMonitoring,
        boolean endpointEncryption,
        boolean securityPolicyTrained
) {}
