package azka.noreen.filemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BrowseFilesActivity extends AppCompatActivity {
    RecyclerView recycleView;
    TextView text;
    ArrayList<StorageItems> studentArrayList=new ArrayList<>();
    Toolbar toolbar;
    String rootPath;
    RecyclerViewAdapter rva;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_files);
        recycleView=findViewById(R.id.rv);
        text=findViewById(R.id.text);
        text.setVisibility(View.GONE);
        toolbar=findViewById(R.id.toolba);


        setSupportActionBar(toolbar); //set toolbar to act as action bar
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_add_circle_24);
        toolbar.setOverflowIcon(drawable);


        Intent intent=getIntent();
        rootPath=intent.getStringExtra("RootPath");
        File file=new File(rootPath);
        File[] childFiles = file.listFiles();

        if(childFiles==null||childFiles.length==0)
        {
            text.setVisibility(View.VISIBLE);
            text.setText(rootPath);
            return;
        }
        for(int i=0; i<childFiles.length; i++){
            String fName=childFiles[i].getName();
            String fPath=childFiles[i].getPath();
            StorageItems storageItems=new StorageItems(fName,fPath);
            studentArrayList.add(storageItems);
        }
        InitRecycleView();
    }
    public void InitRecycleView(){
        rva=new RecyclerViewAdapter();
        recycleView.setAdapter(rva);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        rva.setData(studentArrayList);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu ,this adds items to action bar but here in toolbar
        getMenuInflater().inflate(R.menu.filemenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int menuItemId=item.getItemId();
        if(menuItemId==R.id.folder){
            //1 for folder
            showDialog(BrowseFilesActivity.this,1 );
        }else if(menuItemId==R.id.file){
            //2 for file
            showDialog(BrowseFilesActivity.this, 2);
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialog(Activity activity,int type){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  //don't show dialog default title
        dialog.setCancelable(false); //don't dismiss the dialog if other area is selected
        dialog.setContentView(R.layout.dialoglayout);

        TextView text = (TextView) dialog.findViewById(R.id.name);


        Button dialogButton = (Button) dialog.findViewById(R.id.ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type==1){
                    try {
                        CreateFolder(rootPath,text.getText().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(type==2){

                    try {
                        CreateFile(rootPath,text.getText().toString().trim());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
//                Toast.makeText(BrowseFilesActivity.this, "added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addItem(text.getText().toString(),stext.getText().toString());

                Toast.makeText(BrowseFilesActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
        dialog.show();

    }
    public void CreateFolder(String FolderPath,String folderName)throws IOException {
//        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folderPath=FolderPath+"/"+folderName;
//        Toast.makeText(this, "|"+folderPath+"|", Toast.LENGTH_SHORT).show();

        File folder=new File(folderPath);
        if(!folder.exists()){
            if(folder.mkdir())
            {
                Toast.makeText(this, "Folder Created Successfully", Toast.LENGTH_SHORT).show();
                StorageItems storageItems=new StorageItems(folderName,folderPath);
                studentArrayList.add(storageItems);
                File files=new File(FolderPath);
                File[] childFiles = files.listFiles();

                if(childFiles.length==1)
                {
                    text.setVisibility(View.GONE);
                    InitRecycleView();
                }
                else{
                    rva.notifyItemInserted(studentArrayList.size());
                }
            }
            else
                Toast.makeText(this, "Sorry!not created", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Folder Already Exists", Toast.LENGTH_SHORT).show();
        }
    }
    public void CreateFile(String folderPath,String fileName) throws IOException {
//        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filePath=folderPath+"/"+fileName+".txt";

        File file=new File(filePath);

        if(!file.exists()){

            if(file.createNewFile())
            {

                Toast.makeText(this, "File Created Successfully", Toast.LENGTH_SHORT).show();
                StorageItems storageItems=new StorageItems(fileName+".txt",filePath);
                studentArrayList.add(storageItems);
                File files=new File(folderPath);
                File[] childFiles = files.listFiles();

                if(childFiles.length==1)
                {
                    text.setVisibility(View.GONE);
                    InitRecycleView();
                }
                else{
                    rva.notifyItemInserted(studentArrayList.size());
                }

            }
            else
                Toast.makeText(this, "Sorry!not created", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "file Already Exists", Toast.LENGTH_SHORT).show();
        }


    }
}