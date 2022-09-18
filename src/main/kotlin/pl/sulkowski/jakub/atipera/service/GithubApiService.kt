package pl.sulkowski.jakub.atipera.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import pl.sulkowski.jakub.atipera.domain.*
import pl.sulkowski.jakub.atipera.model.*
import java.net.http.HttpResponse

@Service()
class GithubApiService(
    private val githubApiConnectorService: GithubApiConnectorService
) {
    private val gson = Gson()
    private val reposTypeToken = object : TypeToken<List<GithubReposResponseModel>>() {}.type
    private val branchesTypeToken = object : TypeToken<List<GithubBranchResponseModel>>() {}.type

    fun getUserReposByUsername(username: String): List<RepositoryModel> {
        var pageCounter = 1
        var githubRepoResponse: HttpResponse<String>
        var githubRepoResponseBody = ""
        val rawRepos: MutableList<GithubReposResponseModel> = mutableListOf()

        while(githubRepoResponseBody != GITHUB_EMPTY_RESPONSE) {
            githubRepoResponse = githubApiConnectorService.getUserRepos(username, pageCounter)
            githubRepoResponseBody = githubRepoResponse.body()

            if (githubRepoResponseBody == GITHUB_EMPTY_RESPONSE)
                break

            validateResponse(githubRepoResponse)
            rawRepos.addAll(gson.fromJson<List<GithubReposResponseModel>>(githubRepoResponseBody, reposTypeToken)
                .filter { repo ->
                    !repo.fork
                } as MutableList<GithubReposResponseModel>)
            pageCounter++
        }

        return aggregateDataForReturn(rawRepos)
    }

    // Refactor -> != SOLID :/ (No better idea 4 method name & design :/ )
    private fun aggregateDataForReturn(rawRepos: List<GithubReposResponseModel>): List<RepositoryModel> =
         rawRepos.map { rawRepo ->
            rawRepo.branches_url = rawRepo.branches_url.substring(0, rawRepo.branches_url.length - GITHUB_BRANCH_URL_SUFFIX.length)

            val githubBranchesResponse = githubApiConnectorService.getReposBranches(rawRepo.branches_url).body()
            val rawBranches = gson.fromJson<List<GithubBranchResponseModel>>(githubBranchesResponse, branchesTypeToken)

            val branches: List<BranchModel> = rawBranches.map { rawBranch ->
                BranchModel(rawBranch.name, rawBranch.commit.sha)
            }

            RepositoryModel(rawRepo.name, rawRepo.owner.login, branches)
        }

    private fun validateResponse(githubRepoResponse: HttpResponse<String>) {
        if (githubRepoResponse.statusCode() != HttpStatus.OK.value())
            throw ResponseStatusException(HttpStatus.valueOf(githubRepoResponse.statusCode()), githubRepoResponse.body())
    }
}