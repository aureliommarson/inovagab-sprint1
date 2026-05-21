package br.com.fiap.inovagab.data.repository

import br.com.fiap.inovagab.data.model.CorporateProject
import br.com.fiap.inovagab.data.model.InnovationIdea
import br.com.fiap.inovagab.data.model.StrategicGuideline

interface InnovationRepository {
    suspend fun fetchGuidelines(): List<StrategicGuideline>
    suspend fun createGuideline(guideline: StrategicGuideline): Boolean
    suspend fun deleteGuideline(id: String): Boolean

    suspend fun fetchIdeas(): List<InnovationIdea>
    suspend fun insertIdea(idea: InnovationIdea): Boolean
    suspend fun updateIdeaStatus(id: String, newStatus: String): Boolean

    suspend fun fetchProjects(): List<CorporateProject>
    suspend fun insertProject(project: CorporateProject): Boolean
    suspend fun updateProjectMetrics(id: String, investment: Double, financialReturn: Double, productivityGain: Int, status: String): Boolean
}