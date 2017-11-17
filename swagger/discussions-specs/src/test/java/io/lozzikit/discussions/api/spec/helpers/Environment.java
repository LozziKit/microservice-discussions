package io.lozzikit.discussions.api.spec.helpers;

import io.lozzikit.discussions.api.CommentsApi;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Olivier Liechti on 24/06/17.
 */
public class Environment {

    private CommentsApi api = new CommentsApi();

    public Environment() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("environment.properties"));
        String url = properties.getProperty("io.lozzikit.discussions.server.url");
        api.getApiClient().setBasePath(url);
    }

    public CommentsApi getApi() {
        return api;
    }


}
