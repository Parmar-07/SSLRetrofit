package ssl.hanshake.retrofit.network;

public interface NetworkListener<T> {

    void preCall();
    T onNetworkCall()throws Exception;
    void postCall(T result);
    void error(Exception e);

}
