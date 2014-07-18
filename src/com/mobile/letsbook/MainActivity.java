package com.mobile.letsbook;

import window.Connection;
import window.Inscription;
import adapter.TabsPagerAdapter;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import element.User;

public class MainActivity extends FragmentActivity implements
        android.app.ActionBar.TabListener {
	
    // Navigation
    private ViewPager viewPager;
    private TabsPagerAdapter adapter;
    private ActionBar actionBar;
    private Inscription dialogInscription;
    private Connection dialogConnection;
    private String[] tabsTitle = {"Rechercher", "Ajouter", "Mes Livres"};
    private User user;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation
        viewPager   			= (ViewPager) findViewById(R.id.pager);
        actionBar   			= getActionBar();
        adapter					= new TabsPagerAdapter(getSupportFragmentManager());
        dialogConnection 		= new Connection();
        dialogInscription 		= new Inscription();
        		
        viewPager.setAdapter(adapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        user = null;
        
        // Ajout des onglets et lien d'un listener pour repondre aux choix de l'usager
        for(String tab : tabsTitle){
            actionBar.addTab(actionBar.newTab().setText(tab).setTabListener(this));
        }

        // Ajout d'un listener pour repondre a un usager qui change un onglet en "Cliquer-Deplasser"
        viewPager.setOnPageChangeListener(onPageChangeListener());
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            
            case R.id.itemInscription:
            	dialogInscription.show(getSupportFragmentManager(), "dialog_inscription");
            	return true;
            	
            case R.id.itemConnection:
            	dialogConnection.show(getSupportFragmentManager(), "dialog_Connection");
            	return true;
            	
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    // Evenement appelle lorsqu'un onglet est selectionne
    public void onTabSelected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        
    	// Demande un changement d'onglet
    	viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(android.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
    
    // Ajout d'un listener pour repondre a un usager qui change un onglet en "Cliquer-Deplasser"
    private OnPageChangeListener onPageChangeListener(){
    	
    	return new OnPageChangeListener() {
			
			@Override
			
			// Apres un changement de page, mettre le focus sur le bon onglet
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		};
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public void setUser(User user){
    	this.user = user;
    }
    
    public User getUser(User user){
    	return this.user;
    }
}
