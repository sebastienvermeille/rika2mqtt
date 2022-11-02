package ch.svermeille.rika.health.service.api;

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
public class MavenCentralRetrofitConfiguration {

  @Value("${maven.search.url:https://search.maven.org}")
  private String mavenSearchApiBaseUrl;

  @Bean
  public MavenCentralSearchApi createMavenCentralSearchApi() {
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
        .baseUrl(this.mavenSearchApiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build();

    return retrofit.create(MavenCentralSearchApi.class);
  }

}
