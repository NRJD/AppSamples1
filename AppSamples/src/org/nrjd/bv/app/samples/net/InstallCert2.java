/**
 * Originally posted at: (not available now)
 * http://blogs.sun.com/andreas/resource/InstallCert.java
 * Use:
 * java InstallCert hostname
 * Example:
 * % java InstallCert ecc.fedora.redhat.com
 */
package org.nrjd.bv.app.samples.net;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
//import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
//import org.apache.http.impl.nio.client.HttpAsyncClients;
//import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
//import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
//import org.apache.http.impl.nio.reactor.IOReactorConfig;
//import org.apache.http.nio.conn.NoopIOSessionStrategy;
//import org.apache.http.nio.conn.SchemeIOSessionStrategy;
//import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


public class InstallCert2
{
    private static final int MAX_TOTAL_CONNECTIONS = 40;
    private static final int ROUTE_CONNECTIONS = 40;
    private static final int CONNECT_TIMEOUT = 60000;
    private static final int SOCKET_TIMEOUT = -1;
    private static final int CONNECTION_REQUEST_TIMEOUT = 60000;
    private static final boolean STALE_CONNECTION_CHECK = true;

    public static void main(String[] args) throws Exception
    {

        SSLContext sslcontext = SSLContexts.custom()
                .useTLS()
                .loadTrustMaterial(null, new TrustStrategy()
                {
                    @Override
                    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException
                    {
                        return true;
                    }
                })
                .build();
//        SSLIOSessionStrategy sslSessionStrategy = new SSLIOSessionStrategy(sslcontext, new AllowAll());
//
//        Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder.<SchemeIOSessionStrategy>create()
//                .register("http", NoopIOSessionStrategy.INSTANCE)
//                .register("https", sslSessionStrategy)
//                .build();
//
//        DefaultConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT);
//        PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(ioReactor, sessionStrategyRegistry);
//        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
//        connectionManager.setDefaultMaxPerRoute(ROUTE_CONNECTIONS);
//
//        RequestConfig requestConfig = RequestConfig.custom()
//                .setSocketTimeout(SOCKET_TIMEOUT)
//                .setConnectTimeout(CONNECT_TIMEOUT)
//                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
//                .setStaleConnectionCheckEnabled(STALE_CONNECTION_CHECK)
//                .build();
//
//        CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
//                .setSSLStrategy(sslSessionStrategy)
//                .setConnectionManager(connectionManager)
//                .setDefaultRequestConfig(requestConfig)
//                .build();
//
//        httpClient.start();

        // use httpClient...
    }

    private static class AllowAll implements X509HostnameVerifier
    {
        @Override
        public void verify(String s, SSLSocket sslSocket) throws IOException
        {}

        @Override
        public void verify(String s, X509Certificate x509Certificate) throws SSLException {}

        @Override
        public void verify(String s, String[] strings, String[] strings2) throws SSLException
        {}

        @Override
        public boolean verify(String s, SSLSession sslSession)
        {
            return true;
        }
    }
}
