package org.haxe.extension;

import java.util.*;
import org.json.JSONArray;

import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;


public class DBHelper extends SQLiteOpenHelper 
{
    private HashMap hp;
    private static final String TAG = "DbHelper";
    private static SQLiteDatabase db;



    public DBHelper(Context context)
    {
      super(context, "aptieka.db" , null, 1);

      db = this.getWritableDatabase();

      //onVideoStatus("some 1", "other 2");
    }


    public static int insert2(int val)
    {
      return val*val;
    }

    public static int insert(String table, String[] keys, String[] values)
    {
      List<String> keys_sql = new ArrayList<String>();
      List<String> values_sql = new ArrayList<String>();

      for (int i=0; i<keys.length; i++)
      {
        keys_sql.add("[" + keys[i] + "]");
        values_sql.add(DatabaseUtils.sqlEscapeString(values[i]));
      }

      db.execSQL("INSERT INTO [" + table + "] (" + concatStringsWSep(keys_sql, ", ") + ") VALUES (" + concatStringsWSep(values_sql, ", ") + ")");

      Cursor res = db.rawQuery("SELECT last_insert_rowid()", null);
      res.moveToFirst();
      return res.getInt(0);
    }

  public static int update(String table, String[] keys, String[] values, String[] cond_keys, String[] cond_values)
  {
    List<String> fields_sql = new ArrayList<String>();

    for (int i=0; i<keys.length; i++)
    {
      fields_sql.add("[" + keys[i] + "] = " + DatabaseUtils.sqlEscapeString(values[i]));
    }

    String sql = buildCondSql(cond_keys, cond_values);
    db.execSQL("UPDATE [" + table + "] SET " + concatStringsWSep(fields_sql, ", ") + sql);

    Cursor res = db.rawQuery("SELECT changes()", null);
    res.moveToFirst();
    return res.getInt(0);
  }

  public static int delete(String table, String[] cond_keys, String[] cond_values)
  {
    String sql = buildCondSql(cond_keys, cond_values);
    db.execSQL("DELETE FROM [" + table + "] " + sql);

    Cursor res = db.rawQuery("SELECT changes()", null);
    res.moveToFirst();
    return res.getInt(0);
  }

  public static String[][] loadOne(String table, int id)
    {
      Cursor res = db.rawQuery("SELECT * FROM [" + table + "] WHERE id=" + id, null);
      if (res.getCount() == 0) {
        return new String[][]{ new String[0], new String[0], new String[0] };
      }

      res.moveToFirst();

      int cols = res.getColumnCount();
      String[] keys = new String[cols];
      String[] values = new String[cols];
      String[] types = new String[cols];

      for (int i=0; i<cols; i++)
      {
        keys[i] = res.getColumnName(i);
        values[i] = res.getString(i);
        types[i] = String.valueOf(res.getType(i));
      }

      return new String[][]{ keys, values, types };
    }

  static String buildCondSql(String[] cond_keys, String[] cond_values)
  {
    List<String> cond_sql = new ArrayList<String>();

    for (int i=0; i<cond_keys.length; i++)
    {
      cond_sql.add("[" + cond_keys[i] + "] = " + DatabaseUtils.sqlEscapeString(cond_values[i]));
    }

    String sql = concatStringsWSep(cond_sql, " AND ");
    if (sql.length() > 0) {
      sql = " WHERE " + sql;
    }

    return sql;
  }

  public static String[][] getList(String table, String[] cond_keys, String[] cond_values)
  {
    String sql = buildCondSql(cond_keys, cond_values);
    //Log.d(TAG, "SELECT * FROM [" + table + "]" + sql);

    Cursor res = db.rawQuery("SELECT * FROM [" + table + "]" + sql, null);
    //Cursor res = db.rawQuery("SELECT * FROM [categories] WHERE [name] = 'somecat 123'", null);
    if (res.getCount() == 0) {
      return new String[][]{ new String[0], new String[0], new String[0] };
    }

    res.moveToFirst();
/*
    Log.d(TAG, "count = " + res.getCount());

    return new String[][]{ new String[]{}, new String[]{}, new String[]{} };
*/

    int cols = res.getColumnCount();
    String[] keys = new String[cols];
    String[] values = new String[res.getCount() * cols];
    String[] types = new String[cols];

    for (int i=0; i<cols; i++)
    {
      keys[i] = res.getColumnName(i);
      types[i] = String.valueOf(res.getType(i));
    }

    int j = 0;
    while(res.isAfterLast() == false)
    {
      for (int i=0; i<cols; i++) {
        values[j * cols + i] = res.getString(i);
      }

      res.moveToNext();
      j++;
    }

    return new String[][]{ keys, values, types };
  }

  public static int count(String table, String[] cond_keys, String[] cond_values)
  {
    String sql = buildCondSql(cond_keys, cond_values);

    Cursor res = db.rawQuery("SELECT COUNT(*) FROM [" + table + "]" + sql, null);
    res.moveToFirst();
    return res.getInt(0);
  }

  public static String[][] arrayDemo(int id, String[] keys, String[] values/*, int parent_id, String name*/)
    {      
      Log.d(TAG, "GOT INT " + id);
    
      for (int i=0; i<keys.length; i++)
      {
        Log.d(TAG, "INS2: " + keys[i] + ": " + values[i]);
      }
      
      HashMap cat = new HashMap<String, Object>();
      cat.put("id", id);
      cat.put("id2", id);
      cat.put("id3", id);
      //cat.put("parent_id", parent_id);
      //cat.put("name", name);
      
      //insert("categories", cat);
      
      return new String[][]{ new String[]{"some", "values", "123"}, new String[]{"22_some", "22_values", "22_123"} };
    }

  public static String concatStringsWSep(List<String> strings, String separator) {
    StringBuilder sb = new StringBuilder();
    String sep = "";
    for(String s: strings) {
      sb.append(sep).append(s);
      sep = separator;
    }
    return sb.toString();
  }

  @Override
   public void onCreate(SQLiteDatabase db) {
      Log.d(TAG, "CREATE...");
      db.execSQL("CREATE TABLE revisions (id text primary key, timestamp integer)");
      db.execSQL("CREATE TABLE categories (id integer primary key, parent_id integer, name text)");      
      db.execSQL("CREATE TABLE products (id integer primary key, category_id integer, title text, short_description text, full_description text, instruction text, ingredients text, in_stock integer, net_price real, picture_url text)");
      
      db.execSQL("CREATE INDEX parent_id ON categories (parent_id);");
      db.execSQL("CREATE INDEX category_id ON products (category_id);");     

      ContentValues content = new ContentValues();

      content.put("name", "some cat 123");
      
      db.insert("categories", null, content);
      
      ArrayList array_list = new ArrayList();
      Cursor res =  db.rawQuery( "select * from categories", null );      
      res.moveToFirst();
      while(res.isAfterLast() == false){
        String val = res.getString(res.getColumnIndex("name"));
        array_list.add(val);
        Log.d(TAG, "GOT VAL: " + val);
        res.moveToNext();
      }
   
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // TODO Auto-generated method stub
      //db.execSQL("DROP TABLE IF EXISTS contacts");
      Log.d(TAG, "UPGRADE...");
      
      onCreate(db);
   }

}