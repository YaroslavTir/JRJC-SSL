import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author yaroslavTir.
 */
public class Main {

    private static final String URL = "https://jira.atlassian.com";
    private static final String ADMIN_USERNAME = "yaroslavTir@gmail.com";
    private static final String ADMIN_PASSWORD = "password";

    public static void main(String[] args) throws Exception {
        setProps();
        JiraRestClient client = getJiraRestClient();
        getIssue(client, "CLOUD-7967");
        client.close();
    }

    private static void setProps() {
        Properties systemProps = System.getProperties();
        String path = Main.class.getResource("jira.jks").getPath();
        systemProps.put("javax.net.ssl.trustStore", path);
//        systemProps.put("javax.net.debug", "all");
    }

    private static JiraRestClient getJiraRestClient() throws URISyntaxException {
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        JiraRestClient client = factory.createWithBasicHttpAuthentication(new URI(URL), ADMIN_USERNAME, ADMIN_PASSWORD);
        SearchRestClient searchClient = client.getSearchClient();
        return client;
    }

    private static void getIssue(JiraRestClient client, String key) throws ExecutionException, InterruptedException {
        IssueRestClient issueClient = client.getIssueClient();
        Promise<Issue> issuePromise = issueClient.getIssue(key);
        Issue issue = issuePromise.claim();
        issue.getUpdateDate();
        System.out.println(issue.getId());
    }


}
