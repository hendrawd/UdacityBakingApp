package hendraganteng.udacitybakingapp.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Helper class to get retrofit service
 *
 * @author hendrawd on 8/20/17
 */

public class ServiceGenerator {

    // topher/2017/May/59121517_baking/baking.json

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private static OkHttpClient.Builder sHttpClient =
            //can add interceptors here
            new OkHttpClient.Builder();

    private static Retrofit.Builder sBuilder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(sHttpClient.build());

    public static <T> T createService(Class<T> serviceClass) {
        return sBuilder.build().create(serviceClass);
    }
}
