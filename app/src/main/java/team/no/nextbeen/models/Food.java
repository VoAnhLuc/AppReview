package team.no.nextbeen.models;

public class Food {
    private String district;
    private int amount;
    public String getDistrict() {
        return district;
    }
    public Food()
    {

    }
    public Food(String district,int amount)
    {
        this.district=district;
        this.amount=amount;
    }

    public int getAmount() {
        return amount;
    }
}
