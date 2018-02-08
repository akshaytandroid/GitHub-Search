package com.akshay.gitrepoproject;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static junit.framework.Assert.assertEquals;

/**
 * Created by akshaythalakoti on 2/7/18.
 */

public class HttpUrlConnectionTest {


    public String getUrl() {
        return "https://api.github.com/search/repositories?q=akshay&sort=stars&order=desc";
    }

    @Test
    public void testURL() throws Exception {
        String strUrl = getUrl();
        HttpURLConnection urlConn = null;
        try {
            URL url = new URL(strUrl);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();
            assertEquals(HttpURLConnection.HTTP_OK, urlConn.getResponseCode());
        } catch (IOException e) {
            System.err.println("Error creating HTTP connection");
            e.printStackTrace();
            throw e;
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
    }
}
