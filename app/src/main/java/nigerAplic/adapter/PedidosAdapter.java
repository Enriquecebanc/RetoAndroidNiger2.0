package nigerAplic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nigerAplic.models.Pedidin;
import nigerAplic.nigeraplication.R;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> {

    private List<Pedidin> pedidos;
    private OnPedidoClickListener listener;

    public interface OnPedidoClickListener {
        void onPedidoClick(Pedidin pedido);
    }

    public PedidosAdapter(List<Pedidin> pedidos, OnPedidoClickListener listener) {
        this.pedidos = pedidos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedidin pedido = pedidos.get(position);
        holder.tvFechaPedido.setText(pedido.getFecha());
        holder.tvTotalPedido.setText(String.format("%.2f €", pedido.getTotal()));
        holder.tvClientePedido.setText("Cliente: " + pedido.getClienteNombre());
        holder.tvProductosResumen.setText(pedido.getProductosResumen());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPedidoClick(pedido);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvFechaPedido, tvTotalPedido, tvClientePedido, tvProductosResumen;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFechaPedido = itemView.findViewById(R.id.tvFechaPedido);
            tvTotalPedido = itemView.findViewById(R.id.tvTotalPedido);
            tvClientePedido = itemView.findViewById(R.id.tvClientePedido);
            tvProductosResumen = itemView.findViewById(R.id.tvProductosResumen);
        }
    }
}
