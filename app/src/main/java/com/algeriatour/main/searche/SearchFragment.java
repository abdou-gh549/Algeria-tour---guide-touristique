package com.algeriatour.main.searche;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.algeriatour.R;
import com.algeriatour.uml_class.SearchResult;
import com.algeriatour.utils.AlgeriaTourUtils;
import com.algeriatour.utils.StaticValue;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private final String search_file = "at_find.php";
    private final String get_image_file = "at_get_image_of.php";
    private final String search_url = StaticValue.MYSQL_SITE + search_file;
    private final String get_image__url = StaticValue.MYSQL_SITE + get_image_file;


    @BindView(R.id.search_input_editText)
    EditText searchInput;
    @BindView(R.id.search_information_textVeiw)
    TextView resultTextView;
    @BindView(R.id.search_result_recylerView)
    RecyclerView searchRecylerView;

    SpotsDialog progressDialog;
    SearchRecyclerViewAdapter searchRecyclerViewAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        progressDialog = new SpotsDialog(getContext());
        setUpAdapter();

        return view;
    }
    private void setUpAdapter() {
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter();
        searchRecylerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        searchRecylerView.setLayoutManager(mLayoutManager);

        searchRecylerView.setAdapter(searchRecyclerViewAdapter);
    }

    @OnClick(R.id.search_input_imageView)
    public void onSearchClicked() {
        Log.d("tixx ", "onSearchClicked");
        String searchKey = searchInput.getText().toString();
        if (!searchKey.isEmpty()) {
            resultTextView.setVisibility(View.GONE);
            searchRecyclerViewAdapter.clearData();
            progressDialog.show();
            doSearch(searchKey);
        } else {
            Toasty.warning(getContext(), getString(R.string.search_empty_input_message), Toast.LENGTH_LONG, true).show();
        }
    }

    private void doSearch(String key) {
        AndroidNetworking.post(search_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_KEY, key)
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt(StaticValue.JSON_NAME_SUCCESS) == 1) {
                        JSONArray jsonArray = response.getJSONArray(StaticValue.JSON_NAME_RESULT);
                        if (jsonArray.length() == 0) {
                            showResultTextView(getString(R.string.search_not_found));
                        } else {
                            ArrayList<SearchResult> searchResults = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                searchResults.add(parseSearchResult(jsonArray.getJSONObject(i)));
                                searchRecyclerViewAdapter.addSearchResult(searchResults.get(i));
                            }

                            for (int i = 0; i< searchResults.size(); i++){
                                loadImageOf(searchResults.get(i).getId(), i, searchResults.get(i)
                                        .getType());
                            }

                        }
                    } else {
                        showToastError(AlgeriaTourUtils.getString(R.string
                                .server_error));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "load image reponst catch 1 " + e.getMessage() + " " + e
                            .getCause());
                }

                progressDialog.dismiss();
            }

            @Override
            public void onError(ANError anError) {
                showToastError(AlgeriaTourUtils.getString(R.string.connection_fail));
                progressDialog.dismiss();
            }
        });
    }

    private void loadImageOf(long id, int position, String type) {
        String what;
        what = ("ville".equals(type.toLowerCase())) ? StaticValue.PHP_TOWN : StaticValue.PHP_POINT;

        AndroidNetworking.post(get_image__url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_WHAT, what)
                .addBodyParameter(StaticValue.PHP_ID, id + "")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getInt(StaticValue.JSON_NAME_SUCCESS) == 1){
                        String imageString = response.getString(StaticValue.JSON_NAME_IMAGE);
                        searchRecyclerViewAdapter.setImage( AlgeriaTourUtils.parsImage
                                (imageString), position);
                    }else{
                        Log.d("tixx", "cant load image server error : " + response.getInt
                                (StaticValue.JSON_NAME_MESSAGE));
                    }

                    Log.d("tixx", "reponse load ville image id = "+id+"  msg -> " + response.getInt
                            (StaticValue.JSON_NAME_SUCCESS));
                    Log.d("tixx", "id  = "+id+"  stromg -> " +  response.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "reponst catch id = "+id+"  msg -> " +  e.getMessage());
                    Log.d("tixx", "reponst catch id = "+id+"  stromg -> " +  response.toString());

                }
            }

            @Override
            public void onError(ANError error) {
                Log.d("tixx", "error " + error.getMessage());
            }
        });
    }

    private void showResultTextView(String msg) {
        resultTextView.setText(msg);
        resultTextView.setVisibility(View.VISIBLE);
    }

    private void showToastError(String msg) {
        Toasty.error(getContext(), msg, Toast.LENGTH_LONG, true).show();
    }

    private SearchResult parseSearchResult(JSONObject jsonObject) throws JSONException {
        SearchResult searchResult = new SearchResult();
        searchResult.setId(jsonObject.getLong(StaticValue.JSON_NAME_ID));
        searchResult.setName(jsonObject.getString(StaticValue.JSON_NAME_NAME));
        searchResult.setType(jsonObject.getString(StaticValue.JSON_NAME_TYPE));
        searchResult.setWilaya(jsonObject.getString(StaticValue.JSON_NAME_WILAYA));
        searchResult.setDescreption(jsonObject.getString(StaticValue.JSON_NAME_DESCREPTION));
        searchResult.setRate(Float.parseFloat(jsonObject.getString(StaticValue.JSON_NAME_RATING)));
        if(! "ville".equals(searchResult.getType().toLowerCase())){
            searchResult.setLongitude(jsonObject.getDouble(StaticValue.JSON_NAME_LONGITUDE));
            searchResult.setLatitude(jsonObject.getDouble(StaticValue.JSON_NAME_LATITUDE));
        }
        return searchResult;
    }


}
