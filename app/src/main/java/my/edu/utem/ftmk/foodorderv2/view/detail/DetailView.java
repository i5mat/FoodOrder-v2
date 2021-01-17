package my.edu.utem.ftmk.foodorderv2.view.detail;

import my.edu.utem.ftmk.foodorderv2.model.Meals;

public interface DetailView {
    void showLoading();
    void hideLoading();
    void setMeal(Meals.Meal meal);
    void onErrorLoading(String message);
}
