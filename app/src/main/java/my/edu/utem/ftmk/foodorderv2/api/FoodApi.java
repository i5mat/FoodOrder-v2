package my.edu.utem.ftmk.foodorderv2.api;

import my.edu.utem.ftmk.foodorderv2.model.Categories;
import my.edu.utem.ftmk.foodorderv2.model.Meals;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodApi {

    @GET("random.php")
    Call<Meals> getMeal();

    @GET("categories.php")
    Call<Categories> getCategories();

    @GET("filter.php")
    Call<Meals> getMealByCategory(@Query("c") String category);

    @GET("search.php")
    Call<Meals> getMealByName(@Query("s") String mealName);
}
