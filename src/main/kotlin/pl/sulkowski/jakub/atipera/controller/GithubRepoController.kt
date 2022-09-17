package pl.sulkowski.jakub.atipera.controller

import org.springframework.web.bind.annotation.*;
import pl.sulkowski.jakub.atipera.model.BranchModel
import pl.sulkowski.jakub.atipera.model.GithubUserModel
import pl.sulkowski.jakub.atipera.model.RepositoryModel

@RestController
@RequestMapping("/github")
class GithubRepoController {

    @PostMapping("/repos")
    fun getGithubUserRepos(@RequestBody() user: GithubUserModel): RepositoryModel {
        return RepositoryModel(user.username, "", arrayOf(BranchModel(user.username, "")))
    }
}