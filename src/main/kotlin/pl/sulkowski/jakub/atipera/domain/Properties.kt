package pl.sulkowski.jakub.atipera.domain

// HTTP
const val HTTP_HEADER_ACCEPT: String = "Accept"
const val HTTP_HEADER_ACCEPT_VALUE_GITHUB: String = "application/vnd.github+json"
const val HTTP_HEADER_AUTHORIZATION: String = "Authorization"
const val HTTP_HEADER_AUTHORIZATION_VALUE_BEARER: String = "Bearer"

// Github
const val GITHUB_MAIN_URL: String = "https://api.github.com"
const val GITHUB_USERS_PATH: String = "users"
const val GITHUB_REPOS_PATH: String = "repos"
const val GITHUB_BRANCH_URL_SUFFIX: String = "{/branch}"
const val GITHUB_EMPTY_RESPONSE: String = "[]"
const val GITHUB_TYPE_QUERY_PARAM: String = "type"
const val GITHUB_PER_PAGE_QUERY_PARAM: String = "per_page"
const val GITHUB_PAGE_QUERY_PARAM: String = "page"