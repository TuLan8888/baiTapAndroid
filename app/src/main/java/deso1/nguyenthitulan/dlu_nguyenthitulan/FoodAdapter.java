package deso1.nguyenthitulan.dlu_nguyenthitulan;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.bumptech.glide.Glide;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private Context context;
    private List<Food> foodList;
    private OnItemClickListener listener;

    public FoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        Glide.with(context).load(foodList.get(position).getImage()).into(holder.ivFoodImage);
        holder.tvFoodName.setText(food.getName());
        holder.tvFoodPrice.setText("Giá: " + food.getPrice());
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition(); // Lấy vị trí mới nhất
                if (adapterPosition != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEditClick(adapterPosition);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoodImage, ivEdit;
        TextView tvFoodName, tvFoodPrice;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoodImage = itemView.findViewById(R.id.ivFoodImagee);
            tvFoodName = itemView.findViewById(R.id.tvFoodNamee);
            tvFoodPrice = itemView.findViewById(R.id.tvFoodPricee);
            ivEdit = itemView.findViewById(R.id.ivEdit);
        }
    }

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}