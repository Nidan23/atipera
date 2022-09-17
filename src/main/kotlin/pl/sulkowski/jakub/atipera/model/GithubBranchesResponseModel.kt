package pl.sulkowski.jakub.atipera.model

data class GithubBranchResponseModel(
    val name: String,
    val commit: CommitModel
)

data class CommitModel(
    val sha: String,
)