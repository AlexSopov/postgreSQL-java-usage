package githubapi.interop;

import githubapi.data.models.User;
import githubapi.data.models.Repository;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubApiInterop {

    private final String GITHUB_OAUTH_TOKEN;

    public GitHubApiInterop() {
        GITHUB_OAUTH_TOKEN = System.getenv("GITHUB_OAUTH_TOKEN");
    }

    public void gatherRepositroiesInfo(int maxRepositories) throws URISyntaxException, IOException {
        URI requestUri = getBaseUriToGitHubApi()
                .setPath("search/repositories")
                .setParameter("q", "stars:>0")
                .setParameter("sort", "stars")
                .setParameter("order", "desc")
                .build();

        Pair<String, String> currentResponse;
        JSONObject currentJsonObject;
        JSONArray currentRepositories;
        int processedRepositoriesCount = 0;

        while (requestUri != null && processedRepositoriesCount < maxRepositories) {
            currentResponse = getResponse(requestUri);

            currentJsonObject = new JSONObject(currentResponse.getValue());
            currentRepositories = currentJsonObject.getJSONArray("items");

            for (int i = 0; i < currentRepositories.length() && processedRepositoriesCount < maxRepositories; i++) {
                gatherRepositoryInfo(currentRepositories.getJSONObject(i));
                processedRepositoriesCount++;
            }

            requestUri = matchNextResponsePage(currentResponse.getKey());
            processedRepositoriesCount += currentRepositories.length();
        }
    }

    private void gatherRepositoryInfo(JSONObject repositoryJson) {
        // Get owner
        // Create repository, with owner
        // Gather programming languages
        // Gather commits: autrhor and commit info for each

        //gatherRepositoryOwnerInfo(repositoryJson);

    }
    private User gatherRepositoryOwnerInfo(JSONObject repositoryJson) {
        return null;
    }
    private void gatherRepositoryProgrammingLanguages(Repository repository) {
        //TODO
    }
    private void gatherRepositoryCommits(Repository repository) throws URISyntaxException, IOException {
        URI repositoryCommitsRequest = getBaseUriToGitHubApi()
                .setPath(String.format("repos/%s/commits", repository.getName()))
                .build();

        Pair<String, String> currentResponse;
        JSONObject currentCommitJsonObject;

        while (repositoryCommitsRequest != null) {
            currentResponse = getResponse(repositoryCommitsRequest);
            currentCommitJsonObject = new JSONObject(currentResponse.getValue());

            for (Object commit : currentCommitJsonObject.getJSONArray("items")) {
                currentCommitJsonObject = (JSONObject) commit;

                if (currentCommitJsonObject.isNull("committer")) {
                    continue;
                }
                currentCommitJsonObject = currentCommitJsonObject.getJSONObject("committer");
            }

            repositoryCommitsRequest = matchNextResponsePage(currentResponse.getKey());
        }
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
