package com.app.jontv.ui.home;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    //private MutableLiveData<Object> mTable;
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Recommended");
        //new Request().execute();
    }
    public LiveData<String> getText() {
        return mText;
    }
    /*
    private class Request extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            long totalSize = 0;
            try {
                String url = "http://JonTV.me/recommend.php";
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
                        String DisURL = "http://JonTV.me/getvideo.php?v=" + names.getString(i) + "&recommended=1";
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
                        JSONArray namez = JSONobj.names();
                        JSONArray valuez = JSONobj.toJSONArray(namez);
                        String title = JSONobj.get("n").toString();
                        String thumbnail = JSONobj.get("thumb").toString();
                        int views = Integer.parseInt(JSONobj.get("v").toString());
                        System.out.println("Title:" + title);
                        System.out.println("Thumb:" + thumbnail);
                        System.out.println("Views:" + views);
                        /*new SetContent(title);
                        if(thumbnail.length()>5 && thumbnail != null) {
                            Drawable drawable = LoadImageFromWeb("http://jontv.me/"+thumbnail);
                            System.out.println("Drawable: "+drawable);
                        }
                        mText.postValue(thumbnail+"\n"+title+"\n"+views+" views");
                    } catch (Exception e) {
                        Log.e(names.getString(i), e.toString());
                    }
                    totalSize++;
                }

            } catch (Exception e) {
                Log.e("URLMain", e.toString());
            }
            return totalSize;
        }
    }
    private Drawable LoadImageFromWeb(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            is.close();
            return d;
        } catch(Exception e){
            System.out.println(e.toString());
            return null;
        }
    }*/
}
