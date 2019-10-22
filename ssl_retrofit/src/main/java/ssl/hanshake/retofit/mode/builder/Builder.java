package ssl.hanshake.retofit.mode.builder;

import ssl.hanshake.retofit.exceptions.HandShakeException;
import ssl.hanshake.retofit.mode.HandShakeMode;

public abstract class Builder {

    private String pinUrl;

    Builder(String pinUrl) {
        this.pinUrl = pinUrl;
    }

    public String getPinUrl() {
        return this.pinUrl;
    }

    abstract HandShakeMode build() throws HandShakeException;


}
