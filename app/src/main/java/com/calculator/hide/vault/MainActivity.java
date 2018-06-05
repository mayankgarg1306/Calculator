package com.calculator.hide.vault;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    private String password="";
    private String temppassword="";
    private String key="";
    private static final String TAG="MyMessage";
    NumberFormat nf = new DecimalFormat("##.###");
    int operator;
    private TextView editText;
    boolean opcheck=false;
    private static String a="";
    double number1=0,number2=0;
    double number3=0;
    TextView textView2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            new AlertDialog.Builder(this)
                    .setTitle("App Features/Change Logs")
                    .setMessage("* Simple and Interactive Interface\n" +
                                "* Smoothness Redefined...\n" +
                                "* Retain password even on Reinstall \n" +
                                "* Fixed Bugs  \n" +
                                "* Restore hidden files on Reinstall \n" +
                                "* Support hiding of all file formats\n" +
                                "  and folders                        ")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

            prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();

        }


        button=(Button)findViewById(R.id.buttoncl);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editText.setText("");
                number1=number2=number3=0;
                a="";
                editText.setText("0");
                opcheck=false;
                return true;
            }
        });
        password=RetrieveData("passkey");
        key=RetrieveData("recoverykey");
        textView2=(TextView)findViewById(R.id.textView2);
        if(password.equals("")){
            textView2.setText("set a numeric password and press '='");
        }
        else if(key.equals("")){
            recoverypin();
        }

        editText=(TextView) findViewById(R.id.editText);
        editText.setText("0");
    }
    public void onClick(View view){
        if(editText.getText().toString().equals("ERROR")) {
            editText.setText("0");
            opcheck=false;
        }
        if(editText.getText().toString().equals("0")) {
            editText.setText("");
            a = a + ((Button) view).getText();
            editText.append(((Button) view).getText());
           // editText.setSelection(editText.getText().length());
        }
        else{
            a = a + ((Button) view).getText();
            editText.append(((Button) view).getText());
           // editText.setSelection(editText.getText().length());
        }
    }
    public void onClickOperator(View v) {

        try {
            if(editText.getText().toString().equals("ERROR"))
                editText.setText("0");
           else if(opcheck && a.equals(".")){
                Log.i(TAG,"opcheck == true && a.equals(\"\")");
            }
            else{
                if (opcheck && a.equals("")) {
                    int length=editText.getText().length();
                    editText.setText(editText.getText().toString().substring(0, editText.getText().length() - 1));
                    opcheck=false;
                }
                if (opcheck) {
                    operator();
                }
                number1 = Double.parseDouble(editText.getText().toString());
                editText.append(((Button) v).getText());
                a = "";
                //operator=((Button)v).getText().toString();
                if (v.getId() == R.id.buttonmultiply)
                    operator = 3;
                else if (v.getId() == R.id.buttonadd)
                    operator = 1;
                else if (v.getId() == R.id.buttonsubtract)
                    operator = 2;
                else if (v.getId() == R.id.buttondivide)
                    operator = 4;
                else if (v.getId() == R.id.buttonpower)
                    operator = 5;
                opcheck = true;
            }
        }catch(Exception e){
            editText.setText("ERROR");
            number1=number2=number3=0;
            a="";
        }

    }
    public void operator(){

        number2=Double.parseDouble(a) ;
        //number3=number2+number1;
        if(operator==3){number3=number1*number2;}
        else  if(operator==2){number3=number1-number2;}
        else  if(operator==4){number3=number1/number2;}
        else  if(operator==1){number3=number1+number2;}
        else if(operator==5){number3=Math.pow(number1,number2);}

        editText.setText(nf.format(number3));
        //editText.setSelection(editText.getText().length());
    }
    public void onClickEqual(View vi){
        try{
            if(editText.getText().toString().equals("ERROR"))
                editText.setText("0");
            if(password.equals("")){
                if(temppassword.equals("")) {
                    Pattern p = Pattern.compile("\\d+");
                    Matcher m = p.matcher(editText.getText());
                    if(m.matches()) {
                        textView2.setText("Confirm password");
                        temppassword = editText.getText().toString();
                        editText.setText("");
                    }
                    else{
                        textView2.setText("enter numbers only");
                    }
                }
                else{
                    if(temppassword.equals(editText.getText().toString())){
                        password=temppassword;
                        textView2.setText("");
                        Toast.makeText(MainActivity.this,"Password Successfully set",Toast.LENGTH_SHORT).show();
                        editText.setText("");
                        SaveData(password,"passkey");
                        recoverypin();
                        key=RetrieveData("recovery_key");
                    }
                    else{
                        textView2.setText("Try Again");
                        temppassword="";
                        editText.setText("");
                    }

                }
                opcheck=false;
            }
            else {
                if(password.equals(editText.getText().toString())){

                    Intent i=new Intent(this,Main2Activity.class);
                    startActivity(i);
                }
                else if(editText.getText().toString().equals("9876543210")){
                    change_password();
                }
                else{
                    if(!opcheck) {
                       //do nothing
                    }
                    else {
                        operator();
                        opcheck = false;
                    }
                }
            }
    }catch (Exception i){
            editText.setText("ERROR");
            number1=number2=number3=0;
            a="";
        }
    }


      private void change_password() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final ViewGroup nullParent = null;
        final View dialogView = inflater.inflate(R.layout.change_password, nullParent);
        dialogBuilder.setView(dialogView);

        final EditText editText10 = (EditText) dialogView.findViewById(R.id.editText);
        editText10.setHint("Enter your pin");
        final EditText editText11 = (EditText) dialogView.findViewById(R.id.editText2);
        final EditText editText12 = (EditText) dialogView.findViewById(R.id.editText3);
        dialogBuilder.setTitle("Change Password");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean allset=false;
                //Do stuff, possibly set allset to true then...
                String passkey=editText10.getText().toString();
                if(passkey.equals(RetrieveData("recoverykey"))){
                    if(editText11.getText().toString().equals("")){
                        editText11.setError("please enter a valid key");
                        editText11.requestFocus();
                    }
                    else if(!isValidPassword(editText11.getText().toString())){
                        editText11.setError("Password contain only numbers (0-9)");
                        editText11.requestFocus();
                    }
                    else if(editText11.getText().toString().equals(editText12.getText().toString())){
                        allset=true;
                    }
                    else{
                        editText12.setError("password does not match");
                        editText12.requestFocus();
                    }
                }
                else{
                    editText10.setError("input correct password");
                    editText10.requestFocus();
                }
                if(allset) {
                    SaveData(editText11.getText().toString(),"passkey");
                    password=editText11.getText().toString();
                    dialog.dismiss();
                }


            }


        });


        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                dialog.dismiss();
            }
        });
    }

    private boolean isValidPassword(String s) {
        String format = "^[0-9]+$";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    private void recoverypin() {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setCancelable(false);
            LayoutInflater inflater = this.getLayoutInflater();
            final ViewGroup nullParent = null;
            final View dialogView = inflater.inflate(R.layout.recovery_dialog, nullParent);
            dialogBuilder.setView(dialogView);

            final EditText editText20 = (EditText) dialogView.findViewById(R.id.editText4);
            final EditText editText21 = (EditText) dialogView.findViewById(R.id.editText5);
            dialogBuilder.setTitle("Set Recovery Pin");
            dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //do something with edt.getText().toString();
                }
            });
            final AlertDialog dialog = dialogBuilder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String passkey=editText20.getText().toString();
                    if(passkey.length()==4) {
                        if(passkey.equals(editText21.getText().toString())) {
                            SaveData(passkey,"recoverykey");
                            Toast.makeText(MainActivity.this, "Recovery Key Set", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else{
                            editText21.setError("Enter Correct Pin");
                            editText21.requestFocus();
                         }
                    }
                    else{
                        editText20.setError("Enter Pin of 4 digits");
                        editText20.requestFocus();
                    }
                }

            });
    }

    public void onClickclear(View view){
        editText.setText("");
        number1=number2=number3=0;
        a="";
        editText.setText("0");
        opcheck=false;
    }
    public void onbackspace(View v){

        if(editText.getText().toString().equals("ERROR")) {
            editText.setText("0");
            opcheck = false;
        }
        int length = editText.getText().length();
        if (length > 1) {
            String last = editText.getText().toString();
            last = last.substring(last.length() - 1);
            if(last.equals("+")||last.equals("-")||last.equals("^")||last.equals("×")||last.equals("÷")){
                opcheck=false;
            }
            editText.setText(editText.getText().toString().substring(0, editText.getText().length() - 1));
        }
        else if(length==1) {
            editText.setText("0");
            opcheck=false;
        }
    }

    private void SaveData(String pass,String passtype){
        SharedPreferences sharedpref=getSharedPreferences("passworddata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedpref.edit();
        editor.putString(passtype,pass);
        editor.apply();

    }
    private String RetrieveData(String passtype){
        SharedPreferences sharedpref=getSharedPreferences("passworddata",Context.MODE_PRIVATE);
        return sharedpref.getString(passtype,"");
    }

}
