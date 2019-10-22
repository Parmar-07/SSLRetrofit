package ssl.hanshake.retofit.pin_extractor;

import android.annotation.SuppressLint;
import android.content.Context;
import ssl.hanshake.retofit.exceptions.HandShakeError;
import ssl.hanshake.retofit.exceptions.HandShakeException;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


public class PinExtractor {

    private InputStream inputStream;
    private String keyInstance;
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private static PinExtractor builder = null;

    private PinExtractor() {}

    public static PinExtractor newBuilder(Context mContext, String keyInstance) {
        if (builder == null) {
            builder = new PinExtractor();
        }
        builder.context = mContext;
        builder.keyInstance = keyInstance;
        return builder;
    }


    public PinExtractor open(File file) throws HandShakeException {
        try {
            FileInputStream   fis = new FileInputStream(file.getPath());
            inputStream = new BufferedInputStream(fis);

        } catch (FileNotFoundException e) {
            throw new HandShakeException(HandShakeError.CERTIFICATE_FILE_NOT_FOUND);
        }
        return this;
    }

    public PinExtractor open(String assetsPath) throws HandShakeException {
        try {
            inputStream = context.getResources().getAssets().open(assetsPath);
        } catch (IOException e) {
            throw new HandShakeException(HandShakeError.CERTIFICATE_FILE_NOT_FOUND);

        }
        return this;
    }

    public PinExtractor open(int rawResource) {
        inputStream = context.getResources().openRawResource(rawResource);
        return this;
    }


    public PinExtract build() throws HandShakeException {
        PinExtract extractPublicKey = PinExtract.getExtractor();
        CertificateFactory factory = null;
        try {
            factory = CertificateFactory.getInstance("X.509");
            extractPublicKey.setCertificate((X509Certificate) factory.generateCertificate(inputStream));
            extractPublicKey.setKeyInstance(keyInstance);
            return extractPublicKey;
        } catch (CertificateException e) {
            throw new HandShakeException(HandShakeError.NO_CERTIFICATE_FOUND);
        }

    }


}