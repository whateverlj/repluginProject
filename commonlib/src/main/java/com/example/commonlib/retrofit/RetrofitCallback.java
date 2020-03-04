package  com.example.commonlib.retrofit;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }
}
