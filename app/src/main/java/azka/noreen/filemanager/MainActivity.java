package azka.noreen.filemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Button browse;
    String root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        browse = findViewById(R.id.browse);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkStorage();
            }

            private void checkStorage() {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(MainActivity.this, permissions, 123);
                } else {
                    root=getAllFilesPath();
                    Intent intent=new Intent(MainActivity.this,BrowseFilesActivity.class);
                    intent.putExtra("RootPath",root);
                    startActivity(intent);

                }
            }
        });
    }
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                getAllFilesPath();
            }
            else{
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        private String getAllFilesPath() {
             String rootPath= Environment.getExternalStorageDirectory().getAbsolutePath(); //  if we want string
            return rootPath;
//            File rootPath = Environment.getExternalStorageDirectory();
//            File[] childFiles = rootPath.listFiles();
//            String childFileName= "";
//
//            for(int i=0; i<childFiles.length; i++){
//                childFileName= childFileName + "\n \n \n \n \n \n" + childFiles[i].getName();
//                if(childFiles[i].isDirectory())
//                    childFileName= childFileName + "\n \n" + childFiles[i].getName();
//            }
//            getAllText.setText(childFileName);
//            getAllText.setMovementMethod(new ScrollingMovementMethod());
        }
    }
