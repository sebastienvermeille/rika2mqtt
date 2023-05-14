/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.firenet.api;

import dev.cookiecode.rika2mqtt.rika.firenet.model.Auth;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveStatus;
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
