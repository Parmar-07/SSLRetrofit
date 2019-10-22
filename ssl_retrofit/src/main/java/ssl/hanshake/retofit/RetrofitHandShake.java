package ssl.hanshake.retofit;


import okhttp3.OkHttpClient;
import ssl.hanshake.retofit.client.HandShakeClient;
import ssl.hanshake.retofit.exceptions.HandShakeError;
import ssl.hanshake.retofit.exceptions.HandShakeException;
import ssl.hanshake.retofit.mode.HandShakeMode;

public class RetrofitHandShake {

    private HandShakeMode mode;


    private RetrofitHandShake(HandShakeMode mode) {
        this.mode = mode;
    }


    private static RetrofitHandShake handShake = null;


    public static RetrofitHandShake mode(HandShakeMode mode) {
        if (handShake == null) {
            synchronized (RetrofitHandShake.class) {
                if (handShake == null) {
                    handShake = new RetrofitHandShake(mode);
                }
            }
        }

        return handShake;
    }


    public HandShakeMode getHandShakeMode() {
        return mode;
    }


    public OkHttpClient handshake(OkHttpClient client) throws HandShakeException {

        if (getHandShakeMode().getMode() == HandShakeMode.PIN_MODE
                && getHandShakeMode().getPinBuilder().getPinUrl().isEmpty()) {
            throw new HandShakeException(HandShakeError.NO_URL_FOUND);
        } else if (getHandShakeMode().getMode() != HandShakeMode.PIN_MODE
                && getHandShakeMode().getMode() != HandShakeMode.UN_PIN_MODE
        ) {
            throw new HandShakeException(HandShakeError.NO_MODE_FOUND);
        } else if (getHandShakeMode().getPinBuilder().getPinKey().isEmpty()
                && getHandShakeMode().getMode() == HandShakeMode.PIN_MODE) {
            throw new HandShakeException(HandShakeError.NO_PUBLIC_KEY_FOUND);
        } else {
            return new HandShakeClient(client, this).handshake();
        }


    }


}
