
package com.flash.onlyoffice.domain.util;

import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * 禁用和启用证书和主机名签入
 * HttpsURLConnection，HTTPS/TLS 协议的默认 JVM 实现。
 * 对 Apache Http Client、Ok Http 等实现没有影响。
 * @author zhangsp
 */
@Component
public final class SslUtils {

    private final HostnameVerifier jvmHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();

    private final HostnameVerifier trivialHostnameVerifier = (hostname, sslSession) -> true;

    private final TrustManager[] unquestioningTrustManager = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
                }
            }};

    public void turnOffSslChecking() throws NoSuchAlgorithmException, KeyManagementException {
        HttpsURLConnection.setDefaultHostnameVerifier(trivialHostnameVerifier);
        // 安装全信任的信任管理器
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, unquestioningTrustManager, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    public void turnOnSslChecking() throws KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier(jvmHostnameVerifier);
        // 将其返回到初始状态（通过反射发现，现在已硬编码）
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, null, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

}
