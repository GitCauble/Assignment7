# Assignment7 News App

# Student name: Ryan Cauble

# ID: 00267379


# MainActivity
```
public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Info>>, SwipeRefreshLayout.OnRefreshListener {

    private InfoAdapter iadapt;

    private static int ID_load = 0;

    SwipeRefreshLayout refreshShit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        refreshShit = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        refreshShit.setOnRefreshListener(this);

        refreshShit.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        ListView listView = (ListView) findViewById(R.id.list_view);

        iadapt = new InfoAdapter(this);

        listView.setAdapter(iadapt);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Info news = iadapt.getItem(i);

                String url = news.url;

                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setData(Uri.parse(url));

                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(ID_load, null, this);
    }

    @Override
    public Loader<List<Info>> onCreateLoader(int id, Bundle args) {
        return new InfoLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Info>> loader, List<Info> data) {
        refreshShit.setRefreshing(false);

        if (data != null) {

            iadapt.setNotifyOnChange(false);

            iadapt.clear();

            iadapt.setNotifyOnChange(true);

            iadapt.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Info>> loader) {

    }

    @Override
    public void onRefresh() {
        getSupportLoaderManager().restartLoader(ID_load, null, this);
    }
}
```

# activity_main.xml
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.example.ryan.news_app.MainActivity"
    android:background="@drawable/news2">

<android.support.v4.widget.SwipeRefreshLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/swiperefresh"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ListView
        android:id="@+id/list_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scrollbars="none"
        android:divider="#00000000">

    </ListView>
</android.support.v4.widget.SwipeRefreshLayout>
</RelativeLayout>

```

# Async: InfoLoader
```
public class InfoLoader extends AsyncTaskLoader<List<Info>> {

    public InfoLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Info> loadInBackground() {

        List<Info> listOfNews = null;

        try {
            URL url = ApiStuff.createUrl();

            String jsonResponse = ApiStuff.makeHttpRequest(url);

            listOfNews = ApiStuff.parseJson(jsonResponse);

        } catch (IOException e) {

            Log.e("ApiStuff", "Error LoadInBackground: ", e);

        }

        return listOfNews;
    }
}

```

# array adapter: InfoAdapter
```
public class InfoAdapter extends ArrayAdapter<Info> {

    public InfoAdapter(Context context) {
        super(context, -1, new ArrayList<Info>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView
                    = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);


        TextView author = (TextView) convertView.findViewById(R.id.author);


        TextView date = (TextView) convertView.findViewById(R.id.date);


        TextView section = (TextView) convertView.findViewById(R.id.section);


        Info currentNews = getItem(position);


        title.setText(currentNews.getTitle());


        author.setText(currentNews.getAuthor());


        date.setText(currentNews.getDate());


        section.setText(currentNews.getSection());

        return convertView;
    }
}
```
# ApiStuff.java
```
public class ApiStuff {
    static String createStringUrl() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("order-by", "newest")
                .appendQueryParameter("show-references", "author")
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("q", "Food")
                .appendQueryParameter("api-key", "49cb44d9-848c-4891-ac5c-902ea3526a1c");
        String url = builder.build().toString();
        return url;
    }

    static URL createUrl() {
        String stringUrl = createStringUrl();
        try {
            return new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("APIStuff", "Error URL: ", e);
            return null;
        }
    }

    private static String formatDate(String rawDate) {
        String jsonDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDatePattern, Locale.US);
        try {
            Date parsedJsonDate = jsonFormatter.parse(rawDate);
            String finalDatePattern = "MMM d, yyy";
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern, Locale.US);
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            Log.e("APIStuff", "Error JSON date: ", e);
            return "";
        }
    }

    static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("mainActivity", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("APIStuff", "Error  HTTP request: ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    static List<Info> parseJson(String response) {
        ArrayList<Info> listOfNews = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject jsonResults = jsonResponse.getJSONObject("response");
            JSONArray resultsArray = jsonResults.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject oneResult = resultsArray.getJSONObject(i);
                String webTitle = oneResult.getString("webTitle");
                String url = oneResult.getString("webUrl");
                String date = oneResult.getString("webPublicationDate");
                date = formatDate(date);
                String section = oneResult.getString("sectionName");
                JSONArray tagsArray = oneResult.getJSONArray("tags");
                String author = "";

                if (tagsArray.length() == 0) {
                    author = null;
                } else {
                    for (int j = 0; j < tagsArray.length(); j++) {
                        JSONObject firstObject = tagsArray.getJSONObject(j);
                        author += firstObject.getString("webTitle") + ". ";
                    }
                }
                listOfNews.add(new Info(webTitle, author, url, date, section));
            }
        } catch (JSONException e) {
            Log.e("APIStuff s", "Error JSON response", e);
        }
        return listOfNews;
    }
}
```
