package nigerAplic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import nigerAplic.nigeraplication.R;
import nigerAplic.models.Cliente;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    public interface OnItemDeleteListener {
        void onDeleteClick(Cliente cliente);
    }

    private List<Cliente> clientes;
    private OnItemDeleteListener deleteListener;

    public ClienteAdapter(List<Cliente> clientes, OnItemDeleteListener deleteListener) {
        this.clientes = clientes;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);
        holder.tvNombreApellido.setText(
                cliente.getNombre() + " " + cliente.getApellido());
        holder.tvTelefono.setText("Tel: " + cliente.getTelefono());
        holder.tvDireccion.setText("Dirección: " + cliente.getDireccion());
        holder.tvEmail.setText("Email: " + cliente.getEmail());

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(cliente);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    static class ClienteViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreApellido, tvTelefono, tvDireccion, tvEmail;
        ImageButton btnDelete;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreApellido = itemView.findViewById(R.id.tvNombreApellido);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
