package com.example.smstest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements
			LoaderCallbacks<Cursor> {
		private static final String TAG = PlaceholderFragment.class
				.getSimpleName();
		private static Uri SMS_URI = Uri.parse("content://sms/");

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onStart() {
			super.onStart();
			getLoaderManager().initLoader(0, null, PlaceholderFragment.this);

		}

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			return new CursorLoader(this.getActivity().getApplicationContext(),
					SMS_URI, null, null, null, null);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
			List<Map<String, Object>> columnList = new ArrayList<Map<String, Object>>();
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy/MM/dd HH:mm:ss");

			Log.v(TAG, "ColumnNames:");
			String[] columnNames = cursor.getColumnNames();
			for (String str : columnNames) {
				Log.v(TAG, str);
			}

			if (cursor.moveToFirst()) {
				boolean next = cursor.moveToNext();
				while (next) {
					Map<String, Object> columnMap = new HashMap<String, Object>();
					columnMap
							.put("title",//
									"address:"
											+ cursor.getString(cursor
													.getColumnIndex("address")) //
											+ "\ndate:"
											+ format.format(new Date(
													cursor.getLong(cursor
															.getColumnIndex("date"))))//
											+ "\nbody:"
											+ cursor.getString(cursor
															.getColumnIndex("body"))//
							);
					columnMap.put("_id",
							cursor.getInt(cursor.getColumnIndex("_id")));
					columnList.add(columnMap);
					next = cursor.moveToNext();
				}
			}

			SimpleAdapter adapter = new SimpleAdapter(this.getActivity()
					.getApplicationContext(),//
					columnList,//
					R.layout.list,//
					new String[] { "_id", "title" },//
					new int[] { R.id.no, R.id.name });//
			ListView listView = (ListView) this.getActivity().findViewById(
					R.id.listView);
			listView.setAdapter(adapter);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> arg0) {
			// TODO Auto-generated method stub

		}
	}

}
