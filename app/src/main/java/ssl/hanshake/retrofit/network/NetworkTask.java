package ssl.hanshake.retrofit.network;

import android.annotation.SuppressLint;
import android.app.Activity;

import java.lang.ref.WeakReference;

public class NetworkTask<T> extends NetworkWork<T> {

    private WeakReference<Activity> wActivity;

    private NetworkTask(Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private static NetworkTask task = null;


    public static <T> NetworkTask<T> taskResponse(Class<T> clazz) {
        task = new NetworkTask<>(clazz);
        return task;
    }

    public NetworkTask<T> from(Activity activity) {
        wActivity = new WeakReference<>(activity);
        return task;
    }




    @Override
    protected void onCancelled() {
        if (task != null) {
            task = null;
        }
        super.onCancelled();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        networkListener.preCall();
    }

    @Override
    protected T doInBackground(Void... voids) {
        try {
            return networkListener.onNetworkCall();
        } catch (Exception e) {
            onError(e);
            return null;
        }

    }

    private void onError(final Exception e) {

        if (wActivity != null) {
            wActivity.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    networkListener.error(e);

                }
            });
        }
        cancel(true);
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        if (t != null)
            networkListener.postCall(t);
        else
            networkListener.error(new NullPointerException());

        cancel(true);
    }
}
