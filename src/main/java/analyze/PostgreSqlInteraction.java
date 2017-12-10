package analyze;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreSqlInteraction implements AutoCloseable{

    private Connection dataBaseConnection;

    public  PostgreSqlInteraction() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        dataBaseConnection = DriverManager.getConnection(
                System.getenv("POSTGRES_HOST"),
                System.getenv("POSTGRES_USER"),
                System.getenv("POSTGRES_PASSWORD"));

        executeInitialScript();
    }

    private void executeInitialScript() {
        try (Statement dataBaseConnectionStatement = dataBaseConnection.createStatement()) {
            dataBaseConnectionStatement.executeUpdate("CREATE DATABASE GitHubData;");
        }
        catch (SQLException e) {
            // Database already exists
            // Ignore exception - process creating tables to database
        }

        // Read initial script from initial.sql
        // Execute script
        try (Statement dataBaseConnectionStatement = dataBaseConnection.createStatement()) {
            URI uriToInitialSqlScript = getClass().getResource("../initial.sql").toURI();
            String initialScriptContent = new String(Files.readAllBytes(Paths.get(uriToInitialSqlScript)));

            dataBaseConnectionStatement.executeUpdate(initialScriptContent);

        } catch (URISyntaxException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void close() throws Exception {
        if (dataBaseConnection != null) {
            dataBaseConnection.close();
        }
    }
}
