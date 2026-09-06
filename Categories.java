class Clothing extends Item {
    Clothing(String id, String name, int quantity, long price) {
        super(id, name, quantity, price);
    }

    @Override
    public String getCategory() {
        return "Clothing";
    }
}

class Electronics extends Item {
    Electronics(String id, String name, int quantity, long price) {
        super(id, name, quantity, price);
    }

    @Override
    public String getCategory() {
        return "Electronics";
    }
}

class Entertainment extends Item {
    Entertainment(String id, String name, int quantity, long price) {
        super(id, name, quantity, price);
    }

    @Override
    public String getCategory() {
        return "Entertainment";
    }
}