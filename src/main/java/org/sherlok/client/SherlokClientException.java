package org.sherlok.client;

import org.apache.http.client.ClientProtocolException;

/**
 * An exception thrown by {@link SherlokClient}
 * 
 * @author renaud@apache.org
 */
@SuppressWarnings("serial")
public class SherlokClientException extends Exception {

    public SherlokClientException(String msg) {
        super(msg);
    }

    public SherlokClientException(ClientProtocolException e) {
        super(e);
    }

}
