package githubapi.data.models;

import org.json.JSONObject;

public class User {
    private long id;
    private String name;
    private String profileUrl;

    public User(long id, String name, int commitsCount, String profileUrl) {
        this.id = id;
        this.name = name;
        this.profileUrl = profileUrl;
    }
    public User(JSONObject gitHubContributorJson) {
        this.name = gitHubContributorJson.getString("login");
        this.profileUrl = gitHubContributorJson.getString("html_url");
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

    public String getProfileUrl() {
        return profileUrl;
    }
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
