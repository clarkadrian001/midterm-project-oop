public class Clothing extends Item {
    public Clothing(String id, String name, int quantity, long price) {
        super(id, name, quantity, price);
    }

    @Override
    public String getCategory() {
        return "Clothing";
    }
}