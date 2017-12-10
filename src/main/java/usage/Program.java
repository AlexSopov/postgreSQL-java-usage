package usage;

import analyze.PostgreSqlInteraction;
import githubapi.data.models.GitHubRepository;

import java.sql.SQLException;

public class Program {
    public static void main(String[] args) {

        try {
            new PostgreSqlInteraction();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

//        GitHubApiInteraction gitHubApiInteraction = new GitHubApiInteraction();
//        try {
//            gitHubApiInteraction.getRepositoriesInfo(Program::dosmth, 50);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private static void dosmth(GitHubRepository repository) {
        System.out.println(repository);
    }
}
