package deso1.nguyenthitulan.dlu_nguyenthitulan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddEditFood extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    private ImageView ivFoodImage, ivGallery, ivCamera;
    private EditText etFoodName, etFoodPrice;
    private Button btnSaveFood;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri imageUri;
    private String mode; // "ADD" hoặc "EDIT"
    private String foodId; // ID của món ăn (nếu là chế độ EDIT)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // Khởi tạo Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("foods");
        storageReference = FirebaseStorage.getInstance().getReference("food_images");

        // Ánh xạ view
        ivFoodImage = findViewById(R.id.ivFoodImage);
        ivGallery = findViewById(R.id.ivGallery);
        ivCamera = findViewById(R.id.ivCamera);
        etFoodName = findViewById(R.id.etFoodName);
        etFoodPrice = findViewById(R.id.etFoodPrice);
        btnSaveFood = findViewById(R.id.btnSaveFood);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        mode = intent.getStringExtra("MODE"); // "ADD" hoặc "EDIT"

        if (mode != null && mode.equals("EDIT")) {
            // Nếu là chế độ sửa, hiển thị dữ liệu của món ăn
            foodId = intent.getStringExtra("FOOD_ID");
            String foodName = intent.getStringExtra("FOOD_NAME");
            String foodPrice = intent.getStringExtra("FOOD_PRICE");

            etFoodName.setText(foodName);
            etFoodPrice.setText(foodPrice);
            btnSaveFood.setText("Update"); // Đổi text của button thành "Update"
        } else {
            btnSaveFood.setText("Add"); // Đổi text của button thành "Add"
        }

        // Xử lý sự kiện khi nhấn icon thư viện
        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Xử lý sự kiện khi nhấn icon máy ảnh
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        // Xử lý sự kiện khi nhấn nút Add
        btnSaveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFood();
            }
        });
    }

    // Mở thư viện ảnh
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Mở máy ảnh
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    // Xử lý kết quả từ thư viện ảnh hoặc máy ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                // Lấy ảnh từ thư viện
                imageUri = data.getData();
                ivFoodImage.setImageURI(imageUri);
            } else if (requestCode == CAMERA_REQUEST && data != null) {
                // Lấy ảnh từ máy ảnh
                imageUri = data.getData();
                ivFoodImage.setImageURI(imageUri);
            }
        }
    }

    // Thêm món ăn vào Firebase
    private void saveFood() {
        String foodName = etFoodName.getText().toString().trim();
        String foodPrice = etFoodPrice.getText().toString().trim();

        if (foodName.isEmpty() || foodPrice.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mode.equals("ADD")) {
            String id = databaseReference.push().getKey();
            uploadImageAndSaveFood(id, foodName, foodPrice);
        } else if (mode.equals("EDIT")) {
            uploadImageAndSaveFood(foodId, foodName, foodPrice);
        }
    }

    private void uploadImageAndSaveFood(String id, String foodName, String foodPrice) {
        StorageReference fileReference = storageReference.child(id + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    Food food = new Food(id,imageUrl, foodName, foodPrice);
                    databaseReference.child(id).setValue(food)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getApplicationContext(), mode.equals("ADD") ? "Thêm món thành công" : "Cập nhật món thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                }));
    }
}