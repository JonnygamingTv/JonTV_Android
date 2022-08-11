package com.app.jontv.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.jontv.MainActivity;
import com.app.jontv.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//import android.webkit.WebView;
//import android.widget.Toast;


public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private ViewGroup contain;
    private LayoutInflater inflat;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        contain = container;
        inflat = inflater;
        final TextView textView = root.findViewById(R.id.text_home);
        new Request().execute();
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        MainActivity.navView.setVisibility(View.INVISIBLE);
        return root;
    }
    public ArrayList<String> videos = new ArrayList<String>();
    public ArrayList<String> videoSources = new ArrayList<String>();
    public String currentVideo="";
    JSONObject vidInfo;
    TableLayout Comments;
    public void ShowVideo(String videoID, String vsource){
        String urltoCheck="https://jontv.me/gv/?v="+videoID;
        VideoView video = getActivity().findViewById(R.id.videoview);
        if(video.isPlaying()) return;
        if(videoID==currentVideo)return;
        if(currentVideo!="") video.suspend();
        currentVideo=videoID;
        if(Comments == null){
            TableRow newRow = new TableRow(getContext());
            newRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            TableLayout tableLay = getActivity().findViewById(R.id.table_home);
            TextView tv = new TextView(getContext());
            tv.setText("Comments:");
            newRow.addView(tv);
            TableRow NextRow = new TableRow(getContext());
            Comments = new TableLayout(getContext());
            NextRow.addView(Comments);
            newRow.addView(NextRow);
            tableLay.addView(newRow);
            //Comments.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        Comments.removeAllViews();
        //Comments.setGravity(Gravity.CENTER_HORIZONTAL);
        new getComments().execute();
        //video.setTop(-10);
        //video.setLeft(0);
        try {
            Uri videoYes = Uri.parse("https://jontv.me/" + vsource);
            //video.setVideoPath("http://JonTV.me/" + videoSources.get(v.getId()));
            video.setVideoURI(videoYes);
            if(vidInfo.has(videoID)){}
            MediaController mediacontroller = new MediaController(getContext());
            mediacontroller.setMediaPlayer(video);
            //mediacontroller.setAnchorView(video);
            video.setMediaController(mediacontroller);
            video.start();
            video.requestFocus();
            video.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = video.getLayoutParams();
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());
            video.setLayoutParams(params);
        }catch(Exception e){
            e.printStackTrace();
            video.setVisibility(View.INVISIBLE);
        }
    }
    public class MyOwnListener implements View.OnClickListener
    {
        // ...
        @Override
        public void onClick(View v)
        {
            String videoID = videos.get(v.getId());
            String source = videoSources.get(v.getId()).replace(" ","%20");
            ShowVideo(videoID,source);
            //Toast.makeText(getContext(), videoID, Toast.LENGTH_LONG).show();
            //System.out.println(videoID);
            //System.out.println(video.getWidth());
        }
    }

    private class Request extends AsyncTask<URL, Integer, ArrayList<String>> {
        protected ArrayList<Drawable> drawables=new ArrayList<Drawable>();
        protected ArrayList<String> doInBackground(URL... urls) {
            long totalSize = 0;
            ArrayList<String> responsResul=new ArrayList<String>();
            try {
                String url = "https://jontv.me/gr/";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "Jon_Android");
                con.setDoOutput(true);
                con.setReadTimeout(10000);
                int responseCode = con.getResponseCode();
                InputStream inputStream;
                if (200 <= responseCode && responseCode <= 299) {
                    inputStream = con.getInputStream();
                } else {
                    inputStream = con.getErrorStream();
                }
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                inputStream));
                StringBuilder response = new StringBuilder();
                String currentLine;
                while ((currentLine = in.readLine()) != null)
                    response.append(currentLine);
                in.close();
                JSONObject JSobj = new JSONObject(response.toString());
                JSONArray names = JSobj.names();
                JSONArray values = JSobj.toJSONArray(names);
                for (int i = 0; i < values.length(); i++) {
                    //System.out.println(names.getString(i));
                    try {
                        videos.add(names.getString(i));
                        String DisURL = "https://jontv.me/gv/?v=" + names.getString(i) + "&r=1";
                        URL obje = new URL(DisURL);
                        HttpURLConnection connectio = (HttpURLConnection) obje.openConnection();
                        connectio.setRequestMethod("GET");
                        connectio.setRequestProperty("User-Agent", "Jon_Android");
                        connectio.setDoOutput(true);
                        connectio.setReadTimeout(7000);
                        int responsCod = connectio.getResponseCode();
                        InputStream inputtream;
                        if (200 <= responsCod && responsCod <= 299) {
                            inputtream = connectio.getInputStream();
                        } else {
                            inputtream = connectio.getErrorStream();
                        }
                        BufferedReader insider = new BufferedReader(new InputStreamReader(inputtream));
                        StringBuilder respons = new StringBuilder();
                        String currentline;
                        while ((currentline = insider.readLine()) != null)
                            respons.append(currentline);
                        insider.close();
                        JSONObject JSONobj = new JSONObject(respons.toString());
                        vidInfo.put(names.getString(i),JSONobj);
                        JSONArray namez = JSONobj.names();
                        JSONArray valuez = JSONobj.toJSONArray(namez);
                        videoSources.add(JSONobj.get("vF").toString());
                        String title = JSONobj.get("n").toString();
                        String thumbnail = "";
                        if(JSONobj.get("thumb").toString() != "") {
                            thumbnail = JSONobj.get("thumb").toString();
                        }
                        int views = Integer.parseInt(JSONobj.get("v").toString());
                        responsResul.add(respons.toString());
                        if(thumbnail.length()>10) {
                            Drawable drawable = LoadImageFromWeb("https://jontv.me/" + thumbnail);
                            if(drawable != null) drawables.add(drawable);
                        }
                        //SetContent(thumbnail,title,views);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    totalSize++;
                }
                return responsResul;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(ArrayList<String> result){
            MainActivity.navView.setVisibility(View.VISIBLE);
            if(result!=null) {
                ImageView JonTVLogo = getActivity().findViewById(R.id.imagewait);
                JonTVLogo.setVisibility(View.INVISIBLE);
                try {
                    for (int i=0;i<result.size(); i++) {
                        JSONObject JSONobj = new JSONObject(result.get(i));
                        JSONArray namez = JSONobj.names();
                        JSONArray valuez = JSONobj.toJSONArray(namez);
                        String title = JSONobj.get("n").toString();
                        String thumbnail = "/jontv/jontv_dark_512.png";
                        if (JSONobj.get("thumb").toString() != "") {
                            thumbnail = JSONobj.get("thumb").toString();
                        }
                        int views = Integer.parseInt(JSONobj.get("v").toString());
                        SetContent(thumbnail, title, views, result.get(i));
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(getContext(), "There is nothing to recommend :(", Toast.LENGTH_LONG).show();
            }
        }
        private Drawable LoadImageFromWeb(String url) {
            try {
                InputStream is = (InputStream) new URL(url).getContent();
                Drawable d = Drawable.createFromStream(is, url);
                is.close();
                return d;
            } catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }
        int posyes=0;
        int vidNum=0;
        private void SetContent(String thumb, String title, int views, String vID){
            LayoutInflater inflater = inflat;
            ViewGroup container = contain;
            View root = inflater.inflate(R.layout.fragment_home, container, false);
            TableLayout tableLay = getActivity().findViewById(R.id.table_home);
            Context bcontext=getContext();
            TableRow newRow = new TableRow(bcontext);
            newRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            //ImageView image;
            ImageView image = new ImageView(bcontext);
            Drawable drawable = getResources().getDrawable(R.drawable.jontvme);// = LoadImageFromWeb("http://jontv.me/" + thumb);
            image.setImageResource(R.drawable.jontvme);
            if(drawables != null && !drawables.isEmpty() && thumb.startsWith("uploads")) {
                //System.out.println(posyes);
                try {
                    image.setImageURI(Uri.parse("https://jontv.me/"+thumb));
                    drawable = drawables.get(posyes);
                    posyes++;
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            image.setImageDrawable(drawable);
            image.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            image.requestLayout();
            image.getLayoutParams().height=200;
            image.getLayoutParams().width=TableLayout.LayoutParams.MATCH_PARENT;
            image.setAdjustViewBounds(true);
            newRow.addView(image);
            TextView tv;
            tv = new TextView(bcontext);
            tv.setText(title);
            tv.setBackgroundColor(Color.parseColor("#80808080"));
            tv.setHeight(200);
            tv.setWidth(700);
            tv.setTextColor(Color.parseColor("#f6f6f6ad"));
            tv.setPadding(20, 10, 20, 10);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TableLayout.LayoutParams trparams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            trparams.setMargins(30,30,30,30);
            newRow.setLayoutParams(trparams);
            newRow.addView(tv);
            newRow.setId(vidNum);
            newRow.setOnClickListener(new MyOwnListener());
            try {
                tableLay.addView(newRow);
            } catch(Error e) {
                e.printStackTrace();
            }
            vidNum++;
            /*System.out.println(tableLay);
            newRow.postInvalidate();
            tableLay.postInvalidate();
            Intent thisActivity = new Intent(getContext(),getClass());*/
        }
    }
    private class getComments extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... urls) {
            try {
                String url = "https://jontv.me/getcomments/?v="+currentVideo;
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "Jon_Android");
                con.setDoOutput(true);
                con.setReadTimeout(10000);
                int responseCode = con.getResponseCode();
                InputStream inputStream;
                if (200 <= responseCode && responseCode <= 299) {
                    inputStream = con.getInputStream();
                } else {
                    inputStream = con.getErrorStream();
                }
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                inputStream));
                StringBuilder response = new StringBuilder();
                String currentLine;
                while ((currentLine = in.readLine()) != null)
                    response.append(currentLine);
                in.close();
                return response.toString();
            } catch(Exception err){
                err.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result){
            //Comments.addView();
            if(result!=""){
                try {
                    JSONArray JSobj = new JSONArray(result);
                    for (int i = 0; i < JSobj.length(); i++){
                        TextView user = new TextView(getContext());
                        TextView commentate = new TextView(getContext());
                        JSONObject commentar = new JSONObject(String.valueOf(JSobj.get(i)));
                        commentate.setText(commentar.get("content").toString());
                        JSONObject author = new JSONObject(String.valueOf(commentar.get("author")));
                        user.setText(author.get("name").toString());
                        user.setBackgroundColor(Color.parseColor("#80808080"));
                        user.setTextColor(Color.parseColor("#3D6AF2"));
                        user.setPadding(15, 5, 15, 5);
                        TableRow newRow = new TableRow(getContext());
                        newRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
                        newRow.addView(user);
                        Comments.addView(newRow);
                        newRow = new TableRow(getContext());
                        newRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
                        newRow.addView(commentate);
                        Comments.addView(newRow);
                        //Comments.addView(newRow);
                    }
                    //tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    //user.setHeight(200);
                    //user.setWidth(700);
                } catch(Exception error) {
                    error.printStackTrace();
                }
            }
        }
    }
}