package com.example.room.UI;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.room.R;
import com.example.room.Model.Tarea;
import com.example.room.databinding.ItemTareaBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.TareasViewHolder> {

    private List<Tarea> listaTareas = new ArrayList<>();
    private final TareasViewModel tareasViewModel;

    public TareasAdapter(TareasViewModel tareasViewModel) {
        this.tareasViewModel = tareasViewModel;
    }

    @NonNull
    @Override
    public TareasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTareaBinding binding = ItemTareaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TareasViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TareasViewHolder holder, int position) {
        Tarea tareaActual = listaTareas.get(position);

        holder.binding.textoNombre.setText(tareaActual.getNombre());
        holder.binding.textoDescripcion.setText(tareaActual.getDescripcion());

        if (tareaActual.isCompletada()) {
            holder.binding.textoNombre.setPaintFlags(holder.binding.textoNombre.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.binding.textoNombre.setPaintFlags(holder.binding.textoNombre.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaString = formatoFecha.format(tareaActual.getFecha());
        holder.binding.textoFecha.setText(fechaString);

        holder.binding.textoFecha.setTextColor(getFechaColor(fechaString));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController((Activity) holder.itemView.getContext(), R.id.nav_host_fragment);

                Bundle bundle = new Bundle();
                bundle.putSerializable("tarea", tareaActual);

                navController.navigate(R.id.fragmentDetalleTarea, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    public void establecerTareas(List<Tarea> tareas) {
        this.listaTareas = tareas;

        this.listaTareas.sort((t1, t2) -> t1.getFecha().compareTo(t2.getFecha()));
        notifyDataSetChanged();
    }

    static class TareasViewHolder extends RecyclerView.ViewHolder {

        private final ItemTareaBinding binding;

        public TareasViewHolder(@NonNull ItemTareaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    private int getFechaColor(String fechaString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            java.util.Date fechaTarea = sdf.parse(fechaString);
            java.util.Date fechaActual = new java.util.Date();

            long diferencia = fechaTarea.getTime() - fechaActual.getTime();
            long diasDiferencia = diferencia / (1000 * 60 * 60 * 24);

            if (diasDiferencia < 0) {
                return Color.RED;
            } else if (diasDiferencia <= 7) {
                return Color.YELLOW;
            } else {
                return Color.GREEN;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Color.BLACK;
        }
    }



    public Tarea obtenerTareaEn(int posicion) {
        return listaTareas.get(posicion);
    }

    public void eliminarTareaEn(int posicion) {
        listaTareas.remove(posicion);
        notifyItemRemoved(posicion);
    }
}
