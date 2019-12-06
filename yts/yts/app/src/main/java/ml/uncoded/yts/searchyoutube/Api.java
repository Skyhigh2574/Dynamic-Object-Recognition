package ml.uncoded.yts.searchyoutube;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {
   String BASE_URL = "http://roadgrievances.herokuapp.com/api/android/";

    @Multipart
    @POST("https://imagescdn.herokuapp.com")
    Call<ImageResponse> uploadImage(@Header("auth") String auth, @Part MultipartBody.Part file1);

}