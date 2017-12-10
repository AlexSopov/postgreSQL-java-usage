package githubapi.data.models;

public class RepositoryProgrammingLanguage {
    private int repositoryId;
    private int programmingLanguageId;

    public RepositoryProgrammingLanguage(int repositoryId, int programmingLanguageId) {
        this.repositoryId = repositoryId;
        this.programmingLanguageId = programmingLanguageId;
    }

    public int getRepositoryId() {

        return repositoryId;
    }
    public void setRepositoryId(int repositoryId) {
        this.repositoryId = repositoryId;
    }

    public int getProgrammingLanguageId() {
        return programmingLanguageId;
    }
    public void setProgrammingLanguageId(int programmingLanguageId) {
        this.programmingLanguageId = programmingLanguageId;
    }
}
