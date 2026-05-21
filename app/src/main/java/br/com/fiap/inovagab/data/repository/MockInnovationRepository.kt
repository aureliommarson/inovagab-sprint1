package br.com.fiap.inovagab.data.repository

import br.com.fiap.inovagab.data.model.CorporateProject
import br.com.fiap.inovagab.data.model.InnovationIdea
import br.com.fiap.inovagab.data.model.StrategicGuideline

class MockInnovationRepository : InnovationRepository {

    // Lista em memória simulando as diretrizes estratégicas enviadas pela alta liderança corporativa
    private val guidelinesList = mutableListOf(
        StrategicGuideline("1", "Logística Verde", "Reduzir em 15% a emissão de carbono nas frotas de carga."),
        StrategicGuideline("2", "Check-in Digital", "Digitalizar o embarque de passageiros para mitigar filas nas rodoviárias.")
    )

    // Lista em memória das dores e ideias enviados pelos colaboradores operacionais
    // Inclui as datas de criação e aprovação para permitir o rastreamento completo do funil de inovação
    private val ideasList = mutableListOf(
        InnovationIdea("1", "App Check-list de Bordo", "Substituir pranchetas por tablets na vistoria preventiva dos ônibus.", "Carlos Souza", "Pendente", "Passageiros", "18/05/2026"),
        InnovationIdea("2", "Roteirização por Telemetria", "Otimizar combustível integrando tráfego e peso da carga.", "Fernanda Lima", "Aprovado", "Logística", "19/05/2026", "20/05/2026")
    )

    // Lista em memória que armazena os projetos ativos controlados pelo nível tático (Gestores)
    private val projectsList = mutableListOf(
        CorporateProject("1", "Biometria nos Terminais", "Piloto de embarque seguro por reconhecimento facial.", "Em Execução", 45000.0, 0.0, "30/08/2026", 0),
        CorporateProject("2", "Eco-Rotas V1", "Redução de trechos vazios em frotas interestaduais.", "Concluído", 20000.0, 85000.0, "15/05/2026", 28)
    )

    // Uso de .toList() gera uma nova referência/cópia da lista na memória.
    // Isso força o StateFlow do ViewModel a entender que o estado mudou, atualizando a UI do Compose imediatamente.
    override suspend fun fetchGuidelines(): List<StrategicGuideline> = guidelinesList.toList()

    // Insere uma nova macro-meta definida pela diretoria executiva
    override suspend fun createGuideline(guideline: StrategicGuideline): Boolean {
        guidelinesList.add(guideline)
        return true
    }

    // Remove uma diretriz estratégica da lista de exibição global da empresa
    override suspend fun deleteGuideline(id: String): Boolean {
        guidelinesList.removeIf { it.id == id }
        return true
    }

    // Retorna uma cópia limpa da lista para atualizar instantaneamente a tela do Operador ao enviar dados
    override suspend fun fetchIdeas(): List<InnovationIdea> = ideasList.toList()

    // Cadastra uma nova dor/oportunidade identificada no dia a dia operacional
    override suspend fun insertIdea(idea: InnovationIdea): Boolean {
        ideasList.add(idea)
        return true
    }

    // Atualiza o status do funil da ideia e insere a data de homologação atual quando aprovado pelo Gestor
    override suspend fun updateIdeaStatus(id: String, newStatus: String): Boolean {
        val index = ideasList.indexOfFirst { it.id == id }
        return if (index != -1) {
            ideasList[index] = ideasList[index].copy(
                status = newStatus,
                // Registra a data de aprovação atual de forma fixa na simulação da Sprint 1
                approvedAt = if (newStatus == "Aprovado") "20/05/2026" else null
            )
            true
        } else false
    }

    // Retorna uma cópia da lista de projetos para recalcular reativamente o ROI na visão do Líder ]
    override suspend fun fetchProjects(): List<CorporateProject> = projectsList.toList()

    // Cria e vincula uma iniciativa ativa a partir de uma ideia aprovada na curadoria
    override suspend fun insertProject(project: CorporateProject): Boolean {
        projectsList.add(project)
        return true
    }

    // Edita e atualiza as métricas financeiras, ganho de produtividade e status do projeto
    override suspend fun updateProjectMetrics(
        id: String, investment: Double, financialReturn: Double, productivityGain: Int, status: String
    ): Boolean {
        val index = projectsList.indexOfFirst { it.id == id }
        return if (index != -1) {
            // Clona o projeto alterando os campos modificados pelo formulário do Gestor
            projectsList[index] = projectsList[index].copy(
                investment = investment,
                financialReturn = financialReturn,
                productivityGain = productivityGain,
                status = status
            )
            true
        } else false
    }
}