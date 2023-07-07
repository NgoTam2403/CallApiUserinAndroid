package com.example.apirecyclerview;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.apirecyclerview.adapter.UserAdapter;
import com.example.apirecyclerview.api.ApiService;
import com.example.apirecyclerview.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MainActivity extends AppCompatActivity {
    private static RecyclerView rcUser;
    private static List<User> list;
    private Context context;
    private FloatingActionButton icadduser;
    private UserAdapter userAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcUser = findViewById(R.id.rcUser);
        context = this;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcUser.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcUser.addItemDecoration(itemDecoration);

        list = new ArrayList<>();
        userAdapter = new UserAdapter(list, context);
        rcUser.setAdapter(userAdapter);

        icadduser = findViewById(R.id.icadduser);
        icadduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogToAddData(0,null);
            }
        });

        callApi();
    }

    public void showDialogToAddData(int type,User userUpdate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Thêm dữ liệu");
        View view = getLayoutInflater().inflate(R.layout.dialog_adduser, null);
        builder.setView(view);

        EditText editTextName = view.findViewById(R.id.editTextName);
        EditText editTextJob = view.findViewById(R.id.editTextjob);
        EditText editTextAddress = view.findViewById(R.id.editTextAddress);

        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString();
                String job = editTextJob.getText().toString();
                String address = editTextAddress.getText().toString();

                User user = new User(name, job, address);

                ApiService.apiservice.createUser(user).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()&&(name.isEmpty()==false || job.isEmpty()==false || address.isEmpty()==false)) {
                            User newUser = response.body();
                            list.add(newUser);
                            userAdapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Thêm dữ liệu thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Thêm dữ liệu thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        if(type == 1){
            editTextName.setText(userUpdate.getName());
            editTextJob.setText(userUpdate.getJob());
            editTextAddress.setText(userUpdate.getAddress());
            builder.setPositiveButton("Cap nhat", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = editTextName.getText().toString();
                    String job = editTextJob.getText().toString();
                    String address = editTextAddress.getText().toString();

                    User user = new User(name, job, address);

                    ApiService.apiservice.updateUser(userUpdate.getId(), user).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User updatedUser = response.body();
                            int position = list.indexOf(userUpdate); // Lấy vị trí của người dùng trong danh sách
                            if (position != -1) {
                                list.set(position, updatedUser); // Cập nhật thông tin người dùng trong danh sách
                                userAdapter.notifyItemChanged(position); // Cập nhật chỉ mục của người dùng đã thay đổi
                                Toast.makeText(MainActivity.this, "Cập nhật dữ liệu thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Cập nhật dữ liệu thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });


        }

            AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteItem(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa");
        builder.setMessage("Bạn có muốn xóa không?");
        builder.setCancelable(false);
        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiService.apiservice.deleteUser(Integer.parseInt(id)).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    list.clear();
                                    callApi();
                                    userAdapter.notifyDataSetChanged();
                                    Toast.makeText(context, "Xóa thanh cong", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }

    private void callApi() {
        ApiService.apiservice.getListuser().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    list.clear();
                    list.addAll(response.body());
                    userAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
