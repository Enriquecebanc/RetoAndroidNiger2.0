package nigerAplic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nigerAplic.models.Producto;
import nigerAplic.nigeraplication.R;
import nigerAplic.utils.CartManager;
import nigerAplic.database.AppDatabase;

// Esta clase Adapter se encarga de coger la lista de productos y mostrar cada uno
// en la lista visual (RecyclerView). Es el "puente" entre los datos y la pantalla.
public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private Context context; // Para saber desde qué parte de la app estamos trabajando
    private List<Producto> listaProductos; // La lista de datos

    public ProductoAdapter(Context context, List<Producto> listaProductos) {
        this.context = context;
        this.listaProductos = listaProductos;
    }

    // Crea el "cascarón" o diseño visual para UN elemento de la lista (layout
    // item_producto)
    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    // Rellena el cascarón con los datos de un producto específico (el de la
    // posición 'position')
    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position); // Cogemos el producto de la lista

        // Ponemos los textos
        holder.tvNombre.setText(producto.getNombre());
        holder.tvStock.setText("Inventario: " + producto.getStock());
        holder.tvPrecio.setText("Precio: " + producto.getPrecio() + "€");
        holder.tvMateriales.setText(producto.getMateriales());

        // Cargar imagen
        // Primero intentamos ver si la imagen es un recurso interno de la app 
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
            // Es una imagen interna, la cargamos directamente
            holder.imgProducto.setImageResource(resId);
        } else {
            // Si no es interna, asumimos que es una foto de la galería (URI)
            try {
                android.net.Uri uri = android.net.Uri.parse(producto.getImagen());
                holder.imgProducto.setImageURI(uri);
            } catch (Exception e) {
                // Si falla todo, ponemos una imagen de error o predeterminada
                holder.imgProducto.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }

        // Configurar botón "Añadir al carrito"
        holder.btnAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().add(producto);
            Toast.makeText(context, "Añadido al carrito: " + producto.getNombre(), Toast.LENGTH_SHORT).show();
        });

        // Configurar botón "Eliminar"
        holder.btnBorrar.setOnClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(context);
            db.productoDao().delete(producto); // Borrar de la base de datos

            // Actualizar la lista visual para que desaparezca
            int actualPosition = holder.getAdapterPosition();
            if (actualPosition != RecyclerView.NO_POSITION) {
                listaProductos.remove(actualPosition); // Borrar de la lista en memoria
                notifyItemRemoved(actualPosition); // Avisar al adaptador para que anime el borrado
                notifyItemRangeChanged(actualPosition, listaProductos.size());
                Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    // Clase interna que guarda las referencias a los elementos de UNA fila
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvPrecio, tvMateriales, tvStock;
        ImageView imgProducto;
        Button btnAddToCart, btnBorrar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);

            // Buscamos los elementos visuales dentro del diseño de la fila
            // (item_producto.xml)
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvStock = itemView.findViewById(R.id.tvStock);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvMateriales = itemView.findViewById(R.id.tvMateriales);

            imgProducto = itemView.findViewById(R.id.imgProducto);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
        }
    }
}