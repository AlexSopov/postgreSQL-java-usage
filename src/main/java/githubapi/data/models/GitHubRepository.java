package githubapi.data.models;

import java.util.List;

public class GitHubRepository {
    private int id;
    private String name;
    private int ownerId;
    private int starsCount;
    private String description;

    // Navigation properties
    private GitHubUser repositoryOwner;
    private List<GitHubCommit> repositoryCommits;
    private List<ProgrammingLanguage> repositoryProgrammingLanguages;

    public GitHubRepository(int id, String name, int ownerId, int starsCount, String description) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.starsCount = starsCount;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getStarsCount() {
        return starsCount;
    }
    public void setStarsCount(int starsCount) {
        this.starsCount = starsCount;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public GitHubUser getRepositoryOwner() {
        return repositoryOwner;
    }
    public void setRepositoryOwner(GitHubUser repositoryOwner) {
        this.repositoryOwner = repositoryOwner;
    }

    public List<GitHubCommit> getRepositoryCommits() {
        return repositoryCommits;
    }
    public void setRepositoryCommits(List<GitHubCommit> repositoryCommits) {
        this.repositoryCommits = repositoryCommits;
    }

    public List<ProgrammingLanguage> getRepositoryProgrammingLanguages() {
        return repositoryProgrammingLanguages;
    }
    public void setRepositoryProgrammingLanguages(List<ProgrammingLanguage> repositoryProgrammingLanguages) {
        this.repositoryProgrammingLanguages = repositoryProgrammingLanguages;
    }
}
