package githubapi.data.converters;

import githubapi.data.models.GitHubCommit;
import githubapi.data.models.GitHubUser;
import githubapi.data.models.ProgrammingLanguage;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonObjectToModelConverter {

    public static GitHubUser convertToGitHubUser(JSONObject userJson) {
        int userId = userJson.getInt("id");
        String name = userJson.getString("login");

        return new GitHubUser(userId, name);
    }

    public static GitHubCommit convertToGitHubCommit(JSONObject commitJson, int repositoryId) {
        String sha = commitJson.getString("sha");
        String message = commitJson.getJSONObject("commit").getString("message");
        String dateString = commitJson.getJSONObject("commit").getJSONObject("committer").getString("date");

        GitHubUser committer = convertToGitHubUser(commitJson.getJSONObject("committer"));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat iso8601DateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal.setTime(iso8601DateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        GitHubCommit result = new GitHubCommit(
                sha,
                committer.getId(),
                repositoryId,
                cal,
                message
        );
        result.setCommitter(committer);

        return result;
    }

    public static List<ProgrammingLanguage> convertToProgrammingLanguagesList(JSONObject languagesJson) {
        List<ProgrammingLanguage> programmingLanguages = new ArrayList<>();
        String valueJsonAsString = languagesJson.getString("value");

        Pattern programmingLanguageInQuotesPattern = Pattern.compile("\"(.*?)\"");
        Matcher programmingLanguageInQuotesMatcher = programmingLanguageInQuotesPattern.matcher(valueJsonAsString);

        while (programmingLanguageInQuotesMatcher.find()) {
            programmingLanguages.add(new ProgrammingLanguage(programmingLanguageInQuotesMatcher.group(0).replace("\"", "")));
        }

        return programmingLanguages;
    }
}
