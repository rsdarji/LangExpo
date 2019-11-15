package com.langexpo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.langexpo.R;
import com.langexpo.admin.activity.AddLanguage;
import com.langexpo.admin.activity.AddLecture;
import com.langexpo.model.Favorite;
import com.langexpo.model.Lecture;
import com.langexpo.utility.Constant;
import com.langexpo.utility.LangExpoAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FavoriteAdaptor extends RecyclerView.Adapter<FavoriteAdaptor.FavoriteViewHolder> implements Filterable {
    //this context we will use to inflate the layout
    private Context mCtx;
    private Activity activity;

    //we are storing all the products in a list
    private List<Favorite> favoriteList;
    private List<Favorite> mDataFiltered;
    //getting the context and product list with constructor
    public FavoriteAdaptor(Context mCtx, List<Favorite> favoriteList) {
        this.mCtx = mCtx;
        this.favoriteList = favoriteList;
        this.mDataFiltered = favoriteList;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.favorite_row_layout_admin, null);
        return new FavoriteAdaptor.FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        //getting the product of the specified position
        Favorite favorite = favoriteList.get(position);

        //binding the data with the viewholder views

        holder.word.setText(favorite.getWord());
        holder.resultWord.setText(favorite.getResultWord());
        holder.unfavourite.setImageResource(R.drawable.ic_favorite_filled);



        holder.unfavourite.setOnClickListener(v -> {
            //Toast.makeText(mCtx, "item clicked", Toast.LENGTH_SHORT).show();
            if(holder.unfavourite.getDrawable().getConstantState() == mCtx.getDrawable( R.drawable.ic_favorite).getConstantState()){
                holder.unfavourite.setImageResource(R.drawable.ic_favorite_filled);

                new AddFavoriteAsyncTask(mCtx, Constant.userId, true,
                        holder.word.getText().toString(), holder.resultWord.getText().toString(),holder,
                        position).execute();
            }else{

                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx, R.style.MyDialogTheme)
                        .setTitle("Unfavourite?")
                        .setMessage("The word will be deleted from your favourite list.\n Do you want to continue?")
                        .setIconAttribute(android.R.attr.alertDialogIcon)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Toast.makeText(AddLanguage.this, "Yaay", Toast.LENGTH_SHORT).show();
                                new AddFavoriteAsyncTask(mCtx, Constant.userId, false,
                                        holder.word.getText().toString(), holder.resultWord.getText().toString(), holder,
                                        position).execute();

                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Toast.makeText(AddLanguage.this, "no", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();

            }
            /*Intent intent = new Intent(mCtx, AddLecture.class);
            intent.putExtra("lectureId", lecture.getLectureId());
            intent.putExtra("lectureName",lecture.getLectureName());
            intent.putExtra("lectureContent", lecture.getLectureContent());
            intent.putExtra("languageId",lecture.getLanguageId());
            intent.putExtra("languageName", lecture.getLanguageName());
            intent.putExtra("levelId",lecture.getLevelId());
            intent.putExtra("levelName",lecture.getLevelName());
            intent.putExtra("sequenceNumber",lecture.getSequenceNumber());
            mCtx.startActivity(intent);*/
        });

    }


    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView word;
        TextView resultWord;
        ImageView unfavourite;

        public FavoriteViewHolder(View itemView) {
            super(itemView);

            word = itemView.findViewById(R.id.dictionary_row_layout_word);
            resultWord = itemView.findViewById(R.id.dictionary_row_layout_word_meaning);
            unfavourite = itemView.findViewById(R.id.imageView_unfavourite);
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();
                if (Key.isEmpty()) {
                    mDataFiltered = favoriteList ;
                }
                else {
                    List<Favorite> lstFiltered = new ArrayList<>();
                    for (Favorite row : favoriteList) {
                        if (row.getWord().toLowerCase().contains(Key.toLowerCase())){
                            lstFiltered.add(row);
                        }
                        if (row.getResultWord().toLowerCase().contains(Key.toLowerCase())){
                            lstFiltered.add(row);
                        }
                    }
                    mDataFiltered = lstFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values= mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //levelList.clear();
                favoriteList = (List<Favorite>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class AddFavoriteAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog progressBar;
        private long userId;
        private boolean favorite;
        private String searchWord;
        private String translatedText;
        private FavoriteViewHolder holder;
        private int position;


        public AddFavoriteAsyncTask(Context activity, long userId,
                                    boolean favorite, String searchWord,
                                    String translatedText, FavoriteViewHolder holder,
                                    int position){
            progressBar = new ProgressDialog(activity);
            this.userId = userId;
            this.favorite = favorite;
            this.searchWord = searchWord;
            this.translatedText = translatedText;
            this.holder = holder;
            this.position = position;
        }

        protected void onPreExecute(){
            progressBar.setMessage("Loading...");
            progressBar.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            BufferedReader reader = null;
            StringBuilder stringBuilder = new StringBuilder();

            String methodName = "markFavorite";
            stringBuilder.append(Constant.PROTOCOL);
            stringBuilder.append(Constant.COLON);
            stringBuilder.append(Constant.FORWARD_SLASH);
            stringBuilder.append(Constant.FORWARD_SLASH);
            stringBuilder.append(Constant.WEB_SERVICE_HOST);
            stringBuilder.append(Constant.COLON);
            stringBuilder.append(Constant.WEB_SERVICE_PORT);
            stringBuilder.append(Constant.FORWARD_SLASH);
            stringBuilder.append(Constant.CONTEXT_PATH);
            stringBuilder.append(Constant.FORWARD_SLASH);
            stringBuilder.append(Constant.APPLICATION_PATH);
            stringBuilder.append(Constant.FORWARD_SLASH);
            stringBuilder.append(Constant.CLASS_PATH);
            stringBuilder.append(Constant.FORWARD_SLASH);
            stringBuilder.append(methodName);


            try {
                String urlParameters  = "userId="+Constant.userId+"&favorite="+favorite+
                        "&searchWord="+searchWord+"&resultWord="+translatedText;

                byte[] postData       = urlParameters.getBytes();
                int    postDataLength = postData.length;
                url = new URL(stringBuilder.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                // uncomment this if you want to write output to this url
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects( false );
                connection.setRequestProperty( "charset", "utf-8");
                connection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                connection.setUseCaches( false );
                // give it 15 seconds to respond
                connection.setReadTimeout(15*1000);
                connection.setConnectTimeout(15*1000);
                try( DataOutputStream wr = new DataOutputStream( connection.getOutputStream())) {
                    wr.write( postData );
                }

                connection.connect();

                // read the output from the server
                stringBuilder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                System.out.println("response: "+stringBuilder.toString());
            }
            catch (Exception e) {
                /*if(e instanceof ConnectException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(Dictionary.this, Dictionary.this);
                    alertDialog.alertDialog("Network issue", "Due to maintenance, currently service unavailable");

                }else if(e instanceof SocketTimeoutException){
                    progressBar.dismiss();
                    LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(Dictionary.this, Dictionary.this);
                    alertDialog.alertDialog("Time out", "Please try again.");
                }*/
                e.printStackTrace();
                try {
                    throw e;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            finally {
                if (reader != null) {
                    try{
                        reader.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return stringBuilder.toString();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println(result);
            try {
                JSONObject loginResponse = new JSONObject(result);
                if(loginResponse.length()!=0 &&
                        loginResponse.get("status").toString().equalsIgnoreCase("ok") ) {
                    holder.unfavourite.setImageResource(R.drawable.ic_favorite);
                    Toast toast = Toast.makeText(mCtx,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    /*Intent intent = new Intent(Dictionary.this, LectureList.class);
                    startActivity(intent);*/

                    favoriteList.remove(position);
                    notifyDataSetChanged();
                    progressBar.dismiss();
                    toast.show();
                }
                else if(loginResponse.get("status").toString().equalsIgnoreCase("error")){
                    if(loginResponse.get("code").toString().equalsIgnoreCase("LE_D_411")) {
                        LangExpoAlertDialog alertDialog = new LangExpoAlertDialog(mCtx, activity);
                        alertDialog.alertDialog("Duplicate", loginResponse.get("message").toString());
                    }
                    Toast toast = Toast.makeText(mCtx,loginResponse.get("message").toString(),Toast.LENGTH_LONG);
                    progressBar.dismiss();
                    toast.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
