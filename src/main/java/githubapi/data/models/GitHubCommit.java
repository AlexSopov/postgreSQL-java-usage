package githubapi.data.models;

import java.util.Calendar;

public class GitHubCommit {
    private String sha;
    private int committerId;
    private int repositoryId;
    private Calendar commitDate;
    private String message;

    private GitHubUser committer;

    public GitHubCommit(String sha, int committerId, int repositoryId, Calendar commitDate, String message) {
        this.sha = sha;
        this.committerId = committerId;
        this.repositoryId = repositoryId;
        this.commitDate = commitDate;
        this.message = message;
    }

    public String getSha() {
        return sha;
    }
    public void setSha(String sha) {
        this.sha = sha;
    }

    public int getCommitterId() {
        return committerId;
    }
    public void setCommitterId(int committerId) {
        this.committerId = committerId;
    }

    public int getRepositoryId() {
        return repositoryId;
    }
    public void setRepositoryId(int repositoryId) {
        this.repositoryId = repositoryId;
    }

    public Calendar getCommitDate() {
        return commitDate;
    }
    public void setCommitDate(Calendar commitDate) {
        this.commitDate = commitDate;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public GitHubUser getCommitter() {
        return committer;
    }
    public void setCommitter(GitHubUser committer) {
        this.committer = committer;
    }
}
