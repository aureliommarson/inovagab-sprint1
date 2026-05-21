package br.com.fiap.inovagab.data.model

// Os três perfis de acesso obrigatórios determinados pelo desafio do Grupo Águia Branca
enum class UserRole { OPERADOR, GESTOR, LIDER }

// Representa a captura de dores e oportunidades enviadas pela ponta operacional
data class InnovationIdea(
    val id: String,
    val title: String,
    val description: String,
    val author: String,
    val status: String, // "Pendente" ou "Aprovado"
    val category: String, // "Passageiros", "Comércio" ou "Logística"

    // Campos de data para rastrear o tempo de resposta do funil
    val createdAt: String,         // Data em que o operador registrou a ocorrência
    val approvedAt: String? = null // Data de aprovação (começa nula e o gestor preenche)
)

// Mapeia as diretrizes e macro-objetivos publicados pela liderança executiva
data class StrategicGuideline(
    val id: String,
    val title: String,
    val description: String
)

// Mapeia os projetos reais criados a partir das ideias aprovadas na curadoria tática
data class CorporateProject(
    val id: String,
    val title: String,
    val description: String,
    val status: String,          // "Planejamento", "Em Execução" ou "Concluído"
    val investment: Double,      // Capital alocado para a iniciativa
    val financialReturn: Double, // Retorno financeiro real (usado para o cálculo do ROI global)
    val executionDeadline: String, // Prazo estimado de entrega
    val productivityGain: Int    // Ganho médio de eficiência/produtividade em %
)