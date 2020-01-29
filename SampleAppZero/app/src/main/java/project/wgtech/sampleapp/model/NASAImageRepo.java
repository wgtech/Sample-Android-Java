package project.wgtech.sampleapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * EXAMPLE <br>
 *
 * {<br>
 * "copyright": "Eric Holland",<br>
 * "date": "2019-04-02",<br>
 * "explanation": "What's that unusual spot on the Moon? It's the International Space Station. Using precise timing, the Earth-orbiting space platform was photographed in front of a partially lit gibbous Moon last month. The featured image was taken from Palo Alto, California, USA with an exposure time of only 1/667 of a second. In contrast, the duration of the transit of the ISS across the entire Moon was about half a second.  A close inspection of this unusually crisp ISS silhouette will reveal the outlines of numerous solar panels and trusses.  The bright crater Tycho is visible on the lower left, as well as comparatively rough, light colored terrain known as highlands, and relatively smooth, dark colored areas known as maria.  On-line tools can tell you when the International Space Station will be visible from your area.   Newly Added Venue:  APOD now available on Instagram in Persian",<br>
 * "hdurl": "https://apod.nasa.gov/apod/image/1904/IssMoon_Holland_1063.jpg",<br>
 * "media_type": "image",<br>
 * "service_version": "v1",<br>
 * "title": "Space Station Silhouette on the Moon",<br>
 * "url": "https://apod.nasa.gov/apod/image/1904/IssMoon_Holland_960.jpg"<br>
 * }<br>
 *
 */
public class NASAImageRepo implements Serializable {

    /**
     * EXAMPLES <br>
     * "copyright": "Eric Holland",<br>
     */
    @SerializedName("copyright") public String copyright;

    /**
     * EXAMPLE <br>
     * "date": "2019-04-02",<br>
     */
    @SerializedName("date") public String date;

    /**
     * EXAMPLE <br>
     * "explanation": "What's that unusual spot on the Moon? It's the International Space Station. Using precise timing, the Earth-orbiting space platform was photographed in front of a partially lit gibbous Moon last month. The featured image was taken from Palo Alto, California, USA with an exposure time of only 1/667 of a second. In contrast, the duration of the transit of the ISS across the entire Moon was about half a second.  A close inspection of this unusually crisp ISS silhouette will reveal the outlines of numerous solar panels and trusses.  The bright crater Tycho is visible on the lower left, as well as comparatively rough, light colored terrain known as highlands, and relatively smooth, dark colored areas known as maria.  On-line tools can tell you when the International Space Station will be visible from your area.   Newly Added Venue:  APOD now available on Instagram in Persian",<br>
     */
    @SerializedName("explanation") public String explanation;

    /**
     * EXAMPLE <br>
     * "hdurl": "https://apod.nasa.gov/apod/image/1904/IssMoon_Holland_1063.jpg",<br>
     */
    @SerializedName("hdurl") public String hdurl;

    /**
     * EXAMPLE <br>
     * "media_type": "image",<br>
     */
    @SerializedName("media_type") public String media_type;

    /**
     * EXAMPLE <br>
     * "service_version": "v1",<br>
     */
    @SerializedName("service_version") public String service_version;

    /**
     * EXAMPLE <br>
     * "title": "Space Station Silhouette on the Moon",<br>
     */
    @SerializedName("title") public String title;

    /**
     * EXAMPLE <br>
     * "url": "https://apod.nasa.gov/apod/image/1904/IssMoon_Holland_960.jpg"<br>
     */
    @SerializedName("url") public String url;


    public interface NASAApiInterface {
        @Headers({"Accept: application/json"})
        @GET("planetary/apod")
        Call<NASAImageRepo> get_NASA_images(@Query("date") String date, @Query("api_key") String api_key);
    }



}
