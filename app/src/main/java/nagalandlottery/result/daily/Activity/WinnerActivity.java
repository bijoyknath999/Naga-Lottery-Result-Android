package nagalandlottery.result.daily.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.List;

import nagalandlottery.result.daily.BuildConfig;
import nagalandlottery.result.daily.R;
import nagalandlottery.result.daily.adapter.WinnerAdapter;
import nagalandlottery.result.daily.api.ApiInterface;
import nagalandlottery.result.daily.api.models.AllDatum;
import nagalandlottery.result.daily.api.models.Winner;
import nagalandlottery.result.daily.api.models.WinnerSearchData;
import nagalandlottery.result.daily.databinding.ActivityWinnerBinding;
import nagalandlottery.result.daily.utils.AnimatorUtils;
import nagalandlottery.result.daily.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WinnerActivity extends AppCompatActivity {

    private ActivityWinnerBinding binding;
    private GridLayoutManager gridLayoutManager;
    private List<Winner> winnerList;
    private WinnerAdapter winnerAdapter;
    private int page = 1, previousPage = 1;
    private int visibleItemCount, totalItemCount, pastVisiblesItems;
    private boolean loading = false;
    private boolean running = false;
    private final long DELAY = 2000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWinnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        winnerList = new ArrayList<>();

        gridLayoutManager = new GridLayoutManager(WinnerActivity.this,2, GridLayoutManager.VERTICAL,false);
        binding.winnerRecyclerview.setLayoutManager(gridLayoutManager);
        winnerAdapter = new WinnerAdapter(WinnerActivity.this, winnerList);
        binding.winnerRecyclerview.setAdapter(winnerAdapter);

        loadData();

        binding.winnerRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy>0) //check for scroll down
                {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) binding.winnerRecyclerview.getLayoutManager();
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (!loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            if (page!=previousPage)
                            {
                                binding.loadingProgress.setVisibility(View.VISIBLE);
                                binding.loadingProgress.getLayoutParams().height = 100;
                            }else {
                                binding.loadingProgress.setVisibility(View.GONE);
                            }

                            loading = true;

                            new Handler().postDelayed(()->{

                                if (page!=previousPage)
                                {
                                    loadData();
                                    previousPage = page;
                                }
                                else {
                                    loading = false;
                                }
                            }, 500);
                        }
                    }
                }
            }
        });

        binding.winnerSearchBtn.setOnClickListener(v -> {
            loadSearchData("");
            binding.winnerToolbarLayout.setVisibility(View.GONE);
            binding.winnerSearchLayout.setVisibility(View.VISIBLE);
            showSoftKeyboard(binding.winnerSearchEdit);
        });

        binding.winnerSearchBack.setOnClickListener(v -> {
            binding.winnerToolbarLayout.setVisibility(View.VISIBLE);
            binding.winnerSearchLayout.setVisibility(View.GONE);
            binding.winnerSearchEdit.setText(null);
            hideSoftKeyboard(binding.winnerSearchEdit, this);
        });

        binding.winnerSearchClear.setOnClickListener(v ->
        {
            binding.winnerSearchEdit.setText(null);
            loadSearchData("");
        });


        binding.winnerSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.winnerRecyclerview.setVisibility(View.GONE);

                if (!running)
                {
                    running = true;
                    loadSearchData(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void showSoftKeyboard(View view){
        if(view.requestFocus()){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideSoftKeyboard(View view, Context context){
        InputMethodManager imm =(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void loadData() {

        ApiInterface.getApiRequestInterface().getWinnersList(BuildConfig.API_KEY,page)
                .enqueue(new Callback<AllDatum>() {
                    @Override
                    public void onResponse(Call<AllDatum> call, Response<AllDatum> response) {
                        if (response.isSuccessful())
                        {
                            if (binding.loadingProgress.getVisibility() == View.VISIBLE) {
                                AnimatorUtils.collapseView(binding.loadingProgress);
                            }

                            AllDatum allDatum = response.body();
                            if (!allDatum.getWinners().getData().isEmpty())
                            {

                                page = Integer.parseInt(Tools.getLastPartString(allDatum.getWinners().getNextPageUrl(), "page="));

                                List<Winner> winners = allDatum.getWinners().getData();

                                for (Winner winner : winners)
                                {
                                    winnerList.add(winner);
                                }

                                winnerAdapter.notifyDataSetChanged();

                                loading = false;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AllDatum> call, Throwable t) {
                        Toast.makeText(WinnerActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void loadSearchData(String searchTxt){

        if (!winnerList.isEmpty())
            winnerList.clear();

        ApiInterface.getApiRequestInterface().searchWinners(BuildConfig.API_KEY,searchTxt)
                .enqueue(new Callback<WinnerSearchData>() {
                    @Override
                    public void onResponse(Call<WinnerSearchData> call, Response<WinnerSearchData> response) {
                        if (response.isSuccessful())
                        {
                            binding.winnerRecyclerview.setVisibility(View.VISIBLE);

                            List<Winner> winners = response.body().getWinners();

                            if (!winners.isEmpty())
                            {
                                for (Winner winner : winners)
                                {
                                    winnerList.add(winner);
                                }

                                winnerAdapter.notifyDataSetChanged();

                                running = false;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WinnerSearchData> call, Throwable t) {

                    }
                });

    }

}