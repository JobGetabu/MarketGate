package com.marketgate.farmer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.aurelhubert.ahbottomnavigation.notification.AHNotification
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.marketgate.R
import com.marketgate.adapters.BottomBarAdapter
import com.marketgate.core.LoginActivity
import com.marketgate.utils.PreferenceHelper
import com.marketgate.utils.PreferenceHelper.set
import com.marketgate.utils.fetchColor
import com.marketgate.utils.fetchDrawable
import com.marketgate.utils.fetchString
import kotlinx.android.synthetic.main.activity_main.*



class FarmerActivity : AppCompatActivity() {


    companion object {
        private const val HOME = "Farmer"
        private const val PRODUCT = "Product"
        private const val AGENCY = "Agency"
        private const val NEWS = "News"
        private const val PROFILE = "Profile"

        fun newIntent(context: Context): Intent =
            Intent(context, FarmerActivity::class.java)
    }

    val googleApiClient: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    val googleSingInClient: GoogleSignInClient by lazy { GoogleSignIn.getClient(this, googleApiClient) }

    private lateinit var farmHome: FarmHome
    private var farmNews: FarmNews? = null
    private lateinit var farmProfile: FarmProfile
    private lateinit var farmAgency: FarmAgency
    private lateinit var adapter: BottomBarAdapter

    //firebase
    private lateinit var mAuth: FirebaseAuth

    private val notificationVisible = false

    override fun onStart() {
        super.onStart()
        if(mAuth.currentUser == null){
            launchActivity(LoginActivity::class.java)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.marketgate.R.layout.activity_main)
        setSupportActionBar(toolbar)

        mAuth = FirebaseAuth.getInstance()


        initViews()
    }

    private fun launchActivity(intentClass: Class<*>) {
        val intent = Intent(this, intentClass)
        startActivity(intent)
        overridePendingTransition(com.marketgate.R.anim.fade_out, com.marketgate.R.anim.fade_in)
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = HOME
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        setupViewPager()

    }

    //Setup the view pager
    private fun setupViewPager() {
        adapter = BottomBarAdapter(supportFragmentManager)
        farmHome = FarmHome()
        farmAgency = FarmAgency()
        farmNews = FarmNews()
        farmProfile = FarmProfile()

        adapter.addFragments(farmHome)
        //adapter.addFragments(farmNews)
        adapter.addFragments(farmAgency)
        adapter.addFragments(farmProfile)

        noSwipePager.adapter = adapter
        noSwipePager.setPagingEnabled(false)
        noSwipePager.offscreenPageLimit = 2

        noSwipePager.currentItem = 0
        bottomNav.currentItem = 0


        //bottom nav items
        val item1 = AHBottomNavigationItem(
            fetchString(R.string.farm_title_0,this),
            fetchDrawable(R.drawable.ic_home_trans,this)
        )
        val item2 = AHBottomNavigationItem(
            fetchString(R.string.farm_title_1,this),
            fetchDrawable(R.drawable.ic_adduser,this)
        )
        val item3 = AHBottomNavigationItem(
            fetchString(R.string.farm_title_2,this),
            fetchDrawable(R.drawable.ic_leave,this)
        )
        val item4 = AHBottomNavigationItem(
            fetchString(com.marketgate.R.string.farm_title_3,this),
            fetchDrawable(R.drawable.ic_adduser,this)
        )

        bottomNav.addItem(item1)
        bottomNav.addItem(item3)
        bottomNav.addItem(item4)

        bottomNav.setOnTabSelectedListener(onTabSelectedListener)

        bottomNav.defaultBackgroundColor = Color.WHITE
        bottomNav.accentColor = fetchColor(R.color.colorPrimary,this)
        bottomNav.inactiveColor = fetchColor(R.color.bottomtab_item_resting,this)

        //Color ripple effect will enable it at the finish of design
        //  Enables color Reveal effect
        bottomNav.isColored = true
        // Colors for selected (active) and non-selected items (in color reveal mode).
        bottomNav.setColoredModeColors(fetchColor(R.color.colorPrimary,this),
            fetchColor(R.color.bottomtab_item_resting,this))

        bottomNav.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        //translucent bottom navigation
        bottomNav.isTranslucentNavigationEnabled = true

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_signout) {
            val prefs = PreferenceHelper.customPrefs(this)
            prefs[PreferenceHelper.PREF_USER_TYPE] = ""
            googleSingInClient.signOut()
            mAuth.signOut()
            launchActivity(LoginActivity::class.java)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private var onTabSelectedListener: AHBottomNavigation.OnTabSelectedListener =
        AHBottomNavigation.OnTabSelectedListener { position, wasSelected ->
            //change fragments
            if (!wasSelected) {
                adapter.notifyDataSetChanged()
                noSwipePager.currentItem = position

            }

            // remove notification badge..
            val lastItemPos = bottomNav.itemsCount - 1
            if (notificationVisible && position == lastItemPos) {
                bottomNav.setNotification(AHNotification(), lastItemPos)
            }

            //fragment change logic
            when (position) {
                0 -> {
                }
            }

            true
        }
}
