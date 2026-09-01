public class Electronics extends Item {
    public Electronics(String id, String name, int quantity, long price) {
        super(id, name, quantity, price);
    }

    @Override
    public String getCategory() {
        return "Electronics";
    }
}