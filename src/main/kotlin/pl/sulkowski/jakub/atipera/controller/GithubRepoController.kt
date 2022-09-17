package pl.sulkowski.jakub.atipera.controller

import org.springframework.web.bind.annotation.*
import pl.sulkowski.jakub.atipera.model.GithubUserModel
import pl.sulkowski.jakub.atipera.model.RepositoryModel
import pl.sulkowski.jakub.atipera.service.GithubApiService

@RestController
@RequestMapping("/github")
class GithubRepoController(
    private val githubApiService: GithubApiService
) {

    @PostMapping("/repos")
    fun getGithubUserRepos(@RequestBody() user: GithubUserModel): List<RepositoryModel> {
        return githubApiService.getUserReposByUsername(user.username)
    }
}