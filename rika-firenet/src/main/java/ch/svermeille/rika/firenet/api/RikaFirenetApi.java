package ch.svermeille.rika.firenet.api;

import ch.svermeille.rika.firenet.model.Auth;
import ch.svermeille.rika.firenet.model.StoveStatus;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Sebastien Vermeille
 */
public interface RikaFirenetApi {
  @GET("/api/client/{stoveId}/status")
  @Headers("Content-Type: application/json")
  Call<StoveStatus> getStoveStatus(@Path("stoveId") String stoveId);

  @POST("/web/login")
  Call<ResponseBody> authenticate(@Body Auth credentials);

  @GET("/web/logout")
  Call<ResponseBody> logout();

  @GET("/web/summary")
  Call<ResponseBody> getStoves();
}
