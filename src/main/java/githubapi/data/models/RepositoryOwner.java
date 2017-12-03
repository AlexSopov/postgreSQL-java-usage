package githubapi.data.models;

public class RepositoryOwner {
    private long gitHubUserId;
    private long gitHubRepositoryId;

    public RepositoryOwner(long gitHubUserId, long gitHubRepositoryId) {

        this.gitHubUserId = gitHubUserId;
        this.gitHubRepositoryId = gitHubRepositoryId;
    }

    public long getGitHubUserId() {
        return gitHubUserId;
    }
    public void setGitHubUserId(long gitHubUserId) {
        this.gitHubUserId = gitHubUserId;
    }

    public long getGitHubRepositoryId() {
        return gitHubRepositoryId;
    }
    public void setGitHubRepositoryId(long gitHubRepositoryId) {
        this.gitHubRepositoryId = gitHubRepositoryId;
    }
}
