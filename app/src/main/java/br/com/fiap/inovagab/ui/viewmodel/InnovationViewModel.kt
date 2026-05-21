package br.com.fiap.inovagab.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.inovagab.data.model.CorporateProject
import br.com.fiap.inovagab.data.model.InnovationIdea
import br.com.fiap.inovagab.data.model.StrategicGuideline
import br.com.fiap.inovagab.data.repository.InnovationRepository
import br.com.fiap.inovagab.data.repository.MockInnovationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InnovationViewModel(
    private val repository: InnovationRepository = MockInnovationRepository()
) : ViewModel() {

    private val _ideas = MutableStateFlow<List<InnovationIdea>>(emptyList())
    val ideas: StateFlow<List<InnovationIdea>> = _ideas

    private val _guidelines = MutableStateFlow<List<StrategicGuideline>>(emptyList())
    val guidelines: StateFlow<List<StrategicGuideline>> = _guidelines

    private val _projects = MutableStateFlow<List<CorporateProject>>(emptyList())
    val projects: StateFlow<List<CorporateProject>> = _projects

    init {
        refreshAllData()
    }

    fun refreshAllData() {
        viewModelScope.launch {
            _ideas.value = repository.fetchIdeas()
            _guidelines.value = repository.fetchGuidelines()
            _projects.value = repository.fetchProjects()
        }
    }

    // Registra uma nova ideia no banco de dados com status Pendente
    fun sendNewIdea(title: String, desc: String, author: String, category: String) {
        viewModelScope.launch {
            val idea = InnovationIdea(
                id = (ideas.value.size + 1).toString(),
                title = title,
                description = desc,
                author = author,
                status = "Pendente",
                category = category,
                createdAt = "20/05/2026"
            )
            if (repository.insertIdea(idea)) refreshAllData()
        }
    }

    // Altera o status da ideia para Aprovado e cria um novo projeto corporativo
    fun approveIdea(idea: InnovationIdea) {
        viewModelScope.launch {
            repository.updateIdeaStatus(idea.id, "Aprovado")

            val project = CorporateProject(
                id = (projects.value.size + 1).toString(),
                title = idea.title,
                description = idea.description,
                status = "Planejamento",
                investment = 0.0,
                financialReturn = 0.0,
                executionDeadline = "A definir",
                productivityGain = 0
            )
            repository.insertProject(project)
            refreshAllData()
        }
    }

    // Altera o status da ideia para Recusado e atualiza a interface
    fun rejectIdea(idea: InnovationIdea) {
        viewModelScope.launch {
            repository.updateIdeaStatus(idea.id, "Recusado")
            refreshAllData()
        }
    }

    // Grava as novas métricas de progresso e finanças do projeto
    fun updateProjectValues(id: String, investment: Double, finReturn: Double, prodGain: Int, status: String) {
        viewModelScope.launch {
            if (repository.updateProjectMetrics(id, investment, finReturn, prodGain, status)) {
                refreshAllData()
            }
        }
    }

    // Insere uma nova diretriz estratégica
    fun publishGuideline(title: String, desc: String) {
        viewModelScope.launch {
            val newGuideline = StrategicGuideline(
                id = (guidelines.value.size + 1).toString(),
                title = title,
                description = desc
            )
            if (repository.createGuideline(newGuideline)) refreshAllData()
        }
    }

    // Exclui uma diretriz estratégica pelo seu ID
    fun removeGuideline(id: String) {
        viewModelScope.launch {
            if (repository.deleteGuideline(id)) refreshAllData()
        }
    }
}