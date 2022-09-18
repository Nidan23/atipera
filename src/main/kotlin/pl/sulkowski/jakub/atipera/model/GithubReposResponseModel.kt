package pl.sulkowski.jakub.atipera.model

data class GithubReposResponseModel(
    val name: String,
    val owner: RepoOwner,
    var branches_url: String,
    val fork: Boolean,
)

data class RepoOwner(
    val login: String
)
