package githubapi.data.models;

import org.json.JSONObject;

public class Repository {
    private String name;
    private String description;
    private String language;
    private String url;
    private int starsCount;
    private long id;

    public Repository(long id, String name, String description, String language, String url, int starsCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.language = language;
        this.url = url;
        this.starsCount = starsCount;
    }
    public Repository(JSONObject gitHubRepositoryJson) {
        this.name = gitHubRepositoryJson.getString("full_name");
        this.description = gitHubRepositoryJson.isNull("description") ? "Description is not specified" : gitHubRepositoryJson.getString("description");
        this.language = gitHubRepositoryJson.isNull("language") ? "Language is not specified" : gitHubRepositoryJson.getString("language");
        this.url = gitHubRepositoryJson.getString("html_url");
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public int getStarsCount() {
        return starsCount;
    }
    public void setStarsCount(int starsCount) {
        this.starsCount = starsCount;
    }
}
