package com.example.room.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.room.MainActivity;
import com.example.room.databinding.ActivityRegistroBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * RegisterActivity maneja el registro e inicio de sesión con Firebase Authentication.
 */
public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializamos View Binding
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // Click en "Iniciar Sesión"
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logarUsuario();
            }
        });

        // Click en "Registrarse"
        binding.btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearUsuario();
            }
        });
    }

    /**
     * Crea un nuevo usuario en Firebase (Email/Password).
     */
    private void crearUsuario() {
        String email = binding.textoMail.getText().toString().trim();
        String password = binding.textoPassword.getText().toString().trim();
        String password2 = binding.textoPassword2.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        } else if (!password.equals(password2)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Usuario creado por primera vez
                            FirebaseUser usuario = mAuth.getCurrentUser();
                            if (usuario != null) {
                                String userEmail = usuario.getEmail();
                                // Correo guardado para luego añadirlo a la card
                                Toast.makeText(RegistroActivity.this,
                                        "Bienvenido, nuevo usuario: " + userEmail,
                                        Toast.LENGTH_SHORT).show();
                            }

                            // Pasamos a la actividad principal
                            iniciarMainActivity();
                        } else {
                            // Falla en la creación de usuario (e.g., email ya en uso)
                            Toast.makeText(RegistroActivity.this,
                                    "Error al crear usuario: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Inicia sesión con un usuario existente.
     */
    private void logarUsuario() {
        String email = binding.textoMail.getText().toString().trim();
        String password = binding.textoPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Usuario existente
                            FirebaseUser usuario = mAuth.getCurrentUser();
                            if (usuario != null) {
                                String userEmail = usuario.getEmail();
                                // Correo guardado para hacer referencia luego en la card
                                Toast.makeText(RegistroActivity.this,
                                        "Bienvenido de nuevo: " + userEmail,
                                        Toast.LENGTH_SHORT).show();
                            }

                            // Pasamos a la actividad principal
                            iniciarMainActivity();
                        } else {
                            // Error al iniciar sesión
                            Toast.makeText(RegistroActivity.this,
                                    "Error al iniciar sesión: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Navega a la MainActivity
     */
    private void iniciarMainActivity() {
        Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
