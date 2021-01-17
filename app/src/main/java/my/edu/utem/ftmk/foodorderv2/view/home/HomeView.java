package my.edu.utem.ftmk.foodorderv2.view.home;

import java.util.List;

import my.edu.utem.ftmk.foodorderv2.model.Categories;
import my.edu.utem.ftmk.foodorderv2.model.Meals;

public interface HomeView {
    void showLoading();
    void hideLoading();
    void setMeal(List<Meals.Meal> meal);
    void setCategory(List<Categories.Category> category);
    void onErrorLoading(String message);
}
