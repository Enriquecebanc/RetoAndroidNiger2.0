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

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private Context context;
    private List<Producto> listaProductos;

    public ProductoAdapter(Context context, List<Producto> listaProductos) {
        this.context = context;
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);

        holder.tvNombre.setText(producto.getNombre());
        holder.tvPrecio.setText("Precio: " + producto.getPrecio() + "€");
        holder.tvMateriales.setText(producto.getMateriales());

        // Cargar imagen: primero intentar como RESOURCE (para los default), si no como
        // URI
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
            // Es un recurso (ej: "grande", "mediano")
            holder.imgProducto.setImageResource(resId);
        } else {
            // No es recurso, intentar como URI (ej: "content://...")
            try {
                android.net.Uri uri = android.net.Uri.parse(producto.getImagen());
                holder.imgProducto.setImageURI(uri);
            } catch (Exception e) {
                // Fallback si falla todo
                holder.imgProducto.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }

        holder.btnAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().add(producto);
            Toast.makeText(context, "Añadido al carrito: " + producto.getNombre(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    // VIEW HOLDER
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvPrecio, tvMateriales;
        ImageView imgProducto;
        Button btnAddToCart;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvMateriales = itemView.findViewById(R.id.tvMateriales);
            tvMateriales = itemView.findViewById(R.id.tvMateriales);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}