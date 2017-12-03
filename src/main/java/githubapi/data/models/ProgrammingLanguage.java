package githubapi.data.models;

public class ProgrammingLanguage {
    private long id;
    private String languageName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
    public ProgrammingLanguage(long id, String languageName) {

        this.id = id;
        this.languageName = languageName;
    }
}
