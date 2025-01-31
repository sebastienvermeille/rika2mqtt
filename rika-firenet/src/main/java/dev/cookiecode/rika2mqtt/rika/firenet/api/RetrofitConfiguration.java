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

import static java.util.concurrent.TimeUnit.SECONDS;

import com.google.gson.Gson;
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
  public RikaFirenetApi createRikaFirenetApi(Gson gson) {
    final var cookieHandler = new CookieManager();

    final var httpClient =
        new OkHttpClient.Builder()
            .connectTimeout(60, SECONDS)
            .readTimeout(60, SECONDS)
            .writeTimeout(60, SECONDS)
            .cookieJar(new JavaNetCookieJar(cookieHandler))
            .build();

    final var retrofit =
        new Retrofit.Builder()
            .baseUrl(rikaFirenetApiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build();

    return retrofit.create(RikaFirenetApi.class);
  }

  @Bean
  public Gson gson() {
    return new GsonBuilder().setLenient().create();
  }
}
