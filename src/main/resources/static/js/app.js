(() => {
    const state = {
        fontScale: 1,
        project: null,
        costSummary: null,
        usabilitySummary: null,
        securityResult: null,
        slideIndex: 0
    };

    const diagramDetails = {
        "remote-users": {
            title: "1. Colaboradores Remotos (Endpoints & Acessibilidade)",
            body: "Interfaces Web responsivas com WCAG 2.1 AA (Alto Contraste, leitores de tela, navegação por teclado), Modo Baixa Velocidade para conexões residenciais e Softphone SIP/WebRTC."
        },
        "wan-firewall": {
            title: "2. Borda WAN: Firewall NGFW + Concentrador VPN",
            body: "Redundância Dual-ISP, inspeção de pacotes (IPS/IDS), VPN WireGuard (ChaCha20) e IPsec (AES-256) com Split-Tunneling para tráfego multimídia e bloqueio de portas legadas (RDP 3389)."
        },
        "core-qos": {
            title: "3. Núcleo Convergente (LAN/DMZ + QoS DSCP)",
            body: "Switches L3 com segmentação 802.1Q (VLAN Dados, VLAN Voz, VLAN Vídeo, VLAN Gestão) e priorização DiffServ/DSCP (EF para VoIP, AF41 para vídeo) garantindo latência < 150ms e jitter < 30ms."
        },
        "voip-media": {
            title: "4A. Serviços Multimídia (VoIP & Videoconferência)",
            body: "Servidor Asterisk/SIP com SRTP e RTCP para monitoramento de qualidade (MOS), integrado a WebRTC (Jitsi/Nextcloud Talk) para reuniões e atendimento B2B."
        },
        "vdi-cloud": {
            title: "4B. Virtualização (VDI) & Aplicações em Nuvem",
            body: "Apache Guacamole (HTML5 sobre HTTPS), acesso administrativo via Bastion Host SSH (chaves Ed25519) e armazenamento em nuvem corporativa com backup 3-2-1."
        },
        "siem-iam": {
            title: "4C. Identidade (IAM/MFA) & Monitoramento (SIEM)",
            body: "Keycloak com SSO (OIDC) e MFA obrigatório + Wazuh/Zabbix coletando logs de segurança, alertas de intrusão e métricas de QoS em tempo real."
        }
    };

    const slides = [
        {
            title: "1. Contexto e Levantamento de Necessidades",
            points: [
                "Organização: NovaTech Serviços Corporativos Ltda. (180 colaboradores, 140 remotos).",
                "Problemas críticos: quedas em VoIP por falta de QoS, RDP exposto sem MFA, sistemas pesados em internet lenta e barreiras de acessibilidade.",
                "Objetivo: estruturar uma solução remota segura, convergente, acessível e financeiramente sustentável."
            ]
        },
        {
            title: "2. Usabilidade e Acessibilidade Digital (Unidade IV)",
            points: [
                "Aplicação dos pilares de IHC: Eficiência, Eficácia, Facilidade de Aprendizagem e Satisfação.",
                "Conformidade WCAG 2.1 AA: contraste >= 4.5:1, navegação 100% por teclado, skip-links e suporte a leitores de tela (ARIA).",
                "Modo Baixa Velocidade e indicadores de disponibilidade/estado do sistema para conexões instáveis.",
                "Avaliação contínua via testes com usuários, avaliação heurística e índice SUS."
            ]
        },
        {
            title: "3. Redes Convergentes, QoS e Segurança (Unidade V)",
            points: [
                "Rede convergente integrando Dados, Voz (SIP/RTP/RTCP) e Vídeo (WebRTC) em VLANs dedicadas.",
                "Política QoS/DSCP priorizando tráfego em tempo real (meta MOS >= 4.0, latência < 150ms, jitter < 30ms).",
                "Acesso remoto via VPN WireGuard/IPsec, VDI HTML5 e SSH Bastion Host — eliminando exposição direta.",
                "Arquitetura Zero Trust: Firewall NGFW, MFA obrigatório, RBAC, criptografia e monitoramento SIEM."
            ]
        },
        {
            title: "4. Custos, ROI e Viabilidade (Unidade VI)",
            points: [
                "CAPEX inicial controlado (rede/firewall/SIP + kit home office).",
                "OPEX previsível por usuário (VPN, VDI/Cloud, VoIP/Collab).",
                "Economia expressiva com redução de espaço físico e custos operacionais de escritório, gerando payback rápido."
            ]
        },
        {
            title: "5. Conclusão e Próximos Passos",
            points: [
                "Implantação em fases: Piloto com TI/Suporte (30 dias) -> Expansão Administrativo -> Auditoria WCAG & Pentest.",
                "Monitoramento contínuo de SLA (99,95%), métricas QoS (MOS) e satisfação dos colaboradores (SUS >= 80).",
                "Solução pronta para escalar com segurança, inclusão digital e alta disponibilidade."
            ]
        }
    ];

    function announce(text) {
        const el = document.getElementById("live-announcer");
        if (el) el.textContent = text;
    }

    function escapeHtml(str) {
        return String(str)
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;");
    }

    function formatBrl(val) {
        return new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" }).format(val);
    }

    // --- Navegação por Abas ---
    function setupTabs() {
        const tabs = document.querySelectorAll(".nav-tab");
        const panels = document.querySelectorAll(".tab-panel");

        function activateTab(tab) {
            const targetId = tab.dataset.tab;
            tabs.forEach(t => {
                const active = t === tab;
                t.classList.toggle("active", active);
                t.setAttribute("aria-selected", String(active));
            });
            panels.forEach(p => {
                const active = p.id === targetId;
                p.hidden = !active;
                p.classList.toggle("active", active);
            });
            announce(`Aba ${tab.textContent.trim()} selecionada.`);
        }

        tabs.forEach(tab => {
            tab.addEventListener("click", () => activateTab(tab));
        });
    }

    // --- Controles de Acessibilidade e Estado ---
    function setupAccessibilityControls() {
        const btnContrast = document.getElementById("btn-high-contrast");
        const btnLowBw = document.getElementById("btn-low-bandwidth");
        const btnInc = document.getElementById("btn-font-inc");
        const btnDec = document.getElementById("btn-font-dec");

        btnContrast.addEventListener("click", () => {
            const active = document.body.classList.toggle("high-contrast");
            btnContrast.setAttribute("aria-pressed", String(active));
            announce(active ? "Modo alto contraste ativado" : "Modo alto contraste desativado");
        });

        btnLowBw.addEventListener("click", () => {
            const active = document.body.classList.toggle("low-bandwidth");
            btnLowBw.setAttribute("aria-pressed", String(active));
            announce(active ? "Modo baixa velocidade ativado" : "Modo baixa velocidade desativado");
        });

        btnInc.addEventListener("click", () => {
            state.fontScale = Math.min(1.25, +(state.fontScale + 0.05).toFixed(2));
            document.documentElement.style.setProperty("--font-scale", state.fontScale);
        });

        btnDec.addEventListener("click", () => {
            state.fontScale = Math.max(0.85, +(state.fontScale - 0.05).toFixed(2));
            document.documentElement.style.setProperty("--font-scale", state.fontScale);
        });
    }

    async function checkSystemHealth() {
        const pill = document.getElementById("system-status-pill");
        try {
            const res = await fetch("/api/health");
            if (!res.ok) throw new Error("offline");
            pill.classList.remove("status-offline");
            pill.classList.add("status-ok");
            pill.innerHTML = '<span class="dot" aria-hidden="true"></span> Sistema Online (99,95% SLA)';
        } catch {
            pill.classList.remove("status-ok");
            pill.classList.add("status-offline");
            pill.innerHTML = '<span class="dot" aria-hidden="true"></span> Modo Offline / Reconectando';
        }
    }

    // --- Unidade VI: Carregar Projeto ---
    async function loadProject() {
        const res = await fetch("/api/project");
        const data = await res.json();
        state.project = data;

        document.getElementById("org-name").textContent = data.organizationName;
        document.getElementById("org-sector").textContent = data.sector;
        document.getElementById("org-summary").textContent = data.executiveSummary;
        document.getElementById("kpi-total-emp").textContent = data.totalEmployees;
        document.getElementById("kpi-remote-emp").textContent = data.remoteEmployees;

        document.getElementById("needs-tbody").innerHTML = data.needs.map(n => `
            <tr>
                <td><strong>${escapeHtml(n.id)}</strong></td>
                <td>${escapeHtml(n.category)}</td>
                <td>${escapeHtml(n.problem)}</td>
                <td>${escapeHtml(n.impact)}</td>
                <td>${escapeHtml(n.proposedSolution)}</td>
                <td><span class="badge">${escapeHtml(n.priority)}</span></td>
            </tr>
        `).join("");

        document.getElementById("personas-grid").innerHTML = data.personas.map(p => `
            <article class="item-card">
                <h4>${escapeHtml(p.role)} (${p.headcount} usuários)</h4>
                <p><strong>Conectividade:</strong> ${escapeHtml(p.connectivityProfile)}</p>
                <p><strong>Ferramentas:</strong> ${escapeHtml(p.primaryTools)}</p>
                <p><strong>Acessibilidade:</strong> ${escapeHtml(p.accessibilityNeeds)}</p>
                <p><strong>Acesso:</strong> ${escapeHtml(p.accessTechnology)}</p>
            </article>
        `).join("");

        document.getElementById("infra-list").innerHTML = data.infrastructure.map(i => `
            <div class="item-card">
                <h4>${escapeHtml(i.layer)}</h4>
                <p><strong>Componentes:</strong> ${escapeHtml(i.components)}</p>
                <p><strong>Protocolos:</strong> ${escapeHtml(i.protocols)}</p>
                <p><strong>Redundância:</strong> ${escapeHtml(i.redundancy)}</p>
            </div>
        `).join("");

        document.getElementById("tools-list").innerHTML = data.tools.map(t => `
            <div class="item-card">
                <h4>${escapeHtml(t.name)} — ${formatBrl(t.monthlyCostPerUserBrl)}/usuário/mês</h4>
                <p><strong>Categoria:</strong> ${escapeHtml(t.category)} | <strong>Padrão:</strong> ${escapeHtml(t.protocolOrStandard)}</p>
                <p>${escapeHtml(t.purpose)}</p>
            </div>
        `).join("");

        document.getElementById("policies-grid").innerHTML = data.policies.map(pol => `
            <article class="item-card">
                <h4>${escapeHtml(pol.code)}: ${escapeHtml(pol.title)}</h4>
                <p class="muted">${escapeHtml(pol.unitReference)}</p>
                <p>${escapeHtml(pol.description)}</p>
                <p><strong>Fiscalização:</strong> ${escapeHtml(pol.enforcement)}</p>
            </article>
        `).join("");
    }

    // --- Unidade IV: Contraste WCAG & Avaliações ---
    function setupContrastChecker() {
        const fgInput = document.getElementById("fg-color");
        const bgInput = document.getElementById("bg-color");
        const preview = document.getElementById("contrast-preview");
        const ratioEl = document.getElementById("contrast-ratio");
        const badgeEl = document.getElementById("contrast-badge");

        function luminance(hex) {
            const rgb = [1, 3, 5].map(i => parseInt(hex.slice(i, i + 2), 16) / 255);
            const lin = rgb.map(c => (c <= 0.03928 ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4)));
            return 0.2126 * lin[0] + 0.7152 * lin[1] + 0.0722 * lin[2];
        }

        function update() {
            const fg = fgInput.value;
            const bg = bgInput.value;
            preview.style.color = fg;
            preview.style.backgroundColor = bg;
            const l1 = luminance(fg);
            const l2 = luminance(bg);
            const ratio = (Math.max(l1, l2) + 0.05) / (Math.min(l1, l2) + 0.05);
            ratioEl.textContent = `${ratio.toFixed(2)}:1`;

            if (ratio >= 7) {
                badgeEl.textContent = "WCAG AAA (Excelente)";
            } else if (ratio >= 4.5) {
                badgeEl.textContent = "WCAG AA (Aprovado)";
            } else {
                badgeEl.textContent = "Reprovado (< 4.5:1)";
            }
        }

        fgInput.addEventListener("input", update);
        bgInput.addEventListener("input", update);
        update();
    }

    async function loadUsability() {
        const res = await fetch("/api/usability/evaluations");
        const data = await res.json();
        state.usabilitySummary = data;

        document.getElementById("sus-score").textContent = `${data.averageSusScore} / 100`;
        document.getElementById("sus-grade").textContent = data.maturityGrade;
        document.getElementById("sus-count").textContent = data.totalEvaluations;
        document.getElementById("sus-acc").textContent = `${data.avgAccessibility} / 5.0`;

        document.getElementById("evaluations-list").innerHTML = data.evaluations.map(ev => `
            <div class="item-card">
                <h4>${escapeHtml(ev.id)} — ${escapeHtml(ev.evaluatorName)} (${escapeHtml(ev.severity)})</h4>
                <p class="muted">${escapeHtml(ev.userProfile)} | ${escapeHtml(ev.createdAt)}</p>
                <p>Efic: ${ev.efficiencyScore} | Eficác: ${ev.effectivenessScore} | Aprend: ${ev.learnabilityScore} | Satisf: ${ev.satisfactionScore} | Acess: ${ev.accessibilityScore}</p>
                <p>${escapeHtml(ev.heuristicIssue)}</p>
            </div>
        `).join("");
    }

    function setupUsabilityForm() {
        const form = document.getElementById("usability-form");
        form.addEventListener("submit", async e => {
            e.preventDefault();
            const payload = {
                evaluatorName: document.getElementById("eval-name").value,
                userProfile: document.getElementById("eval-profile").value,
                efficiencyScore: Number(document.getElementById("eval-eff").value),
                effectivenessScore: Number(document.getElementById("eval-efc").value),
                learnabilityScore: Number(document.getElementById("eval-lrn").value),
                satisfactionScore: Number(document.getElementById("eval-sat").value),
                accessibilityScore: Number(document.getElementById("eval-acc").value),
                heuristicIssue: document.getElementById("eval-issue").value,
                severity: document.getElementById("eval-severity").value
            };
            await fetch("/api/usability/evaluations", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });
            form.reset();
            await loadUsability();
            announce("Nova avaliação de usabilidade registrada com sucesso.");
        });
    }

    // --- Unidade V: Simulador QoS ---
    async function runQosSimulation() {
        const payload = {
            serviceType: document.getElementById("qos-service").value,
            latencyMs: Number(document.getElementById("qos-latency").value),
            jitterMs: Number(document.getElementById("qos-jitter").value),
            packetLossPercent: Number(document.getElementById("qos-loss").value),
            bandwidthMbps: Number(document.getElementById("qos-bw").value),
            qosPrioritizationEnabled: document.getElementById("qos-dscp").checked
        };
        const res = await fetch("/api/network/qos-simulate", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        const data = await res.json();
        document.getElementById("qos-mos").textContent = data.mosScore;
        document.getElementById("qos-rfactor").textContent = data.rFactor;
        document.getElementById("qos-status").textContent = data.qualityStatus;
        document.getElementById("qos-voice").textContent = data.voiceImpact;
        document.getElementById("qos-video").textContent = data.videoImpact;
        document.getElementById("qos-recs").innerHTML = data.recommendations.map(r => `<li>${escapeHtml(r)}</li>`).join("");
    }

    function setupQosForm() {
        const bindings = [
            ["qos-latency", "val-latency"],
            ["qos-jitter", "val-jitter"],
            ["qos-loss", "val-loss"],
            ["qos-bw", "val-bw"]
        ];
        bindings.forEach(([inputId, labelId]) => {
            const input = document.getElementById(inputId);
            const label = document.getElementById(labelId);
            input.addEventListener("input", () => {
                label.textContent = input.value;
            });
        });

        document.getElementById("qos-form").addEventListener("submit", async e => {
            e.preventDefault();
            await runQosSimulation();
            announce("Simulação de QoS atualizada.");
        });
    }

    // --- Unidade V: Segurança ---
    async function runSecurityAssessment() {
        const payload = {
            firewallNgfw: document.getElementById("sec-fw").checked,
            vpnEncrypted: document.getElementById("sec-vpn").checked,
            mfaEnabled: document.getElementById("sec-mfa").checked,
            vlanSegmentation: document.getElementById("sec-vlan").checked,
            rbacZeroTrust: document.getElementById("sec-rbac").checked,
            siemMonitoring: document.getElementById("sec-siem").checked,
            endpointEncryption: document.getElementById("sec-endpoint").checked,
            securityPolicyTrained: document.getElementById("sec-policy").checked
        };
        const res = await fetch("/api/security/assess", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        const data = await res.json();
        state.securityResult = data;

        document.getElementById("sec-score").textContent = `${data.scorePercent}%`;
        document.getElementById("sec-level").textContent = data.postureLevel;
        document.getElementById("sec-active").innerHTML = data.activeControls.map(c => `<li>${escapeHtml(c)}</li>`).join("") || "<li>Nenhum controle ativo.</li>";
        document.getElementById("sec-missing").innerHTML = data.missingControls.map(c => `<li>${escapeHtml(c)}</li>`).join("") || "<li>Nenhuma lacuna crítica!</li>";
    }

    function setupSecurityForm() {
        document.getElementById("security-form").addEventListener("submit", async e => {
            e.preventDefault();
            await runSecurityAssessment();
            announce("Avaliação de postura de segurança atualizada.");
        });
    }

    // --- Unidade VI: Custos & Diagrama ---
    async function calculateCosts() {
        const payload = {
            remoteUsers: Number(document.getElementById("cost-users").value),
            networkSetupCapexBrl: Number(document.getElementById("cost-capex-net").value),
            hardwareUpgradePerUserBrl: Number(document.getElementById("cost-hw").value),
            vpnAndSecurityMonthlyPerUserBrl: Number(document.getElementById("cost-vpn").value),
            cloudAndVdiMonthlyPerUserBrl: Number(document.getElementById("cost-vdi").value),
            voipAndCollabMonthlyPerUserBrl: Number(document.getElementById("cost-voip").value),
            officeSavingsMonthlyPerUserBrl: Number(document.getElementById("cost-savings").value)
        };
        const res = await fetch("/api/costs/calculate", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        const data = await res.json();
        state.costSummary = data;

        document.getElementById("res-capex").textContent = formatBrl(data.totalCapexBrl);
        document.getElementById("res-opex-m").textContent = formatBrl(data.monthlyOpexBrl);
        document.getElementById("res-net").textContent = formatBrl(data.monthlyNetBenefitBrl);
        document.getElementById("res-payback").textContent = data.paybackMonths > 0 ? `${data.paybackMonths} meses` : "N/A";
        document.getElementById("res-roi").textContent = `${data.annualRoiPercent}% ao ano`;
        document.getElementById("res-verdict").textContent = data.financialVerdict;
    }

    function setupCostAndDiagram() {
        document.getElementById("cost-form").addEventListener("submit", async e => {
            e.preventDefault();
            await calculateCosts();
            announce("Cálculo financeiro atualizado.");
        });

        const detailBox = document.getElementById("diagram-detail");
        document.querySelectorAll(".svg-node").forEach(node => {
            const show = () => {
                const info = diagramDetails[node.dataset.node];
                if (info) {
                    detailBox.innerHTML = `<strong>${escapeHtml(info.title)}</strong><p>${escapeHtml(info.body)}</p>`;
                }
            };
            node.addEventListener("click", show);
            node.addEventListener("keydown", e => {
                if (e.key === "Enter" || e.key === " ") {
                    e.preventDefault();
                    show();
                }
            });
        });
    }

    // --- Unidade VI: Apresentação & Exportação ---
    function setupPresentation() {
        const contentEl = document.getElementById("slide-content");
        const counterEl = document.getElementById("slide-counter");

        function renderSlide() {
            const s = slides[state.slideIndex];
            contentEl.innerHTML = `
                <h3>${escapeHtml(s.title)}</h3>
                <ul class="checklist">${s.points.map(pt => `<li>${escapeHtml(pt)}</li>`).join("")}</ul>
            `;
            counterEl.textContent = `Slide ${state.slideIndex + 1} de ${slides.length}`;
        }

        document.getElementById("btn-prev-slide").addEventListener("click", () => {
            state.slideIndex = (state.slideIndex - 1 + slides.length) % slides.length;
            renderSlide();
        });
        document.getElementById("btn-next-slide").addEventListener("click", () => {
            state.slideIndex = (state.slideIndex + 1) % slides.length;
            renderSlide();
        });

        document.getElementById("btn-export-md").addEventListener("click", () => {
            const p = state.project;
            const c = state.costSummary;
            const u = state.usabilitySummary;
            const sec = state.securityResult;
            if (!p) return;

            const md = [
                `# Projeto Integrador — Solução Remota (${p.organizationName})`,
                `**Setor:** ${p.sector} | **Colaboradores:** ${p.totalEmployees} (${p.remoteEmployees} remotos)`,
                `\n## Resumo Executivo\n${p.executiveSummary}`,
                `\n## Necessidades Levantadas`,
                ...p.needs.map(n => `- **${n.id} (${n.category} - ${n.priority}):** ${n.problem} -> *Solução:* ${n.proposedSolution}`),
                `\n## Usabilidade & Acessibilidade (Unidade IV)`,
                u ? `- **Índice SUS Médio:** ${u.averageSusScore}/100 (${u.maturityGrade})` : "",
                `\n## Segurança Remota (Unidade V)`,
                sec ? `- **Score de Maturidade:** ${sec.scorePercent}% (${sec.postureLevel})` : "",
                `\n## Análise Financeira (Unidade VI)`,
                c ? `- **CAPEX:** ${formatBrl(c.totalCapexBrl)} | **OPEX Mensal:** ${formatBrl(c.monthlyOpexBrl)} | **Payback:** ${c.paybackMonths} meses | **ROI:** ${c.annualRoiPercent}%` : ""
            ].join("\n");

            const blob = new Blob([md], { type: "text/markdown;charset=utf-8" });
            const url = URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = "projeto-integrador-solucao-remota.md";
            a.click();
            URL.revokeObjectURL(url);
        });

        renderSlide();
    }

    // --- Inicialização ---
    async function init() {
        setupTabs();
        setupAccessibilityControls();
        setupContrastChecker();
        setupUsabilityForm();
        setupQosForm();
        setupSecurityForm();
        setupCostAndDiagram();
        setupPresentation();

        await Promise.all([
            checkSystemHealth(),
            loadProject(),
            loadUsability(),
            runQosSimulation(),
            runSecurityAssessment(),
            calculateCosts()
        ]);
    }

    init();
})();
