package azka.noreen.filemanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<StorageItems> StorageItemsArrayList=new ArrayList<StorageItems>();
Context context;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.storageitems_item,parent,false);
        return new StorageItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        StorageItems st=StorageItemsArrayList.get(position);
        StorageItemsViewHolder StorageItemsViewHolder= (StorageItemsViewHolder) holder;
        StorageItemsViewHolder.StorageItemsPhone.setSelected(true);

        StorageItemsViewHolder.StorageItemsTextName.setText(st.getFileName());
        StorageItemsViewHolder.StorageItemsPhone.setText(st.getFilePath());

        StorageItemsViewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type;
                if(st.getFileName().endsWith(".txt"))
                    type=2;
                //file
                else
                    type=1;
                PopupMenu popupMenu=new PopupMenu(view.getContext(), StorageItemsViewHolder.more);
                popupMenu.getMenuInflater().inflate(R.menu.popupmenun, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
//                        Toast.makeText(view.getContext(), "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        switch((String)menuItem.getTitle()) {
                            case "Rename":
                               Toast.makeText(view.getContext(), "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();

                                BrowseFilesActivity activity =  ((BrowseFilesActivity) StorageItemsViewHolder.itemView.getContext());
                                if(type==2)
                                showDialog(activity,2,st,position);
                                else
                                    showDialog(activity,1,st,position);
                                break;
                            case "Delete":
                                try {
                                    DeleteFile(st.getFilePath(),position);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "details":
                                // code block
                                break;
                            default:
                                // code block
                        }

                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();

            }
        });

        File file=new File(st.getFilePath());

        if(file.isDirectory()){
            StorageItemsViewHolder.img.setImageResource(R.drawable.ic_folder);
        }
        else if(st.getFileName().endsWith(".mp3")||st.getFileName().endsWith(".wav")||st.getFileName().endsWith(".m4a"))
        {
            StorageItemsViewHolder.img.setImageResource(R.drawable.ic_music);

        }else if(st.getFileName().endsWith(".jpg")||st.getFileName().endsWith(".jpeg")||st.getFileName().endsWith(".webp"))
        {
            StorageItemsViewHolder.img.setImageResource(R.drawable.ic_baseline_image_24);
        }
        else if(st.getFileName().endsWith(".mp4")||st.getFileName().endsWith(".mov")||st.getFileName().endsWith(".avi")||st.getFileName().endsWith(".mkv"))
        {
            StorageItemsViewHolder.img.setImageResource(R.drawable.ic_baseline_videocam_24);
        }
        else if(st.getFileName().endsWith(".pdf"))
        {
            StorageItemsViewHolder.img.setImageResource(R.drawable.ic_baseline_menu_book_24);
        }
        else if(st.getFileName().endsWith(".pptx"))
        {
            StorageItemsViewHolder.img.setImageResource(R.drawable.ic_baseline_slideshow_24);
        }
        else if(st.getFileName().endsWith(".txt"))
        {
            StorageItemsViewHolder.img.setImageResource(R.drawable.ic_baseline_text_snippet_24);
        }
        else{

            StorageItemsViewHolder.img.setImageResource(R.drawable.ic_baseline_insert_drive_file_24);

        }

        StorageItemsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent=new Intent(view.getContext(),BrowseFilesActivity.class);
                    intent.putExtra("RootPath",st.getFilePath());
                    view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return StorageItemsArrayList.size();
    }
    public void setData(ArrayList<StorageItems> StorageItemsArrayList){
        this.StorageItemsArrayList=StorageItemsArrayList;
        notifyDataSetChanged();
    }
//to find views of single list xml file
    public static class StorageItemsViewHolder extends RecyclerView.ViewHolder{

        TextView StorageItemsTextName;
        TextView StorageItemsPhone;
    ImageView img;
    ImageView more;

    public StorageItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            StorageItemsTextName=itemView.findViewById(R.id.fileName);
            StorageItemsPhone=itemView.findViewById(R.id.filePath);
        img=itemView.findViewById(R.id.img);
        more=itemView.findViewById(R.id.option);


    }
    }
    public void showDialog(Activity activity, int type,StorageItems storageItems,int pos){
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

                        RenameFolder(storageItems.getFilePath(),storageItems.getFileName(),text.getText().toString(),pos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(type==2){

                    try {
                        RenameFile(storageItems.getFilePath(),text.getText().toString(),pos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                dialog.dismiss();

            }
        });
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addItem(text.getText().toString(),stext.getText().toString());

                Toast.makeText(v.getContext(), "cancel", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
        dialog.show();
    }
    public void RenameFile(String path,String rname,int pos) throws IOException {

        File file=new File(path);
        String folderPath=path.substring(0,path.lastIndexOf("/"));

        if(file.exists()){
            if(file.renameTo(new File(folderPath+"/"+rname+".txt")))
            {
//                filePath=path+"/"+name+".txt";
                Toast.makeText(context, "File Renamed Successfully", Toast.LENGTH_SHORT).show();
                StorageItemsArrayList.set( pos,new StorageItems(rname+".txt",folderPath+"/"+rname));

                notifyItemChanged(pos);
            }
            else
                Toast.makeText(context, "Sorry!not renamed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "file does not exist Exists", Toast.LENGTH_SHORT).show();
        }
    }
    public void RenameFolder(String path,String name,String rname,int pos) throws IOException {

        File oldFolder = new File(path.substring(0,path.lastIndexOf("/")),name);
        File newFolder = new File(path.substring(0,path.lastIndexOf("/")),rname);
        boolean success = oldFolder.renameTo(newFolder);
        if(success)
        Toast.makeText(context, "Folder Renamed Successfully", Toast.LENGTH_SHORT).show();
        StorageItemsArrayList.set( pos,new StorageItems(rname,path.substring(0,path.lastIndexOf("/"))+"/"+rname));

        notifyItemChanged(pos);

    }
    public void DeleteFile(String path,int position) throws IOException {
        File file=new File(path);

        if(file.exists()){
            if (file.isDirectory())
            {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++)
                {
                    new File(file, children[i]).delete();
                }
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            }
            boolean success =file.delete();
            if(success){
                StorageItemsArrayList.remove( position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,getItemCount());
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();}
                }
        else{
            Toast.makeText(context, "file does not exist Exists", Toast.LENGTH_SHORT).show();
        }
    }
}
