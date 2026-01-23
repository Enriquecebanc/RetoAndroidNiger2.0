package nigerAplic.models;

import java.util.Objects;

public class CartItem {
    private Producto producto;
    private int quantity;

    public CartItem(Producto producto, int quantity) {
        this.producto = producto;
        this.quantity = quantity;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return producto.getPrecio() * quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(producto, cartItem.producto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producto);
    }
}
