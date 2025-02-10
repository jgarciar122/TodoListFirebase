package com.example.room.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.room.Model.Tarea;
import com.example.room.R;
import com.example.room.databinding.FragmentBusquedaBinding;

public class FragmentBusqueda extends Fragment {

    private TareasViewModel tareasViewModel;
    private TareasAdapter tareasAdapter;
    private FragmentBusquedaBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBusquedaBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tareasViewModel = new ViewModelProvider(this).get(TareasViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_tareas_busqueda);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tareasAdapter = new TareasAdapter(tareasViewModel);
        recyclerView.setAdapter(tareasAdapter);

        tareasViewModel.obtenerTodasLasTareas().observe(getViewLifecycleOwner(), tareas -> {
            tareasAdapter.establecerTareas(tareas);
        });

        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tareasViewModel.obtenerTareasFiltradasPorNombre(newText).observe(getViewLifecycleOwner(), tareas -> {
                    tareasAdapter.establecerTareas(tareas);
                });
                return true;
            }
        });
    }
}
