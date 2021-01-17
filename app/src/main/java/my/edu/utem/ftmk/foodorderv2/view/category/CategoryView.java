package my.edu.utem.ftmk.foodorderv2.view.category;

import java.util.List;

import my.edu.utem.ftmk.foodorderv2.model.Meals;

public interface CategoryView {
    void showLoading();
    void hideLoading();
    void setMeals(List<Meals.Meal> meals);
    void onErrorLoading(String message);
}
