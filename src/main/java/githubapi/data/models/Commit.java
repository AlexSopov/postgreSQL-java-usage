package githubapi.data.models;

import java.util.Calendar;

public class Commit {
    private String sha;
    private int committerId;
    private Calendar commitDate;

    public Commit(String sha, int committerId, Calendar commitDate) {

        this.sha = sha;
        this.committerId = committerId;
        this.commitDate = commitDate;
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

    public Calendar getCommitDate() {
        return commitDate;
    }
    public void setCommitDate(Calendar commitDate) {
        this.commitDate = commitDate;
    }
}
