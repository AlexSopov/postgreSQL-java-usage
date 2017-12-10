package githubapi.interactivity;

import githubapi.data.converters.JsonObjectToModelConverter;
import githubapi.data.models.GitHubCommit;
import githubapi.data.models.GitHubUser;
import githubapi.data.models.GitHubRepository;
import githubapi.data.models.ProgrammingLanguage;
import javafx.util.Pair;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubApiInteraction {

    private final String GITHUB_OAUTH_TOKEN;

    public GitHubApiInteraction() {
        GITHUB_OAUTH_TOKEN = System.getenv("GITHUB_OAUTH_TOKEN");
    }

    public void getRepositoriesInfo(Consumer<GitHubRepository> onRepositoryDataGathered, int maxRepositoriesCount) throws URISyntaxException, IOException {
        URI requestUri = getBaseUriToGitHubApi()
                .setPath("search/repositories")
                .setParameter("q", "stars:>0 created:>2017-11-10")
                .setParameter("sort", "stars")
                .setParameter("order", "desc")
                .build();

        Pair<String, String> currentResponse;
        JSONObject currentJsonObject;
        JSONArray currentRepositories;
        GitHubRepository processingGitHubRepository;
        int processedRepositoriesCount = 0;

        while (requestUri != null && processedRepositoriesCount < maxRepositoriesCount) {
            currentResponse = getResponse(requestUri);

            currentJsonObject = new JSONObject(currentResponse.getValue());
            currentRepositories = currentJsonObject.getJSONArray("items");

            for (int i = 0; i < currentRepositories.length() && processedRepositoriesCount < maxRepositoriesCount; i++, processedRepositoriesCount++) {
                currentJsonObject = currentRepositories.getJSONObject(i);

                processingGitHubRepository = gatherRepositoryInfo(currentJsonObject);
                onRepositoryDataGathered.accept(processingGitHubRepository);
            }

            requestUri = matchNextResponsePage(currentResponse.getKey());
        }
    }

    private GitHubRepository gatherRepositoryInfo(JSONObject repositoryJson) throws IOException, URISyntaxException {
        // Get data properties values
        int repositoryId =  getRepositoryId(repositoryJson);
        String repositoryName = getRepositoryName(repositoryJson);
        int repositoryStarsCount = getRepositoryStarsCount(repositoryJson);
        String repositoryDescription = getRepositoryDescription(repositoryJson);

        // Get navigation properties data
        GitHubUser repositoryOwner = getRepositoryOwner(repositoryJson);
        List<GitHubCommit> repositoryCommits = getRepositoryCommits(repositoryJson);
        List<ProgrammingLanguage> repositoryProgrammingLanguages = getRepositoryProgrammingLanguages(repositoryName);

        // Create instande of GitHubRepository
        GitHubRepository gatheredResult = new GitHubRepository(
                repositoryId,
                repositoryName,
                repositoryOwner.getId(),
                repositoryStarsCount,
                repositoryDescription
        );

        gatheredResult.setRepositoryOwner(repositoryOwner);
        gatheredResult.setRepositoryCommits(repositoryCommits);
        gatheredResult.setRepositoryProgrammingLanguages(repositoryProgrammingLanguages);

        return gatheredResult;
    }

    private int getRepositoryId(JSONObject repositoryJson) {
        return repositoryJson.getInt("id");
    }
    private String getRepositoryName(JSONObject repositoryJson) {
        return repositoryJson.getString("full_name");
    }
    private int getRepositoryStarsCount(JSONObject repositoryJson) {
        return repositoryJson.getInt("stargazers_count");
    }
    private String getRepositoryDescription(JSONObject repositoryJson) {
        return repositoryJson.getString("description");
    }

    private GitHubUser getRepositoryOwner(JSONObject repositoryJson) {
        return JsonObjectToModelConverter.convertToGitHubUser(repositoryJson.getJSONObject("owner"));
    }
    private List<GitHubCommit> getRepositoryCommits(JSONObject repositoryJson) throws URISyntaxException, IOException {
        String repositoryName = getRepositoryName(repositoryJson);
        int repositoryId = getRepositoryId(repositoryJson);

        URI repositoryCommitsRequest = getBaseUriToGitHubApi()
                .setPath(String.format("repos/%s/commits", repositoryName))
                .build();

        Pair<String, String> currentResponse;
        JSONObject currentCommitJsonObject;
        JSONArray array;
        List<GitHubCommit> commitsToRepository = new ArrayList<>();
        GitHubCommit currentCommit;

        while (repositoryCommitsRequest != null) {
            currentResponse = getResponse(repositoryCommitsRequest);
            array = new JSONArray(currentResponse.getValue());


            for (Object commit : array) {
                currentCommitJsonObject = (JSONObject) commit;
                if (currentCommitJsonObject.isNull("committer")) {
                    continue;
                }

                currentCommit = JsonObjectToModelConverter.convertToGitHubCommit(currentCommitJsonObject, repositoryId);
                commitsToRepository.add(currentCommit);
            }

            repositoryCommitsRequest = matchNextResponsePage(currentResponse.getKey());
        }

        return commitsToRepository;
    }
    private List<ProgrammingLanguage> getRepositoryProgrammingLanguages(String repositoryName) throws URISyntaxException, IOException {
        URI repositoryCommitsRequest = getBaseUriToGitHubApi()
                .setPath(String.format("repos/%s/languages", repositoryName))
                .build();

        JSONObject programmingLanguagesResponse = new JSONObject(getResponse(repositoryCommitsRequest));
        return JsonObjectToModelConverter.convertToProgrammingLanguagesList(programmingLanguagesResponse);
    }

    /**
     * Executes specified request and reads response to used structures.
     * @param requestUri Request to be executed.
     * @return Pair: data from Link header and response body, converted to String
     * @throws ClientProtocolException if response code was not in range [200, 300)
     */
    private Pair<String, String> getResponse(URI requestUri) throws IOException {

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet getRequest = new HttpGet(requestUri);
            getRequest.setHeader("Accept", "application/vnd.github.v3+json");
            getRequest.setHeader("Accept", "application/vnd.github.cloak-preview");
            getRequest.setHeader("Authorization", "token " + GITHUB_OAUTH_TOKEN);

            System.out.println("Executing request " + getRequest.getRequestLine());

            ResponseHandler<Pair<String, String>> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();

                if (status >= 200 && status < 300) {
                    HttpEntity responseBodyEntity = response.getEntity();
                    String responseBody = responseBodyEntity != null ? EntityUtils.toString(responseBodyEntity) : null;
                    Header responseHeader = response.getFirstHeader("Link");
                    String responseHeaderValue = responseHeader != null ? responseHeader.getValue() : null;

                    return new Pair<>(responseHeaderValue, responseBody);
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            return httpclient.execute(getRequest, responseHandler);
        }
    }

    private URIBuilder getBaseUriToGitHubApi() throws URISyntaxException {
        return new URIBuilder()
                .setScheme("https")
                .setHost("api.github.com")
                .setParameter("per_page", "100");
    }
    private URI matchNextResponsePage(String linkHeader) throws URISyntaxException {
        if (linkHeader == null || linkHeader.isEmpty()) {
            return null;
        }

        Pattern headerNextResponsePagePattern = Pattern.compile("<(.*)>; rel=\"next\"");
        Matcher headerNextResponsePatternMatcher = headerNextResponsePagePattern.matcher(linkHeader);

        if (headerNextResponsePatternMatcher.find()) {
            return new URI(headerNextResponsePatternMatcher.group(1));
        }
        else {
            return null;
        }
    }
}
