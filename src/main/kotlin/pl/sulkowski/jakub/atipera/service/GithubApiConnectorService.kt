package pl.sulkowski.jakub.atipera.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pl.sulkowski.jakub.atipera.domain.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service()
class GithubApiConnectorService {
    @Value("\${githubUserToken}")
    private lateinit var githubApiToken: String
    private val httpBuilder = HttpClient.newHttpClient()

    fun getUserRepos(username: String, page: Int): HttpResponse<String> =
        httpBuilder.send(
            httpGetRequestBuilder("$GITHUB_MAIN_URL/$GITHUB_USERS_PATH/$username/$GITHUB_REPOS_PATH?$GITHUB_TYPE_QUERY_PARAM=all&$GITHUB_PER_PAGE_QUERY_PARAM=100&$GITHUB_PAGE_QUERY_PARAM=$page"),
            HttpResponse.BodyHandlers.ofString()
        )


    fun getReposBranches(branchUrl: String): HttpResponse<String> =
        httpBuilder.send(httpGetRequestBuilder(branchUrl), HttpResponse.BodyHandlers.ofString())


    private fun httpGetRequestBuilder(url: String): HttpRequest {
        val targetUrl = URI.create(url)

        return HttpRequest.newBuilder().GET()
            .header(HTTP_HEADER_AUTHORIZATION, "$HTTP_HEADER_AUTHORIZATION_VALUE_BEARER $githubApiToken")
            .header(HTTP_HEADER_ACCEPT, HTTP_HEADER_ACCEPT_VALUE_GITHUB)
            .uri(targetUrl)
            .build()
    }
}