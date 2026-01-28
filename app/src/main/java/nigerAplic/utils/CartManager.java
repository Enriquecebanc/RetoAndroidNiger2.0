package nigerAplic.utils;

import java.util.ArrayList;
import java.util.List;

import nigerAplic.models.CartItem;
import nigerAplic.models.Producto;

// Gestor del carrito de compras
// Usa el patrón Singleton para mantener una única instancia del carrito en toda la aplicación
public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    // Obtiene la instancia única del gestor del carrito
    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Añade un producto al carrito
    // Si el producto ya existe, aumenta su cantidad en 1
    // Si no existe, lo añade con cantidad 1
    public void add(Producto producto) {
        for (CartItem item : cartItems) {
            if (item.getProducto().getId() == producto.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(producto, 1));
    }

    // Elimina un item del carrito
    public void remove(CartItem item) {
        cartItems.remove(item);
    }

    // Elimina un item del carrito por su posición
    public void remove(int index) {
        if (index >= 0 && index < cartItems.size()) {
            cartItems.remove(index);
        }
    }

    // Aumenta la cantidad de un producto en 1
    public void increaseQuantity(int index) {
        if (index >= 0 && index < cartItems.size()) {
            CartItem item = cartItems.get(index);
            item.setQuantity(item.getQuantity() + 1);
        }
    }

    // Disminuye la cantidad de un producto en 1
    // No permite que la cantidad sea menor que 0
    public void decreaseQuantity(int index) {
        if (index >= 0 && index < cartItems.size()) {
            CartItem item = cartItems.get(index);
            if (item.getQuantity() > 0) {
                item.setQuantity(item.getQuantity() - 1);
            }
        }
    }

    // Devuelve una copia de todos los items del carrito
    public List<CartItem> getAll() {
        return new ArrayList<>(cartItems);
    }

    // Vacía completamente el carrito
    public void clear() {
        cartItems.clear();
    }

    // Calcula el precio total de todos los productos en el carrito
    public double calculateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }
}
