package pl.sulkowski.jakub.atipera.model

data class RepositoryModel(
    val name: String,
    val ownerName: String,
    val branches: List<BranchModel>?
)
