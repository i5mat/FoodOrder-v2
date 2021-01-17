package my.edu.utem.ftmk.foodorderv2.view.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.edu.utem.ftmk.foodorderv2.R;
import my.edu.utem.ftmk.foodorderv2.adapter.ViewPagerCategoryAdapter;
import my.edu.utem.ftmk.foodorderv2.model.Categories;
import my.edu.utem.ftmk.foodorderv2.view.home.HomeActivity;

public class CategoryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        initActionBar();
        initIntent();

    }

    private void initIntent() {
        Intent intent = getIntent();
        List<Categories.Category> categories =
                (List<Categories.Category>) intent.getSerializableExtra(HomeActivity.EXTRA_CATEGORY);
        int position = intent.getIntExtra(HomeActivity.EXTRA_POSITION, 0);

        ViewPagerCategoryAdapter adapter = new ViewPagerCategoryAdapter(
                getSupportFragmentManager(),
                categories);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(position, true);
        adapter.notifyDataSetChanged();

    }

    private void initActionBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}