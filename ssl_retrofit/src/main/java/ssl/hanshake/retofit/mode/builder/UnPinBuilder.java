package ssl.hanshake.retofit.mode.builder;


import ssl.hanshake.retofit.exceptions.HandShakeException;
import ssl.hanshake.retofit.mode.HandShakeMode;
import ssl.hanshake.retofit.mode.UnPinMode;

public class UnPinBuilder extends Builder {

    private String sslVersion = "SSL";
    private String[] tlsVersions = null;
    private boolean enabedTLS = false;

    private UnPinBuilder() {
        super("");
    }

    public static UnPinBuilder newBuilder() {
        return new UnPinBuilder();
    }

    public UnPinBuilder enableTLSVersion(String... versions) {
        this.enabedTLS = true;
        sslVersion = "TLS";
        tlsVersions = versions;
        return this;
    }

    public String getSslVersion() {
        return sslVersion;
    }

    public String[] getTlsVersions() {
        return tlsVersions;
    }

    public boolean isTlsVersionsEnabled() {
        return this.enabedTLS;
    }

    @Override
    public HandShakeMode build() throws HandShakeException {
        return new UnPinMode().getHandShakeMode(this);
    }

}
