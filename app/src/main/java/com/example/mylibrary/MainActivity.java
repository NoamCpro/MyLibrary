package com.example.mylibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    EditText etBook, etAuthor, etPages;
    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        etBook = findViewById(R.id.etBook);
        etAuthor = findViewById(R.id.etAuthor);
        etPages = findViewById(R.id.etPages);
        Button btnSave = findViewById(R.id.btnSave);
        tvStatus = findViewById(R.id.tvStatus);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean good = true;
                String pagesText = etPages.getText().toString();
                String bookText = etBook.getText().toString();
                String authorText = etAuthor.getText().toString();
//                Integer pages = Integer.parseInt(pagesText);

                if(bookText.isEmpty()) {
                    etBook.setError("חובה למלא שם הספר");
                    good = false;
                    etBook.requestFocus();
                }

                if(authorText.isEmpty()) {
                    etAuthor.setError("חובה למלא שם הספר");
                    etAuthor.requestFocus();
                    good = false;
                }

                if (pagesText.isEmpty()) {
                    etPages.setError("חובה למלא מספר עמודים");
                    etPages.requestFocus();
                    good = false;
                }
                if(!good)
                    return;

                Map<String, Object> book = new HashMap<>();
                book.put("title",bookText);
                book.put("author", authorText);
                book.put("pages", pagesText);

                tvStatus.setText("שומר...");

                db.collection("books").document("current").set(book)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                tvStatus.setText("נשמר בענן");
                                Toast.makeText(MainActivity.this, "הספר נשמר", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                tvStatus.setText("השמירה נכשלה: " + e.getMessage());
                            }
                        });
            }
        });

    }
}