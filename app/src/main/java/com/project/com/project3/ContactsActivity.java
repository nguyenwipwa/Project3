package com.project.com.project3;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.com.project3.model.Contact;
import com.project.com.project3.model.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.project.com.project3.model.FileUtils.getPath;

public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayAdapter<String> adapter;
    ListView listView;
    String vfile;
    TextView urlText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        anhXa();
        getList();

    }

    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/x-vcard");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Import"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void importContacts(String file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(file)), "text/x-vcard"); //storage path is path of your vcf file and vFile is name of that file.
        startActivity(intent);
    }

    private String getVcardString() throws IOException {
        // TODO Auto-generated method stub
        vCard = new ArrayList<String>();  // Its global....
        vfile = "Contacts" + "_" + System.currentTimeMillis() + ".vcf";
        String storage_path = "";
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            int i;
            storage_path = Environment.getExternalStorageDirectory().toString() + File.separator + vfile;
            FileOutputStream mFileOutputStream = new FileOutputStream(storage_path, false);
            cursor.moveToFirst();
            for (i = 0; i < cursor.getCount(); i++) {
                get(cursor);
                Log.d("TAG", "Contact " + (i + 1) + "VcF String is" + vCard.get(i));
                cursor.moveToNext();
                mFileOutputStream.write(vCard.get(i).toString().getBytes());
            }
            mFileOutputStream.close();
            cursor.close();
        } else {
            Log.d("TAG", "No Contacts in Your Phone");
        }
        return storage_path;
    }

    ArrayList<String> vCard;

    private void get(Cursor cursor) {
        String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
        AssetFileDescriptor fd;
        try {
            fd = this.getContentResolver().openAssetFileDescriptor(uri, "r");

            FileInputStream fis = fd.createInputStream();
            byte[] buf = new byte[(int) fd.getDeclaredLength()];
            fis.read(buf);
            String vcardstring = new String(buf);
            vCard.add(vcardstring);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private void getList() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        startManagingCursor(cursor);
        String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone._ID};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter listadapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to);
        listView.setAdapter(listadapter);
    }

    public void anhXa() {
        listView = (ListView) findViewById(R.id.listContact);
        urlText = (TextView) findViewById(R.id.urlFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("TAG", "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d("TAG", "File Path: " + path);
                    importContacts(path);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnExport:
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute("EXPORT");
                break;
            case R.id.btnImport:
                showFileChooser();
                break;
            default:
                break;
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Waiting..."); // Calls onProgressUpdate()
            try {
                resp = getVcardString();
            } catch (IOException e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            urlText.setText(result);
            Toast.makeText(ContactsActivity.this, "Xong", Toast.LENGTH_SHORT).show();
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ContactsActivity.this,
                    "SAVING",
                    "Waiting");
        }


        @Override
        protected void onProgressUpdate(String... text) {
            urlText.setText(text[0]);
        }
    }
}
