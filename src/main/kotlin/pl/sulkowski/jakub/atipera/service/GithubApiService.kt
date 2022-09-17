package pl.sulkowski.jakub.atipera.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import pl.sulkowski.jakub.atipera.domain.*
import pl.sulkowski.jakub.atipera.model.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service()
class GithubApiService {

    @Value("\${githubUserToken}")
    private lateinit var githubApiToken: String
    private val gson = Gson()
    private val httpBuilder = HttpClient.newHttpClient()
    private val reposTypeToken = object : TypeToken<List<GithubReposResponseModel>>() {}.type
    private val branchesTypeToken = object : TypeToken<List<GithubBranchResponseModel>>() {}.type

    fun getUserReposByUsername(username: String): List<RepositoryModel> {
        val githubRepoResponse = getUserRepos(username)

        validateResponse(githubRepoResponse)

        val githubRepoResponseBody = githubRepoResponse.body()
        val rawRepos = gson.fromJson<List<GithubReposResponseModel>>(githubRepoResponseBody, reposTypeToken)
            .filter { repo ->
                repo.owner.login == username
            }

        return aggregateDataForReturn(rawRepos)
    }

    private fun getUserRepos(username: String): HttpResponse<String> =
        httpBuilder.send(
            httpGetRequestBuilder("$GITHUB_MAIN_URL/$GITHUB_USERS_PATH/$username/$GITHUB_REPOS_PATH"),
            HttpResponse.BodyHandlers.ofString()
        )


    private fun getReposBranches(branchUrl: String): HttpResponse<String> =
        httpBuilder.send(httpGetRequestBuilder(branchUrl), HttpResponse.BodyHandlers.ofString())


    private fun httpGetRequestBuilder(url: String): HttpRequest {
        val targetUrl = URI.create(url)

        return HttpRequest.newBuilder().GET()
            .header(HTTP_HEADER_AUTHORIZATION, "$HTTP_HEADER_AUTHORIZATION_VALUE_BEARER $githubApiToken")
            .header(HTTP_HEADER_ACCEPT, HTTP_HEADER_ACCEPT_VALUE_GITHUB)
            .uri(targetUrl)
            .build()
    }

    // Refactor -> != SOLID :/ (No better idea 4 method name & design :/ )
    private fun aggregateDataForReturn(rawRepos: List<GithubReposResponseModel>): List<RepositoryModel> =
         rawRepos.map { rawRepo ->
            rawRepo.branches_url = rawRepo.branches_url.substring(0, rawRepo.branches_url.length - GITHUB_BRANCH_URL_SUFFIX.length)

            val githubBranchesResponse = getReposBranches(rawRepo.branches_url).body()
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