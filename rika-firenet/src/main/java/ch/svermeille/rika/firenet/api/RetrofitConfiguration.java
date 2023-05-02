/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.svermeille.rika.firenet.api;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.google.gson.GsonBuilder;
import java.net.CookieManager;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Sebastien Vermeille
 */
@Component
@Configuration
public class RetrofitConfiguration {

  @Value("${rika.url:https://www.rika-firenet.com}")
  private String rikaFirenetApiBaseUrl;

  @Bean
  public RikaFirenetApi createRikaFirenetApi() {
    final var cookieHandler = new CookieManager();

    final var gson = new GsonBuilder()
        .setLenient()
        .create();

    final var httpClient = new OkHttpClient.Builder()
        .connectTimeout(60, SECONDS)
        .readTimeout(60, SECONDS)
        .writeTimeout(60, SECONDS)
        .cookieJar(new JavaNetCookieJar(cookieHandler))
        .build();

    final var retrofit = new Retrofit.Builder()
        .baseUrl(rikaFirenetApiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build();

    return retrofit.create(RikaFirenetApi.class);
  }

}
