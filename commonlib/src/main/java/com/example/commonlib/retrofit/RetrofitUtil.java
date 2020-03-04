package  com.example.commonlib.retrofit;



import com.example.commonlib.Constants;
import com.example.commonlib.util.GsonUtils;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitUtil {

    public static Retrofit mRetrofit;
    private static OkHttpClient mClient;

    public static Retrofit getRetrofit(String token) {

        if (mRetrofit == null) {
//            UserNoInterceptor userNoInterceptor = new UserNoInterceptor.Builder()
//                    .addParam("currentUser", userNo)
//                    .addQueryParam("currentUser", userNo)
//                    .build();
            OkHttpClient.Builder  buider=getUnSaveBuilder();
            mClient = buider
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
//                    .addInterceptor(userNoInterceptor)
                    .build();

            mRetrofit = new Retrofit.Builder().baseUrl(Constants.SERVER_URL)
                    .client(mClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(GsonUtils.gson))
                    .build();
        }
        return mRetrofit;
    }

    public static <T> T getService(Class clazz,String token) {
        return (T) getRetrofit(token).create(clazz);
    }



    private static OkHttpClient.Builder getUnSaveBuilder() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
