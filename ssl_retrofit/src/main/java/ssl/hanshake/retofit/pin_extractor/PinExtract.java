package ssl.hanshake.retofit.pin_extractor;

import android.annotation.SuppressLint;
import android.util.Base64;
import ssl.hanshake.retofit.exceptions.HandShakeError;
import ssl.hanshake.retofit.exceptions.HandShakeException;

import java.security.MessageDigest;
import java.security.cert.X509Certificate;

public class PinExtract {

    private X509Certificate certificate = null;
    private String keyInstance = null;

    private PinExtract() {}


    @SuppressLint("StaticFieldLeak")
    private static PinExtract extract = null;

    static PinExtract getExtractor() {
        if (extract == null) {
            extract = new PinExtract();
        }

        return extract;
    }


    public String getPublicKey() throws HandShakeException {

        try {
            byte[] publicKeyEncoded = certificate.getPublicKey().getEncoded();
            MessageDigest messageDigest = MessageDigest.getInstance(keyInstance);
            byte[] publicKeySha256 = messageDigest.digest(publicKeyEncoded);
            byte[] publicKeyShaBase64 = Base64.encode(publicKeySha256, Base64.NO_WRAP);
            return keyInstance + "/" + new String(publicKeyShaBase64);
        } catch (Exception e) {
            throw new HandShakeException(HandShakeError.PUBLIC_KEY_EXTRACT_ERROR);
        }

    }


    void setCertificate(X509Certificate certificate) {
        this.certificate = certificate;
    }

    X509Certificate getCertificate() {
        return this.certificate;
    }

    void setKeyInstance(String keyInstance) {
        this.keyInstance = keyInstance;
    }
}
