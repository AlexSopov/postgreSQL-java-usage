package githubapi.data.models;

public class ProgrammingLanguage {
    private String languageName;

    public ProgrammingLanguage(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageName() {

        return languageName;
    }
    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
}
