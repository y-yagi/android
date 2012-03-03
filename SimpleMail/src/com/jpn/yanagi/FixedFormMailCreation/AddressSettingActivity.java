
package com.jpn.yanagi.FixedFormMailCreation;
 
import com.jpn.yanagi.FixedFormMailCreation.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.ContactMethods;
import android.provider.Contacts.ContactMethodsColumns;
import android.provider.Contacts.People;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class AddressSettingActivity extends Activity implements OnClickListener {

	private static final int REQUEST_PICK_CONTACT = 1;

	private Button mPickButton = null;
	private TextView mNameText = null;
	private TextView mNumberText = null;
	private TextView mAddressText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pick_contacts);
		mPickButton = (Button) findViewById(R.id.pick_contacts_button);
		mNameText = (TextView) findViewById(R.id.contacts_name_text);
		mNumberText = (TextView) findViewById(R.id.contacts_number_text);
		mAddressText = (TextView) findViewById(R.id.contacts_address_text);
		mPickButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if( v.getId() == R.id.pick_contacts_button) {
			Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
			startActivityForResult(intent, REQUEST_PICK_CONTACT);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
		if (requestCode == REQUEST_PICK_CONTACT && resultCode == Activity.RESULT_OK) {
			Uri uri = returnedIntent.getData();
			Cursor personCursor = managedQuery(uri, null, null, null, null);
			if (personCursor.moveToFirst()) {
				int idIndex = personCursor.getColumnIndexOrThrow(People._ID);
				int nameIndex = personCursor.getColumnIndexOrThrow(People.NAME);
				int numberIndex = personCursor.getColumnIndexOrThrow(People.NUMBER);
				
				String id = personCursor.getString(idIndex);
				String name = personCursor.getString(nameIndex);
				String number = personCursor.getString(numberIndex);
				String address = "";

				StringBuilder where = new StringBuilder();
				where.append(ContactMethods.PERSON_ID).append(" == ? AND ");
				where.append(ContactMethods.KIND).append(" == ?");
				String selection = where.toString();
				String[] selectionArgs = {id, String.valueOf(Contacts.KIND_POSTAL)};
				Cursor addressCursor = managedQuery(ContactMethods.CONTENT_URI, null, selection, selectionArgs, null);
				if (addressCursor.moveToFirst()) {
					int addressIndex = addressCursor.getColumnIndexOrThrow(ContactMethodsColumns.DATA);
					address = addressCursor.getString(addressIndex);
				}

				mNameText.setText(name);
				mNumberText.setText(number);
				mAddressText.setText(address);
			}
		}
	}
}
