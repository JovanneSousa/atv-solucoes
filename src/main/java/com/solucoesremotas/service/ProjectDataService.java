package com.solucoesremotas.service;

import com.solucoesremotas.model.ProjectOverview;
import com.solucoesremotas.model.ProjectOverview.InfraLayer;
import com.solucoesremotas.model.ProjectOverview.NeedItem;
import com.solucoesremotas.model.ProjectOverview.PolicyItem;
import com.solucoesremotas.model.ProjectOverview.ToolItem;
import com.solucoesremotas.model.ProjectOverview.UserPersona;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectDataService {

    public ProjectOverview getOverview() {
        return new ProjectOverview(
                "NovaTech Serviços Corporativos Ltda.",
                "Serviços Corporativos, Tecnologia e Atendimento B2B",
                180,
                140,
                "Proposta de arquitetura integrada de trabalho remoto e híbrido para 140 colaboradores remotos, "
                        + "unificando políticas de usabilidade e acessibilidade digital (WCAG 2.1 AA), rede convergente "
                        + "com priorização QoS para voz/vídeo (VoIP/SIP/RTP), acesso remoto seguro via VPN WireGuard/VDI "
                        + "com autenticação MFA e governança Zero Trust.",
                List.of(
                        new NeedItem(
                                "NEC-01",
                                "Redes e Conectividade",
                                "Quedas frequentes em chamadas VoIP e travamentos em videoconferências por ausência de QoS.",
                                "Perda de produtividade e falhas no atendimento B2B em horários de pico.",
                                "Implantar rede convergente com marcação DSCP/QoS (EF para voz, AF41 para vídeo) e link redundante.",
                                "Alta"
                        ),
                        new NeedItem(
                                "NEC-02",
                                "Segurança e Acesso Remoto",
                                "Uso de RDP exposto e senhas estáticas sem MFA para acesso a sistemas internos.",
                                "Risco elevado de invasão, ransomware e vazamento de dados (LGPD).",
                                "Substituir RDP direto por VPN WireGuard/IPsec + VDI segmentado em VLANs e MFA obrigatório.",
                                "Crítica"
                        ),
                        new NeedItem(
                                "NEC-03",
                                "Usabilidade e Baixa Velocidade",
                                "Sistemas legados pesados que falham em conexões residenciais instáveis (< 10 Mbps).",
                                "Aumento de erros operacionais, retrabalho e frustração dos usuários.",
                                "Aplicar interface responsiva com Modo Baixa Velocidade, auto-save local e feedback de estado.",
                                "Alta"
                        ),
                        new NeedItem(
                                "NEC-04",
                                "Acessibilidade Digital",
                                "Falta de contraste adequado, ausência de navegação por teclado e incompatibilidade com leitores de tela.",
                                "Exclusão de colaboradores PcD e descumprimento de boas práticas WCAG 2.1.",
                                "Padronizar UI com WCAG 2.1 nível AA, atalhos de teclado, ARIA landmarks e modo Alto Contraste.",
                                "Média"
                        )
                ),
                List.of(
                        new UserPersona(
                                "Atendimento & Suporte B2B",
                                45,
                                "Banda larga residencial (15-50 Mbps), uso intenso de VoIP",
                                "Softphone SIP/WebRTC, CRM Web, Chat Corporativo",
                                "Contraste adequado, atalhos rápidos de teclado, áudio claro",
                                "Aplicação Web + Softphone VoIP via VPN Split-Tunnel QoS"
                        ),
                        new UserPersona(
                                "Engenharia de Software & TI",
                                40,
                                "Fibra óptica (100+ Mbps), sessões SSH/Git contínuas",
                                "IDE Cloud/VDI, Git, SSH, Monitoramento SIEM/Grafana",
                                "Temas dark/alto contraste, leitura de logs acessível",
                                "VPN Full/Split Tunnel + Bastion Host SSH com MFA"
                        ),
                        new UserPersona(
                                "Administrativo & Financeiro",
                                35,
                                "Conexão mista (10-50 Mbps) ou 4G/5G backup",
                                "ERP Web, Planilhas em Nuvem, Assinatura Digital",
                                "Fonte ampliável, formulários simples com prevenção de erros",
                                "VDI (Área de Trabalho Virtual) + Nuvem Corporativa"
                        ),
                        new UserPersona(
                                "Gestão & Diretoria",
                                12,
                                "Mobilidade frequente (Wi-Fi/5G)",
                                "Videoconferência HD, Dashboards BI, Aprovações",
                                "Design responsivo mobile-first, resumo executivo claro",
                                "Aplicações Web SaaS + VPN sob demanda"
                        ),
                        new UserPersona(
                                "Colaboradores PcD (Inclusão Digital)",
                                8,
                                "Variada (10-100 Mbps)",
                                "Suite Web acessível, Leitores de tela (NVDA/JAWS)",
                                "WCAG 2.1 AA, navegação 100% teclado, alto contraste, legendas",
                                "Portal Web Acessível + VDI com tecnologias assistivas"
                        )
                ),
                List.of(
                        new InfraLayer(
                                "Borda e Segurança (WAN)",
                                "Firewall NGFW Redundante (Active-Passive) + Concentrador VPN",
                                "IPsec IKEv2 / WireGuard, TLS 1.3, BGP/SD-WAN",
                                "Dual-ISP (Fibra Principal + Link Dedicado Backup)"
                        ),
                        new InfraLayer(
                                "Núcleo Convergente (LAN/DMZ)",
                                "Switches L3 Gerenciáveis com VLANs (Dados, Voz, Vídeo, Gestão)",
                                "802.1Q VLAN, DiffServ/DSCP QoS, TCP/IP Dual-Stack",
                                "Link Aggregation (LACP 802.3ad) e fontes redundantes"
                        ),
                        new InfraLayer(
                                "Serviços Multimídia & Comunicação",
                                "Servidor SIP/Asterisk + Gateway WebRTC + MCU Videoconferência",
                                "SIP, RTP/SRTP, RTCP, WebRTC, H.264/Opus",
                                "Cluster Active-Standby com monitoramento de MOS/Jitter"
                        ),
                        new InfraLayer(
                                "Virtualização & Nuvem (VDI/Cloud)",
                                "Cluster VDI + Storage Corporativo + Aplicações Web Containerizadas",
                                "RDP sobre TLS/VPN, HTTPS, S3/WebDAV, SSH",
                                "Backup 3-2-1 automatizado e replicação em nuvem"
                        )
                ),
                List.of(
                        new ToolItem("WireGuard / OpenVPN Enterprise", "Acesso Remoto & VPN", "Túnel criptografado ponto-a-site com baixa latência", "UDP/ChaCha20-Poly1305 / AES-256", 18.00),
                        new ToolItem("Asterisk / FreePBX + WebRTC", "Comunicação VoIP", "Telefonia IP convergente com QoS e criptografia SRTP", "SIP / RTP / RTCP", 22.00),
                        new ToolItem("Jitsi Meet / Nextcloud Talk", "Videoconferência & Collab", "Reuniões em tempo real e compartilhamento de tela", "WebRTC / DTLS-SRTP", 15.00),
                        new ToolItem("Apache Guacamole / VDI", "Virtualização / Remote Desktop", "Acesso a desktops virtuais via navegador HTML5 sem cliente", "HTTPS / RDP / SSH", 65.00),
                        new ToolItem("Keycloak (MFA / SSO)", "Identidade & Controle de Acesso", "Autenticação multifator e Single Sign-On corporativo", "OAuth2 / OIDC / TOTP", 12.00),
                        new ToolItem("Wazuh / Zabbix SIEM", "Monitoramento & Segurança", "Detecção de intrusão, logs e métricas de rede/QoS", "Syslog / SNMPv3 / Agent", 14.00)
                ),
                List.of(
                        new PolicyItem("POL-01", "Política de Usabilidade e Padrão UI", "Unidade IV (4.1 - 4.3)",
                                "Todas as interfaces internas devem seguir consistência visual, feedback imediato (<300ms), prevenção de erros e modo de baixa largura de banda.",
                                "Revisão heurística trimestral e testes SUS >= 75."),
                        new PolicyItem("POL-02", "Diretriz de Acessibilidade WCAG 2.1 AA", "Unidade IV (4.4)",
                                "Contraste mínimo 4.5:1 para texto normal, navegação completa por teclado, foco visível e suporte a leitores de tela via ARIA.",
                                "Auditoria automatizada + validação com usuários PcD."),
                        new PolicyItem("POL-03", "Política de Priorização QoS em Rede Convergente", "Unidade V (5.2 - 5.4)",
                                "Tráfego VoIP (RTP) e videoconferência têm prioridade sobre downloads e backups, mantendo latência < 150ms, jitter < 30ms e perda < 1%.",
                                "Políticas DSCP nos roteadores/switches e alertas Zabbix."),
                        new PolicyItem("POL-04", "Política de Acesso Remoto Zero Trust & Segurança", "Unidade V (5.5 - 5.6)",
                                "Proibido expor RDP/SSH diretamente na Internet. Acesso obrigatório via VPN criptografada + MFA, com segmentação por VLAN e bloqueio após 5 falhas.",
                                "Firewall NGFW, logs centralizados no SIEM e revisão mensal de acessos.")
                )
        );
    }
}
