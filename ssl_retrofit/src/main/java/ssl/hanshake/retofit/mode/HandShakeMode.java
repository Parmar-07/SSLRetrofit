package ssl.hanshake.retofit.mode;

import ssl.hanshake.retofit.exceptions.HandShakeException;
import ssl.hanshake.retofit.mode.builder.Builder;
import ssl.hanshake.retofit.mode.builder.PinBuilder;
import ssl.hanshake.retofit.mode.builder.UnPinBuilder;

public abstract class HandShakeMode {

    public final static short PIN_MODE = 1;
    public final static short UN_PIN_MODE = 2;
    private short mode = -1;
    private PinBuilder pinBuilder;
    private UnPinBuilder unPinBuilder;


    public abstract HandShakeMode getHandShakeMode(Builder modeBuilder) throws HandShakeException;


    public short getMode() {
        return mode;
    }

    public void setMode(short mode) {
        this.mode = mode;
    }

    public PinBuilder getPinBuilder() {
        return pinBuilder;
    }

    public UnPinBuilder getUnPinBuilder() {
        return unPinBuilder;
    }

    public void setUnPinBuilder(UnPinBuilder unPinBuilder) {
        this.unPinBuilder = unPinBuilder;
    }

    public void setPinBuilder(PinBuilder pinBuilder) {
        this.pinBuilder = pinBuilder;
    }
}