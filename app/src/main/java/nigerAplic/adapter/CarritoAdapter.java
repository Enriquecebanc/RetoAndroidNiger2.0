package nigerAplic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nigerAplic.models.CartItem;
import nigerAplic.models.Producto;
import nigerAplic.nigeraplication.R;
import nigerAplic.utils.CartManager;

// Adaptador para mostrar los productos del carrito en un RecyclerView
public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private Context context;
    private List<CartItem> listaCarrito;
    private OnItemDeletedListener deletedListener;

    // Interfaz para notificar cuando se elimina o modifica un item
    public interface OnItemDeletedListener {
        void onItemDeleted();
    }

    public CarritoAdapter(Context context, List<CartItem> listaCarrito, OnItemDeletedListener deletedListener) {
        this.context = context;
        this.listaCarrito = listaCarrito;
        this.deletedListener = deletedListener;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    // Configura cada item del carrito con sus datos y eventos
    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        CartItem item = listaCarrito.get(position);
        Producto producto = item.getProducto();

        holder.tvNombre.setText(producto.getNombre());
        // Mostrar precio unitario
        holder.tvPrecio.setText(producto.getPrecio() + "€");
        // Mostrar cantidad inicial
        holder.tvCantidad.setText(String.valueOf(item.getQuantity()));
        // Mostrar total del item
        holder.tvTotalItem.setText(String.format("%.2f €", item.getTotalPrice()));

        // Cargar imagen del producto
        int resId = 0;
        try {
            resId = context.getResources().getIdentifier(
                    producto.getImagen(),
                    "drawable",
                    context.getPackageName());
        } catch (Exception e) {
            resId = 0;
        }

        if (resId != 0) {
            holder.imgProducto.setImageResource(resId);
        } else {
            try {
                android.net.Uri uri = android.net.Uri.parse(producto.getImagen());
                holder.imgProducto.setImageURI(uri);
            } catch (Exception e) {
                holder.imgProducto.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }

        // Botón para aumentar la cantidad del producto
        holder.btnSumar.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                // Actualizar cantidad en el gestor del carrito
                CartManager.getInstance().increaseQuantity(currentPos);
                // Actualizar la vista de este item
                notifyItemChanged(currentPos);
                // Notificar para actualizar el total general
                if (deletedListener != null)
                    deletedListener.onItemDeleted();
            }
        });

        // Botón para disminuir la cantidad del producto
        holder.btnRestar.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                // Actualizar cantidad en el gestor del carrito
                CartManager.getInstance().decreaseQuantity(currentPos);
                // Actualizar la vista de este item
                notifyItemChanged(currentPos);
                // Notificar para actualizar el total general
                if (deletedListener != null)
                    deletedListener.onItemDeleted();
            }
        });

        // Botón para eliminar el producto del carrito
        holder.btnEliminar.setOnClickListener(v -> {
            int actualPosition = holder.getAdapterPosition();
            if (actualPosition != RecyclerView.NO_POSITION) {
                // Eliminar del gestor del carrito
                CartManager.getInstance().remove(actualPosition);
                // Eliminar de la lista local
                listaCarrito.remove(actualPosition);
                // Notificar al RecyclerView que se eliminó un item
                notifyItemRemoved(actualPosition);
                notifyItemRangeChanged(actualPosition, listaCarrito.size());

                // Notificar para actualizar el total general
                if (deletedListener != null) {
                    deletedListener.onItemDeleted();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCarrito.size();
    }

    // ViewHolder que contiene las vistas de cada item del carrito
    public static class CarritoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvPrecio, tvCantidad, tvTotalItem;
        ImageView imgProducto;
        android.widget.ImageButton btnEliminar;
        android.widget.Button btnSumar, btnRestar;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Vincular las vistas del layout con las variables
            tvNombre = itemView.findViewById(R.id.tvNombreCarrito);
            tvPrecio = itemView.findViewById(R.id.tvPrecioCarrito);
            imgProducto = itemView.findViewById(R.id.imgProductoCarrito);
            btnEliminar = itemView.findViewById(R.id.btnEliminarCarrito);

            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvTotalItem = itemView.findViewById(R.id.tvTotalItemCarrito);
            btnSumar = itemView.findViewById(R.id.btnSumar);
            btnRestar = itemView.findViewById(R.id.btnRestar);
        }
    }
}
