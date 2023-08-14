/*
 * The MIT License
 * Copyright © 2022 Sebastien Vermeille
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package dev.cookiecode.rika2mqtt.rika.firenet.api;

import dev.cookiecode.rika2mqtt.rika.firenet.model.Auth;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveStatus;
import dev.cookiecode.rika2mqtt.rika.firenet.model.UpdatableControls;
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

  /**
   * Typed api call (safer)
   *
   * @param stoveId
   * @param fields
   * @return
   */
  @POST("/api/client/{stoveId}/controls")
  Call<ResponseBody> updateControls(
      @Path("stoveId") String stoveId, @Body UpdatableControls fields);
}
