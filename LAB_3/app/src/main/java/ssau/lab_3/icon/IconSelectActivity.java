package ssau.lab_3.icon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ssau.lab_3.R;

public class IconSelectActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_select);
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent();
        int ic_id=0;
        switch (view.getId())
        {
            case R.id.ic1:  ic_id=1; break;
            case R.id.ic2:  ic_id=2; break;
            case R.id.ic3:  ic_id=3; break;
            case R.id.ic4:  ic_id=4; break;
            case R.id.ic5:  ic_id=5; break;
            case R.id.ic6:  ic_id=6; break;
            case R.id.ic7:  ic_id=7; break;
            case R.id.ic8:  ic_id=8; break;
            case R.id.ic9:  ic_id=9; break;
            case R.id.ic10: ic_id=10;break;
            case R.id.ic11: ic_id=11;break;
            case R.id.ic12: ic_id=12;break;
            case R.id.ic13: ic_id=13;break;
            case R.id.ic14: ic_id=14;break;
            case R.id.ic15: ic_id=15;break;
        }
        intent.putExtra("ic_number", ic_id);
        setResult(RESULT_OK, intent);
        finish();
    }
}
