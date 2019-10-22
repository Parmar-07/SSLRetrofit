package ssl.hanshake.retofit.mode.builder;

import ssl.hanshake.retofit.exceptions.HandShakeException;
import ssl.hanshake.retofit.mode.HandShakeMode;
import ssl.hanshake.retofit.mode.PinMode;
import ssl.hanshake.retofit.pin_extractor.PinExtract;

public class PinBuilder extends Builder {


    private String pinKey = "";

    private PinBuilder(String pinUrl) {
        super(pinUrl);
    }

    public static PinBuilder newBuilder(String pinUrl) {
        return new PinBuilder(pinUrl);
    }


    public String getPinKey() {
        return pinKey;
    }

    public PinBuilder pinKey(String pinKey) {
        this.pinKey = pinKey;
    return this;
    }


    public PinBuilder pinKey(PinExtract pinExtract) throws HandShakeException {
        this.pinKey = pinExtract.getPublicKey();
    return this;
    }

    @Override
    public HandShakeMode build() throws HandShakeException {
        return new PinMode().getHandShakeMode(this);
    }



}
