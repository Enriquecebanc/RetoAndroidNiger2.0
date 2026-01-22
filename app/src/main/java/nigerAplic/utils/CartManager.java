package nigerAplic.utils;

import java.util.ArrayList;
import java.util.List;

import nigerAplic.models.Producto;

public class CartManager {
    private static CartManager instance;
    private List<Producto> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void add(Producto producto) {
        cartItems.add(producto);
    }

    public void remove(Producto producto) {
        cartItems.remove(producto);
    }

    public void remove(int index) {
        if (index >= 0 && index < cartItems.size()) {
            cartItems.remove(index);
        }
    }

    public List<Producto> getAll() {
        return new ArrayList<>(cartItems);
    }

    public void clear() {
        cartItems.clear();
    }

    public double calculateTotal() {
        double total = 0;
        for (Producto p : cartItems) {
            total += p.getPrecio();
        }
        return total;
    }
}
