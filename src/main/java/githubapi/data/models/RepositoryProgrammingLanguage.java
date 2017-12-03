package githubapi.data.models;

public class RepositoryProgrammingLanguage {
    private long repositoryId;
    private long programmingLanguageId;

    public RepositoryProgrammingLanguage(long repositoryId, long programmingLanguageId) {
        this.repositoryId = repositoryId;
        this.programmingLanguageId = programmingLanguageId;
    }

    public long getRepositoryId() {

        return repositoryId;
    }
    public void setRepositoryId(long repositoryId) {
        this.repositoryId = repositoryId;
    }

    public long getProgrammingLanguageId() {
        return programmingLanguageId;
    }
    public void setProgrammingLanguageId(long programmingLanguageId) {
        this.programmingLanguageId = programmingLanguageId;
    }
}
