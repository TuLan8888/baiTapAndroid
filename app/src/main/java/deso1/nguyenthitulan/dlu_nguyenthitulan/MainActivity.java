package deso1.nguyenthitulan.dlu_nguyenthitulan;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    private RecyclerView rvFoodList;
    private FloatingActionButton fabAddFood;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("foods");

        // Ánh xạ view
        rvFoodList = findViewById(R.id.rvFoodList);
        fabAddFood = findViewById(R.id.fabAddFood);

        // Khởi tạo danh sách món ăn
        foodList = new ArrayList<>();
        foodAdapter = new FoodAdapter(this, foodList);

        // Cấu hình RecyclerView
        rvFoodList.setLayoutManager(new LinearLayoutManager(this));
        rvFoodList.setAdapter(foodAdapter);

        // Lấy dữ liệu từ Firebase
        loadFoodsFromFirebase();

        // Xử lý sự kiện khi nhấn nút Add
        fabAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến trang thêm món ăn
                Intent intent = new Intent(MainActivity.this, AddEditFood.class);
                intent.putExtra("MODE", "ADD"); // Truyền chế độ ADD
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi nhấn nút Edit trong mỗi item
        foodAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                // Lấy món ăn được chọn
                Food selectedFood = foodList.get(position);

                // Chuyển đến trang sửa món ăn
                Intent intent = new Intent(MainActivity.this, AddEditFood.class);
                intent.putExtra("MODE", "EDIT"); // Truyền chế độ EDIT
                intent.putExtra("FOOD_ID", selectedFood.getKey()); // Truyền ID của món ăn
                intent.putExtra("FOOD_NAME", selectedFood.getName()); // Truyền tên món ăn
                intent.putExtra("FOOD_PRICE", selectedFood.getPrice()); // Truyền giá món ăn
                startActivity(intent);
            }
        });
    }

    // Lấy dữ liệu từ Firebase
    private void loadFoodsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    foodList.add(food);
                }
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });
    }
}