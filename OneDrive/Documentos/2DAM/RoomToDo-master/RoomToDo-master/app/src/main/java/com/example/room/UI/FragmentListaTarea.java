package com.example.room.UI;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.room.Model.Tarea;
import com.example.room.R;
import com.example.room.databinding.FragmentListaTareaBinding;

public class FragmentListaTarea extends Fragment {

    private TareasViewModel tareasViewModel;
    private TareasAdapter tareasAdapter;
    private FragmentListaTareaBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListaTareaBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_tareas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_toolbar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                Log.i("TAG", "onMenuItemSelected: ");
                boolean filtroActivo = !menuItem.isChecked();
                if (filtroActivo) {
                    menuItem.setChecked(true);
                    tareasViewModel.obtenerTareasNoCompletadas().observe(getViewLifecycleOwner(), tareas -> {
                        tareasAdapter.establecerTareas(tareas);
                    });
                } else {
                    menuItem.setChecked(false);
                    tareasViewModel.obtenerTodasLasTareas().observe(getViewLifecycleOwner(), tareas -> {
                        tareasAdapter.establecerTareas(tareas);
                    });
                }
                return false;
            }
        });
        tareasViewModel = new ViewModelProvider(this).get(TareasViewModel.class);

        tareasAdapter = new TareasAdapter(tareasViewModel);
        recyclerView.setAdapter(tareasAdapter);

        binding.botomAgregarTarea.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.fragmentAgregarTarea);
        });

        tareasViewModel.obtenerTodasLasTareas().observe(getViewLifecycleOwner(), tareas -> {
            tareasAdapter.establecerTareas(tareas);
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int posicion = viewHolder.getAdapterPosition();
                Tarea tarea = tareasAdapter.obtenerTareaEn(posicion);

                if (direction == ItemTouchHelper.RIGHT) {
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Marcar tarea como completada")
                            .setMessage("¿Estás seguro de que quieres marcar esta tarea como completada?")
                            .setPositiveButton("Sí", (dialog, which) -> {
                                tarea.setCompletada(true);
                                tareasViewModel.actualizar(tarea);
                                tareasAdapter.notifyItemChanged(posicion);
                            })
                            .setNegativeButton("Cancelar", (dialog, which) -> {
                                tareasAdapter.notifyItemChanged(posicion); // Restaurar tarea si se cancela
                            })
                            .show();
                } else if (direction == ItemTouchHelper.LEFT) {
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Eliminar tarea")
                            .setMessage("¿Estás seguro de que quieres eliminar esta tarea?")
                            .setPositiveButton("Eliminar", (dialog, which) -> {
                                tareasViewModel.eliminar(tarea);
                                tareasAdapter.eliminarTareaEn(posicion);
                            })
                            .setNegativeButton("Cancelar", (dialog, which) -> {
                                tareasAdapter.notifyItemChanged(posicion);
                            })
                            .show();
                }
            }

        }).attachToRecyclerView(binding.recyclerTareas);
    }
}