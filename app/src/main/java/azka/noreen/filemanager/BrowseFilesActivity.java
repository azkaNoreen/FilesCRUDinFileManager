package azka.noreen.filemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class BrowseFilesActivity extends AppCompatActivity {
    RecyclerView recycleView;
    TextView text;
    ArrayList<StorageItems> studentArrayList=new ArrayList<>();
    Toolbar toolbar;
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
        String rootPath=intent.getStringExtra("RootPath");
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
        RecyclerViewAdapter rva=new RecyclerViewAdapter();
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
            Toast.makeText(this, "Folder Clicked", Toast.LENGTH_SHORT).show();
        }else if(menuItemId==R.id.file){
            Toast.makeText(this, "File Clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}