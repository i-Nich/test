package ssau.lab_3.icon;

import ssau.lab_3.R;

/**
 * Created by Александр on 05.12.2017.
 */

public class IconManager
{
    public static int getIcon(int iconNumber)
    {
        switch (iconNumber)
        {
            case 1: return R.drawable.worker;
            case 2: return R.drawable.food_fork_drink;
            case 3: return R.drawable.hotel;
            case 4: return R.drawable.delete_variant;
            case 5: return R.drawable.sleep;
            case 6: return R.drawable.sword_cross;
            case 7: return R.drawable.dumbbell;
            case 8: return R.drawable.currency_usd;
            case 9: return R.drawable.car_sports;
            case 10:return R.drawable.camera_party_mode;
            case 11:return R.drawable.bike;
            case 12:return R.drawable.baby_buggy;
            case 13:return R.drawable.apple_safari;
            case 14:return R.drawable.all_inclusive;
            case 15:return R.drawable.account_multiple;
        }
        return 0;
    }
}
