package in.completecourse.helper;

import in.completecourse.model.Updates;
import retrofit2.Call;
import retrofit2.http.GET;


public interface APIService {

    @GET("updates")
    Call<Updates> getHeroes();
}
