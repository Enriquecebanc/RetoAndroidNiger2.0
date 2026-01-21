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

import nigerAplic.models.Producto;
import nigerAplic.nigeraplication.R;
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

        // Cargar imagen desde drawable por nombre
        int resId = context.getResources().getIdentifier(
                producto.getImagen(),
                "drawable",
                context.getPackageName()
        );

        if (resId != 0) {
            holder.imgProducto.setImageResource(resId);
        } else {
            holder.imgProducto.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    // VIEW HOLDER
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvPrecio, tvMateriales;
        ImageView imgProducto;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvMateriales = itemView.findViewById(R.id.tvMateriales);
            imgProducto = itemView.findViewById(R.id.imgProducto);
        }
    }
}