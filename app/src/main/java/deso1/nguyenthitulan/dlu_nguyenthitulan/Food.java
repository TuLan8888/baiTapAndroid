package deso1.nguyenthitulan.dlu_nguyenthitulan;

public class Food {

    private String key;
    private String image;
    private String name;
    private String price;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Food(){};
    public Food(String key, String image, String name, String price) {
        this.key = key;
        this.image = image;
        this.name = name;
        this.price = price;
    }
}

