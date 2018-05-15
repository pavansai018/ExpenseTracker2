package com.krishnachaitanya.expensetracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.krishnachaitanya.expensetracker.Common.Common;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class Welcome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    Button btnDatePicker;
    EditText in_date, edtAddress;
    private int mYear, mMonth, mDay;
    FirebaseDatabase db;
    FirebaseAuth auth;
    DatabaseReference users;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Intent u;
    private BottomNavigationView bnav;
    private FrameLayout bframe;
    private HistoryFragment historyFragment;
    private GraphFragment graphFragment;
    TextView textView;
    Button fab;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appBarLayout=(AppBarLayout)findViewById(R.id.appbarid);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        setupViewPager(viewPager);


        tabLayout.setupWithViewPager(viewPager);

        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                u = new Intent(getApplicationContext(), plus.class);
                startActivity(u);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
/*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
*/
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_welcome);
        if (navigationHeaderView == null)
            Log.i("Null", "NavHV");
        TextView txtName = (TextView) navigationHeaderView.findViewById(R.id.txtexpenseTracker);
        CircleImageView imageAvatar = (CircleImageView) navigationHeaderView.findViewById(R.id.image_avatar);
        txtName.setText(Common.currentUser.getName());
        if (Common.currentUser.getAvatarUrl() != null && !TextUtils.isEmpty(Common.currentUser.getAvatarUrl())) {
            Picasso.with(this)
                    .load(Common.currentUser.getAvatarUrl())
                    .into(imageAvatar);

        }
//bottom navigation view
        bframe =(FrameLayout)findViewById(R.id.bframe);
        bnav=(BottomNavigationView)findViewById(R.id.bnav);
        historyFragment= new HistoryFragment();
        graphFragment=new GraphFragment ();



        bnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                    case R.id.nav_History:
                        bnav.setItemBackgroundResource(R.color.colorwhite);
                        setFragment(historyFragment);
                        return true;
                    case R.id.nav_graphs:
                        bnav.setItemBackgroundResource(R.color.colorwhite);
                        setFragment(graphFragment);
                        return true;
                    default:
                        return false;

                }

            }
        });




    }
    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter= new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "ONE");
        adapter.addFragment(new TwoFragment(), "TWO");



    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.bframe,fragment);
        fragmentTransaction.commit();
    }



    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_update_info) {
            showDialogUpdateInfo();



        }else if (id == R.id.nav_settings) {




        } else if (id == R.id.nav_change_pass) {
            showDialogChangePwd();




        } else if (id == R.id.nav_signout) {

            signOut();

        }
        else if(id == R.id.nav_alerts){
            showDialogNotification();
        }
        else if(id == R.id.nav_remainder){
            showDialogRemainders();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialogRemainders() {

        Intent i=new Intent(Welcome.this,hello.class);
        startActivity(i);



    }

    private void showDialogNotification() {

        String[] multiChoiceItems = getResources().getStringArray(R.array.dialog_multi_choice_array);
        final boolean[] checkedItems = {false, false,false};

        new AlertDialog.Builder(Welcome.this)
                .setTitle("Select your alerts time")
                .setMultiChoiceItems(multiChoiceItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index, boolean isChecked) {
                        Log.d("Welcome", "clicked item index is " + index);
                    }
                })
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDialogUpdateInfo() {
        LayoutInflater inflater=this.getLayoutInflater();
        View layout_pwd= inflater.inflate(R.layout.layout_update_information,null);
        final EditText edtName =(EditText)layout_pwd.findViewById(R.id.edtName);
        final EditText edtPhone =(EditText)layout_pwd.findViewById(R.id.edtPhone);
        in_date=(EditText)layout_pwd.findViewById(R.id.in_date);
        btnDatePicker=(Button)layout_pwd.findViewById(R.id.btn_date);
        btnDatePicker.setOnClickListener(this);
        final EditText  edtAddress=(EditText)layout_pwd.findViewById(R.id.edtAddress);

        final ImageView image_upload =(ImageView) layout_pwd.findViewById(R.id.image_upload);
        image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();


            }
        });

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Update Information")
                .customView(layout_pwd, true)
                .positiveText("Update")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        final android.app.AlertDialog waitingDialog = new SpotsDialog(Welcome.this);
                        waitingDialog.show();

                        String name = edtName.getText().toString();
                        String phone = edtPhone.getText().toString();
                        String dob=in_date.getText().toString();
                        String address=edtAddress.getText().toString();
                        Map<String,Object> updateInfo =new HashMap<>();
                        if(!TextUtils.isEmpty(name))
                            updateInfo.put("name",name);
                        if(!TextUtils.isEmpty(phone))
                            updateInfo.put("phone",phone);
                        if(!TextUtils.isEmpty(dob))
                            updateInfo.put("dob",dob);
                        if(!TextUtils.isEmpty(address))
                            updateInfo.put("address",address);

                        DatabaseReference userinformations=FirebaseDatabase.getInstance().getReference().child(Common.users_tbl).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        userinformations.updateChildren(updateInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(Welcome.this," Information Uploaded",Toast.LENGTH_SHORT);
                                    waitingDialog.dismiss();
                                }

                                else{
                                    Toast.makeText(Welcome.this,"Information Update Failed!",Toast.LENGTH_SHORT).show();


                                }
                            }
                        });

                        dialog.dismiss();
                    }
                })
                .negativeText("CANCEL")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
        builder.build().show();
    }

    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture:  "),Common.PICK_IMAGE_REQUEST );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data !=null && data.getData()!=null)
        {
            Uri saveUri =data.getData();
            if (saveUri != null)
            {

                final ProgressDialog mDialog =new ProgressDialog(this);
                mDialog.setMessage("Uploading......");
                mDialog.show();

                String imageName = UUID.randomUUID().toString();// random name image upload
                final   StorageReference imageFolder = storageReference.child("images/"+imageName);
                imageFolder.putFile(saveUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mDialog.dismiss();
                                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        //update this url to avatar property of User
                                        //first add avatar property on user model
                                        Map<String,Object> avatarUpdate = new HashMap<>();
                                        avatarUpdate.put("avatarUrl",uri.toString());
                                        DatabaseReference userinformations=FirebaseDatabase.getInstance().getReference(Common.users_tbl);
                                        userinformations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .updateChildren(avatarUpdate)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(Welcome.this,"Uploaded",Toast.LENGTH_SHORT);
                                                        }

                                                        else{
                                                            Toast.makeText(Welcome.this,"Upload error!",Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });




                                    }
                                });
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress =(100.0* taskSnapshot.getBytesTransferred() /taskSnapshot.getTotalByteCount() );
                                mDialog.setMessage("Uploaded "+progress+"%");
                            }
                        });




            }

        }
    }

    private void showDialogChangePwd() {
        LayoutInflater inflater=this.getLayoutInflater();
        View layout_pwd= inflater.inflate(R.layout.layout_change_pwd,null);
        final EditText edtPassword =(EditText)layout_pwd.findViewById(R.id.edtPassword);
        final EditText edtNewPassword =(EditText)layout_pwd.findViewById(R.id.edtNewPassword);

        final EditText edtRepeatPassword =(EditText)layout_pwd.findViewById(R.id.edtRepeatpassword);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Update Information")
                .customView(layout_pwd, true)
                .positiveText("CHANGE PASSWORD")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        final android.app.AlertDialog waitingDialog =new SpotsDialog(Welcome.this);
                        waitingDialog.show();

                        if(edtNewPassword.getText().toString().equals(edtRepeatPassword.getText().toString())){
                            String email =FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            //get auth credentials from the user for reautentication
                            AuthCredential credential= EmailAuthProvider.getCredential(email,edtPassword.getText().toString());
                            FirebaseAuth.getInstance().getCurrentUser()
                                    .reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                FirebaseAuth.getInstance().getCurrentUser()
                                                        .updatePassword(edtRepeatPassword.getText().toString())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    //update user information
                                                                    Map<String,Object> password = new HashMap<>();
                                                                    password.put("password",edtRepeatPassword.getText().toString());
                                                                    DatabaseReference userInformation=FirebaseDatabase.getInstance().getReference(Common.users_tbl);
                                                                    userInformation.child(auth.getCurrentUser().getUid())
                                                                            .updateChildren(password)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        Toast.makeText(Welcome.this,"Password Changed",Toast.LENGTH_SHORT);
                                                                                        waitingDialog.dismiss();
                                                                                    }
                                                                                    else{
                                                                                        Toast.makeText(Welcome.this,"Password was changed but not updated in Database",Toast.LENGTH_SHORT);
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                                else{

                                                                    Toast.makeText(Welcome.this,"Password doesn't change",Toast.LENGTH_SHORT);
                                                                }
                                                            }
                                                        });
                                            }
                                            else{
                                                waitingDialog.dismiss();
                                                Toast.makeText(Welcome.this,"Wrong Old Password",Toast.LENGTH_SHORT).show();
                                            }



                                        }
                                    });



                        }
                        else {
                            waitingDialog.dismiss();
                            Toast.makeText(Welcome.this,"Password doesn't match",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .negativeText("CANCEL")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
        builder.build().show();

    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(Welcome.this,MainActivity.class);
        startActivity(intent);
        finish();



    }


    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);



            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            in_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    private  class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }
}