package ncrc.nise.ajou.ac.kr.opa.scheduler;

/**
 * Created by AJOU on 2016-01-05.
 */
public class FoodData {
    private String foodName;
    private String type;
    private Double kcal;

    public FoodData(String foodName, String type, Double kcal) {
        this.foodName = foodName;
        this.type = type;
        this.kcal = kcal;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Double getKcal() {
        return kcal;
    }

    public void setKcal(Double kcal) {
        this.kcal = kcal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
