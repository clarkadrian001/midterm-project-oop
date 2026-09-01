public class Entertainment extends Item {
    public Entertainment(String id, String name, int quantity, long price) {
        super(id, name, quantity, price);
    }

    @Override
    public String getCategory() {
        return "Entertainment";
    }
}