package adapter;

/**
 * Created by julien on 14-06-08.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import tabs.*;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Recherche de livres
                return new SearchBooks();
            case 1:
                return new AddBooks();
            case 2:
            	return new Books();
                //return new Connection();
                //return new Books();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
