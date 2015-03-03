package org.sherlok.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.sherlok.mappings.SherlokResult;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Client for Sherlok server.
 * 
 * @author renaud@apache.org
 */
public class SherlokClient {

    public static final String DEFAULT_HOST = "http://localhost";
    public static final int DEFAULT_PORT = 9600;

    private final String postUrl;
    private ObjectMapper mapper;
    private CloseableHttpClient client;

    /**
     * Creates a client with {@link #DEFAULT_PORT} 9600 and
     * {@link SherlokClient#DEFAULT_HOST} localhost.
     */
    public SherlokClient() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    /**
     * @param host
     * @param port
     */
    public SherlokClient(String host, int port) {
        this.postUrl = host + ":" + port + "/annotate/";
        this.mapper = new ObjectMapper();
        this.client = HttpClientBuilder.create().build();
    }

    /**
     * @param pipeline
     *            the name of the pipeline (no version)
     * @param text
     *            the text to annotate
     * @return a {@link SherlokResult} containing the found annotations
     */
    public SherlokResult annotate(String pipeline, String text)
            throws SherlokClientException {
        return annotate(pipeline, null, text);
    }

    /**
     * @param pipeline
     *            the name of the pipeline
     * @param version
     *            the version of the pipeline
     * @param text
     *            the text to annotate
     * @return a {@link SherlokResult} containing the found annotations
     */
    public SherlokResult annotate(String pipeline, String version, String text)
            throws SherlokClientException {
        String json = annotateRaw(pipeline, version, text);
        try {
            return mapper.readValue(json, SherlokResult.class);
        } catch (Exception e) {
            throw new SherlokClientException("could not read Sherlok JSON: "
                    + e.getMessage());
        }
    }

    /**
     * @param pipeline
     *            the name of the pipeline (no version)
     * @param text
     *            the text to annotate
     * @return the raw JSON from the Sherlok server
     */
    public String annotateRaw(String pipeline, String text)
            throws SherlokClientException {
        return annotateRaw(pipeline, null, text);
    }

    /**
     * @param pipeline
     *            the name of the pipeline
     * @param version
     *            the version of the pipeline
     * @param text
     *            the text to annotate
     * @return the raw JSON from the Sherlok server
     */
    public String annotateRaw(String pipeline, String version, String text)
            throws SherlokClientException {

        try {
            HttpPost post = new HttpPost(postUrl + pipeline);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("version", version == null ? "null"
                    : version));
            nvps.add(new BasicNameValuePair("text", text));
            post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();

            String responseString = EntityUtils.toString(entity, "UTF-8");
            if (response.getStatusLine().getStatusCode() > 250) {
                throw new SherlokClientException(
                        "Could not annotate (error code "
                                + response.getStatusLine().getStatusCode()
                                + ") " + responseString);
            }
            return responseString;
        } catch (HttpHostConnectException e) {
            throw new SherlokClientException(
                    "could not connect to Sherlok server at '" + postUrl + "'");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);// should not happend
        } catch (ClientProtocolException e) {
            throw new SherlokClientException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);// should not happend
        }
    }
}
