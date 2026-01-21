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

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private Context context;
    private List<Producto> listaCarrito;

    public CarritoAdapter(Context context, List<Producto> listaCarrito) {
        this.context = context;
        this.listaCarrito = listaCarrito;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        Producto producto = listaCarrito.get(position);

        holder.tvNombre.setText(producto.getNombre());
        holder.tvPrecio.setText(producto.getPrecio() + "€");

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
            holder.imgProducto.setImageResource(resId);
        } else {
            try {
                android.net.Uri uri = android.net.Uri.parse(producto.getImagen());
                holder.imgProducto.setImageURI(uri);
            } catch (Exception e) {
                holder.imgProducto.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaCarrito.size();
    }

    public static class CarritoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvPrecio;
        ImageView imgProducto;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreCarrito);
            tvPrecio = itemView.findViewById(R.id.tvPrecioCarrito);
            imgProducto = itemView.findViewById(R.id.imgProductoCarrito);
        }
    }
}
