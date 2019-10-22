package ssl.hanshake.retofit.client;

import android.annotation.SuppressLint;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import ssl.hanshake.retofit.RetrofitHandShake;
import ssl.hanshake.retofit.exceptions.HandShakeError;
import ssl.hanshake.retofit.exceptions.HandShakeException;
import ssl.hanshake.retofit.mode.HandShakeMode;

import javax.net.ssl.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class HandShakeClient {
    private OkHttpClient.Builder clientBuilder;
    private RetrofitHandShake handShake;

    public HandShakeClient(OkHttpClient okhttpClient, RetrofitHandShake handShake) {
        this.clientBuilder = okhttpClient.newBuilder();
        this.handShake = handShake;
    }

    public OkHttpClient handshake() throws HandShakeException {

        if (handShake.getHandShakeMode().getMode() == HandShakeMode.PIN_MODE) {
            return pin();
        } else if (handShake.getHandShakeMode().getMode() == HandShakeMode.UN_PIN_MODE) {
            return byPass();
        } else {
            throw new HandShakeException(HandShakeError.NO_MODE_FOUND);
        }

    }


    private OkHttpClient pin() throws HandShakeException {

        try {
            URL url = new URL(handShake.getHandShakeMode().getPinBuilder().getPinUrl());
            final String hostName = url.getHost();
            CertificatePinner certPin = new CertificatePinner.Builder()
                    .add(hostName, handShake.getHandShakeMode().getPinBuilder().getPinKey())
                    .build();

            clientBuilder.certificatePinner(certPin);
            clientBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return hostName.equals(s);
                }
            });
        } catch (MalformedURLException e) {
            throw new HandShakeException(HandShakeError.INVALID_URL);
        }


        return clientBuilder.build();
    }

    @SuppressLint({"TrustAllX509TrustManager", "BadHostnameVerifier"})
    private OkHttpClient byPass() throws HandShakeException {

        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        try {
            SSLContext sslContext = SSLContext.getInstance(handShake.getHandShakeMode().getUnPinBuilder().getSslVersion());
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory;
            if (handShake.getHandShakeMode().getUnPinBuilder().isTlsVersionsEnabled())
                sslSocketFactory = new TLSSocketFactory(sslContext, handShake.getHandShakeMode().getUnPinBuilder().getTlsVersions());
            else
                sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (NoSuchAlgorithmException e) {
            throw new HandShakeException(HandShakeError.CERTIFICATE_ALGORITHM_ERROR);
        } catch (KeyManagementException e) {
            throw new HandShakeException(HandShakeError.CERTIFICATE_KEY_ERROR);
        }


        return clientBuilder.build();
    }

}
