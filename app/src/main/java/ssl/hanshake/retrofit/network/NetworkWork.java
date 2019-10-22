package ssl.hanshake.retrofit.network;

import android.os.AsyncTask;

public abstract class NetworkWork<T> extends AsyncTask<Void, Void, T> {


    NetworkListener<T> networkListener;

    public void call(NetworkListener<T> networkListener) {
        this.networkListener = networkListener;
        super.execute();

    }
}
