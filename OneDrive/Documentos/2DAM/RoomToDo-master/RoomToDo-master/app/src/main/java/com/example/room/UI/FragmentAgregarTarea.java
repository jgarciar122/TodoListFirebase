package com.example.room.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.room.databinding.FragmentAgregarTareaBinding;
import com.example.room.Model.Tarea;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragmentAgregarTarea extends Fragment {

    private static final int REQUEST_IMAGE_PICK = 1;
    private FragmentAgregarTareaBinding binding;
    private byte[] imagenSeleccionada;
    private TareasViewModel tareasViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAgregarTareaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tareasViewModel = new ViewModelProvider(this).get(TareasViewModel.class);
        binding.btnSeleccionarImagen.setOnClickListener(v -> abrirGaleria());
        binding.btnGuardar.setOnClickListener(v -> guardarTarea());
        binding.editFecha.setOnClickListener(v -> mostrarDatePicker());
    }
    private void mostrarDatePicker() {
        Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);
        int month = calendario.get(Calendar.MONTH);
        int day = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, yearSelected, monthSelected, daySelected) -> {
                    String fechaSeleccionada = daySelected + "/" + (monthSelected + 1) + "/" + yearSelected;
                    binding.editFecha.setText(fechaSeleccionada);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == getActivity().RESULT_OK && data != null) {
            Uri uriImagen = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uriImagen);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                binding.imagenTarea.setImageBitmap(bitmap);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                imagenSeleccionada = outputStream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarTarea() {
        String nombre = binding.editNombre.getText().toString();
        String descripcion = binding.editDescripcion.getText().toString();
        String fechaTexto = binding.editFecha.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date fechaConvertida = null;
        try {
            fechaConvertida = dateFormat.parse(fechaTexto);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Tarea tarea = new Tarea(nombre, descripcion, fechaConvertida, imagenSeleccionada);
        tareasViewModel.insertar(tarea);
        getActivity().onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
