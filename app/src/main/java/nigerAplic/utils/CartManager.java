package nigerAplic.utils;

import java.util.ArrayList;
import java.util.List;

import nigerAplic.models.CartItem;
import nigerAplic.models.Producto;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

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
        for (CartItem item : cartItems) {
            if (item.getProducto().getId() == producto.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(producto, 1));
    }

    public void remove(CartItem item) {
        cartItems.remove(item);
    }

    public void remove(int index) {
        if (index >= 0 && index < cartItems.size()) {
            cartItems.remove(index);
        }
    }

    public void increaseQuantity(int index) {
        if (index >= 0 && index < cartItems.size()) {
            CartItem item = cartItems.get(index);
            item.setQuantity(item.getQuantity() + 1);
        }
    }

    public void decreaseQuantity(int index) {
        if (index >= 0 && index < cartItems.size()) {
            CartItem item = cartItems.get(index);
            if (item.getQuantity() > 0) {
                item.setQuantity(item.getQuantity() - 1);
            }
        }
    }

    public List<CartItem> getAll() {
        return new ArrayList<>(cartItems);
    }

    public void clear() {
        cartItems.clear();
    }

    public double calculateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }
}
