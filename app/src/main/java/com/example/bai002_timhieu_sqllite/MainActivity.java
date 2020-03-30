package com.example.bai002_timhieu_sqllite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private EditText Ed1;
    private  EditText Ed2;
    private EditText Ed3;
    private Button tbn1;
    private Button tbn2 , tbn3;
    private ListView LvUser;
    private ArrayAdapter<User> adapter;
    private ArrayList<User> Userlist = new ArrayList<>();
    int idUpdate = -1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        Ed1 = findViewById(R.id.Ed1);
        Ed2 = findViewById(R.id.Ed2);
        Ed3 = findViewById(R.id.Ed3);
        tbn2 = findViewById(R.id.tbn2);
        tbn1 = findViewById(R.id.tbn1);
        tbn3 = findViewById(R.id.tbn3);



        // tbn event
        tbn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(idUpdate < 0)
               {
                   InsertTest();

               }
               else
               {
                   updaterow();
                    idUpdate = -1;
               }
                LoadData();
            }
        });
        tbn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                   builder.setMessage("Bạn muốn xóa toàn bộ không").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           DeleteDataUser2();
                           LoadData();
                       }
                   })
                           .setNegativeButton("No", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.cancel();
                               }
                           });
                   AlertDialog alẻt = builder.create();
                   alẻt.setTitle("Bạn lựa chọn gì !");
                   alẻt.show();
            }
        });
        tbn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Bạn có muốn xuất ra không").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showTotal();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.setTitle("xuat du lieu");
                    alert.show();

                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "error" + e , Toast.LENGTH_SHORT).show();
                }
            }
        });


        // adapter
        LvUser = findViewById(R.id.LvUser);
        adapter = new ArrayAdapter<User>(this , 0 , Userlist){
            @NonNull
            @Override
            public View getView(int position,  View convertView,  ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView =  inflater.inflate(R.layout.data_item , null);
                TextView txt1 = convertView.findViewById(R.id.txt1);
                TextView txt2 = convertView.findViewById(R.id.txt2);
                TextView txt3 = convertView.findViewById(R.id.txt3);
                User u = Userlist.get(position);
                txt1.setText(u.getName());
                txt2.setText(u.getTuoi());
                txt3.setText(u.getDiem());
                return convertView;
            }
        };
        LvUser.setAdapter(adapter);
        LvUser.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DeleteDataUser(position);
                LoadData();
                return false;
            }
        });
        LvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showInfotenedit(position);

            }
        });

        LoadData();

    }



    private  void  showInfotenedit(int position)
    {
      try
      {
          User u = Userlist.get(position);
          String goc = "dtc010";
          String sosanh = u.getTuoi();
          if(sosanh.equals(goc))
          {
              Ed1.setText(u.getName());
              Ed2.setText(u.getTuoi());
              Ed3.setText(u.getDiem());
              idUpdate = u.getId();
          }
          else
          {
              Toast.makeText(this, "chung toi chi sua dtc010", Toast.LENGTH_SHORT).show();
          }

      }
      catch (Exception e)
      {
          Log.e("loi",e.toString());
      }
    }
    private void updaterow()
    {
        try
        {
            String name = Ed1.getText().toString();
            String tuoi = Ed2.getText().toString();
            String diem = Ed3.getText().toString();
            String sql = "UPDATE tbchuno SET name =  '"+name+"' , tuoi = '"+tuoi+"' , diem = '"+diem+"' WHERE id = " + idUpdate;
            db.execSQL(sql);
            Toast.makeText(this, "chung toi da sua doi thanh cong", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Log.e("eroor",e.toString());
        }
    }

    private void  initData()
    {
       try
       {
           db = openOrCreateDatabase("KiemTra2.db",MODE_PRIVATE,null);
           String sql3 = "CREATE TABLE IF NOT EXISTS tbchuno (id integer primary key autoincrement, name text ,  tuoi text , diem text )";
           db.execSQL(sql3);
       }
       catch (Exception e)
       {
           Log.e("error",e.toString());
       }
    }

    private void InsertTest()
    {
        String name = Ed1.getText().toString();
        String tuoi = Ed2.getText().toString();
        String diem = Ed3.getText().toString();
        String sql = " INSERT INTO tbchuno (name,tuoi,diem) VALUES ('"+name+"' , '"+tuoi+"','"+diem+"') ";
        db.execSQL(sql);
    }

    private  void DeleteDataUser(int position)
    {
      try
      {
          int id = Userlist.get(position).getId();
          String tuois = Userlist.get(position).getTuoi();
          String tuois2 = "dtc01";
          if(tuois.equals(tuois2))
          {
              String sql = "DELETE FROM tbchuno WHERE tuoi = 'dtc01' AND id = " + id;
              db.execSQL(sql);
              Toast.makeText(this, "bạn đã xóa thành công", Toast.LENGTH_SHORT).show();
          }
          else
          {
              Toast.makeText(this, "chung toi chi xoa dtc01", Toast.LENGTH_SHORT).show();
          }


      }
      catch (Exception e)
      {
          Toast.makeText(this, "error" + e, Toast.LENGTH_SHORT).show();
      }
    }

    private  void DeleteDataUser2()
    {
        String sql2 = "DELETE FROM tbchuno";
        db.execSQL(sql2);
    }

    private void LoadData()
    {
        Userlist.clear();
        String sql2 = "SELECT * FROM tbchuno";
        Cursor cursor = db.rawQuery(sql2 , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String tuoi = cursor.getString(2);
            String diem = cursor.getString(3);
            User u = new User();
            u.setId(id);
            u.setName(name);
            u.setTuoi(tuoi);
            u.setDiem(diem);
            Userlist.add(u);
            cursor.moveToNext();
        }
        adapter.notifyDataSetChanged();
    }
    private void showTotal()
    {
        Userlist.clear();
        String sql2 = "SELECT * FROM tbchuno WHERE diem < 4";
        Cursor cursor = db.rawQuery(sql2 , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String tuoi = cursor.getString(2);
            String diem = cursor.getString(3);
            User u = new User();
            u.setId(id);
            u.setName(name);
            u.setTuoi(tuoi);
            u.setDiem(diem);
            Userlist.add(u);
            cursor.moveToNext();
        }
        adapter.notifyDataSetChanged();
    }

}
