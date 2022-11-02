package ch.svermeille.rika.health.service.api;

import ch.svermeille.rika.health.service.api.dto.LastVersionResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @author Sebastien Vermeille
 */
public interface MavenCentralSearchApi {
  @GET("/solrsearch/select?wt=json&rows=2")
  @Headers("Content-Type: application/json")
  Call<LastVersionResponse> getLastVersions(@Query(value = "q", encoded = true) String queryString);
}

