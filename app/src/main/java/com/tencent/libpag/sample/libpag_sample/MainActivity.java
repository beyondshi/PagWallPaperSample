package com.tencent.libpag.sample.libpag_sample;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements SimpleListAdapter.ItemClickListener {

    private static final String[] items = new String[]{"A Simple PAG Animation", "Text Replacement", "Image Replacement", "Render Multiple PAG Files on A PAGView", "Create PAGSurface through texture ID", "Render an interval of the pag file", "Render Multiple PAGImageView", "Render PAGImageView in list",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        RecyclerView rv = findViewById(R.id.rv_);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(new SimpleListAdapter(items, this));
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
//                startActivity(new Intent(MainActivity.this, TestActivity.class));
//                break;
            case 1:
            case 2:
            case 3:
            case 5:
                goToAPIsDetail(position);
                break;
            case 4:
                goToTestDetail(position);
                break;
            case 6:
                startActivity(new Intent(MainActivity.this, MultiplePAGImageViewActivity.class));
                break;
            case 7:
                startActivity(new Intent(MainActivity.this, PAGImageViewListActivity.class));
                break;
            default:
                break;
        }
    }

    private void goToAPIsDetail(int position) {
        if (position == 0) {
            Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(this, LiveWallpaperService.class));
            startActivity(intent);
        } else {
            Intent intent1 = new Intent(MainActivity.this, APIsDetailActivity.class);
            intent1.putExtra("API_TYPE", position);
            startActivity(intent1);
        }
    }

    private void goToTestDetail(int position) {
        Intent intent = new Intent(MainActivity.this, TextureDemoActivity.class);
        intent.putExtra("API_TYPE", position);
        startActivity(intent);
    }
}
