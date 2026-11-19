# NexusRemote — Projeto Integrador: Desenvolvimento de uma Solução Remota

Aplicação Web desenvolvida em **Java 21 + Spring Boot 3.3.5** que implementa uma plataforma interativa para o **Projeto Integrador (Unidades IV, V e VI)**, atendendo aos requisitos de Usabilidade, Acessibilidade Digital (WCAG 2.1 AA), Redes Convergentes, Qualidade de Serviço (QoS), Segurança Remota, Análise de Custos/ROI, Diagrama da Solução e Apresentação Executiva.

---

## Tecnologias Utilizadas

- **Backend:** Java 21, Spring Boot 3.3.5 (Spring Web), Maven 3.9+
- **Frontend:** HTML5 Semântico (WCAG 2.1 AA), CSS3 Responsivo (com temas Alto Contraste e Modo Baixa Velocidade), JavaScript Vanilla
- **Testes:** JUnit 5, Spring Boot Test, MockMvc

---

## Pré-requisitos

- **Java JDK 21+**
- **Apache Maven 3.8+**

---

## Como Executar

1. Na raiz do projeto, inicie a aplicação com o Maven:

```bash
mvn spring-boot:run
```

2. Acesse no navegador:

```text
http://localhost:8080
```

3. Para executar a suíte de testes automatizados (unitários e integração):

```bash
mvn test
```

---

## Estrutura do Projeto

```text
src/
├── main/
│   ├── java/com/solucoesremotas/
│   │   ├── Application.java                  # Classe principal Spring Boot
│   │   ├── controller/
│   │   │   └── SolutionApiController.java    # Endpoints REST (/api/*)
│   │   ├── model/                            # Records de entrada/saída da API
│   │   └── service/
│   │       ├── ProjectDataService.java       # Dados do Projeto Integrador (Unidade VI)
│   │       ├── UsabilityService.java         # Avaliações heurísticas e índice SUS (Unidade IV)
│   │       ├── QosCalculatorService.java     # Simulador de QoS, R-Factor e MOS (Unidade V)
│   │       ├── SecurityService.java          # Diagnóstico de postura Zero Trust (Unidade V)
│   │       └── CostCalculatorService.java    # Calculadora de CAPEX, OPEX, Payback e ROI (Unidade VI)
│   └── resources/
│       ├── application.properties            # Configuração da porta (8080)
│       └── static/
│           ├── index.html                    # Interface acessível (WCAG 2.1 AA)
│           ├── css/styles.css                # Estilos responsivos, Alto Contraste e Baixa Velocidade
│           └── js/app.js                     # Lógica SPA, verificador WCAG, SVG e exportação
└── test/java/com/solucoesremotas/
    ├── SolutionServicesTest.java             # Testes unitários dos serviços
    └── SolutionApiControllerTest.java        # Testes de integração dos endpoints REST
```

---

## Endpoints da API REST

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/api/health` | Verifica disponibilidade e status da aplicação |
| `GET` | `/api/project` | Retorna necessidades, personas, infraestrutura, ferramentas e políticas |
| `GET` | `/api/usability/evaluations` | Lista avaliações de usabilidade e calcula índice SUS médio |
| `POST` | `/api/usability/evaluations` | Registra uma nova avaliação heurística / teste com usuário |
| `POST` | `/api/network/qos-simulate` | Calcula `R-Factor`, `MOS`, impacto em VoIP/vídeo e recomendações |
| `POST` | `/api/security/assess` | Avalia controles de segurança remota e retorna score de maturidade |
| `POST` | `/api/costs/calculate` | Calcula CAPEX, OPEX, economia líquida, ROI anual e payback |

---

## Mapeamento dos Requisitos da Atividade

### Unidade IV — Políticas de Usabilidade em Soluções Remotas (4.1 a 4.5)
- **Fundamentos e Princípios de Design (4.1 - 4.2):** Interface consistente, feedback imediato, prevenção de erros, visibilidade do estado do sistema (indicador Online/Offline) e design responsivo.
- **Usabilidade em Soluções Remotas (4.3):** Botão de **Modo Baixa Velocidade** para conexões lentas e tolerância a falhas.
- **Acessibilidade Digital / WCAG 2.1 AA (4.4):** Barra de acessibilidade com **Alto Contraste**, ajuste de fonte (`A-` / `A+`), navegação completa por teclado (`Tab`, `Skip Link`, atalhos `Alt+1..6`), regiões `ARIA` para leitores de tela e **Verificador de Contraste WCAG** em tempo real.
- **Avaliação de Usabilidade (4.5):** Formulário de avaliação heurística e testes com usuários persistido via API (`/api/usability/evaluations`), com cálculo automático do **índice SUS (0–100)**.

### Unidade V — Redes Convergentes e Soluções Remotas (5.1 a 5.6)
- **Redes Convergentes & Protocolos Multimídia (5.1 - 5.3):** Mapeamento de VLANs (Dados, Voz, Vídeo), VoIP (SIP, RTP/SRTP, RTCP), WebRTC e Streaming.
- **Simulador de QoS (5.4):** Cálculo de **R-Factor** e **MOS (1.0–4.5)** via `/api/network/qos-simulate` considerando latência, jitter, perda de pacotes, largura de banda e priorização DSCP.
- **Acesso Remoto (5.5):** Matriz comparativa de VPN (WireGuard/IPsec), VDI/Remote Desktop, SSH Bastion Host e Aplicações Web.
- **Segurança de Redes (5.6):** Avaliador de maturidade Zero Trust (`/api/security/assess`) cobrindo Firewall NGFW, VPN criptografada, MFA, segmentação VLAN, RBAC, SIEM, criptografia de endpoint e políticas LGPD.

### Unidade VI — Projeto Integrador (Proposta Completa)
- Levantamento de necessidades, perfis de usuários (personas), infraestrutura em camadas, ferramentas e políticas corporativas (`/api/project`).
- Calculadora financeira de **CAPEX, OPEX, Payback e ROI** (`/api/costs/calculate`).
- Diagrama lógico interativo em SVG da arquitetura da solução.
- Modo **Apresentação em Slides** e **Exportação de Relatório (.md)**.
