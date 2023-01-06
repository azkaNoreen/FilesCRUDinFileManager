package azka.noreen.filemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FileContents extends AppCompatActivity {
TextView con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_contents);
        con=findViewById(R.id.con);
        Intent intent=getIntent();
        String contetn=intent.getStringExtra("FileContent");
        con.setText(contetn.toString());
    }
}