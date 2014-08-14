package ;

#if cpp
  import cpp.Lib;
#elseif neko
  import neko.Lib;
#end

#if android
  import openfl.utils.JNI;
#end

// http://www.tutorialspoint.com/android/android_sqlite_database.htm


class DBHelper
{
#if android
  static inline var pkg = 'org.haxe.extension';
  
  static var jni_arrayDemo : Dynamic = openfl.utils.JNI.createStaticMethod(
        pkg + ".DBHelper",
        "arrayDemo",
        "(I[Ljava/lang/String;[Ljava/lang/String;)[[Ljava/lang/String;"
    );
    
  static var jni_insert : Dynamic = openfl.utils.JNI.createStaticMethod(
        pkg + ".DBHelper",
        "insert",
        "(Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)I"
    );
    
  static var jni_update : Dynamic = openfl.utils.JNI.createStaticMethod(
        pkg + ".DBHelper",
        "update",
        "(Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)I"
    );
    
  static var jni_delete : Dynamic = openfl.utils.JNI.createStaticMethod(
        pkg + ".DBHelper",
        "delete",
        "(Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)I"
    );
    
  static var jni_loadOne : Dynamic = openfl.utils.JNI.createStaticMethod(
        pkg + ".DBHelper",
        "loadOne",
        "(Ljava/lang/String;I)[[Ljava/lang/String;"
    );
    
  static var jni_getList : Dynamic = openfl.utils.JNI.createStaticMethod(
        pkg + ".DBHelper",
        "getList",
        "(Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)[[Ljava/lang/String;"
    );
    
  static var jni_count : Dynamic = openfl.utils.JNI.createStaticMethod(
        pkg + ".DBHelper",
        "count",
        "(Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)I"
    );

	static var dbhelper_insert = Lib.load("aptiekaextension", "dbhelper_insert", 3);
    
/*
 * Before calling a Java object's method from JNI, we need its signature. For example, the method
long myMethod (int n, String s, int[] arr);
is seen from JNI with the signature
(ILJAVA/LANG/STRING;[I)J    
 * 
*/
  
  public static function arrayDemo(id:Int, keys:Array<String>, values:Array<String>)
  {
    return jni_arrayDemo(id, keys, values);
  }
  
  public static function insert(table:String, fields:Dynamic):Int
  {
    //var env:Dynamic = JNI.getEnv();
    //env.PushLocalFrame(env, 10);
    
    var keys:Array<String> = [];
    var values:Array<String> = [];
    
    for (k in Reflect.fields(fields))
    {
      keys.push(k);
      values.push(''+Reflect.field(fields, k));
    }
    
    var result:Int = dbhelper_insert(table, keys, values);
    //env.PopLocalFrame(env);
    return result;
  }

  
  public static function loadOne(table:String, id:Int):Dynamic
  {
    var result:Dynamic = {};
    var jni_result:Array<Array<String>> = jni_loadOne(table, id);
    for (i in 0...jni_result[0].length)
    {
      var val:Dynamic = jni_result[1][i];
      
      switch (jni_result[2][i])
      {
        case '1': val = Std.parseInt(val);
        case '2': val = Std.parseFloat(val);        
      }
      
      Reflect.setField(result, jni_result[0][i], val);
    }
    
    return result;
  }

  public static function update(table:String, fields:Dynamic, cond:Dynamic):Int
  {
    var keys:Array<String> = [];
    var values:Array<String> = [];
    
    for (k in Reflect.fields(fields))
    {
      keys.push(k);
      values.push(''+Reflect.field(fields, k));
    }
    
    
    var cond_keys:Array<String> = [];
    var cond_values:Array<String> = [];
    
    for (k in Reflect.fields(cond))
    {
      cond_keys.push(k);
      cond_values.push(''+Reflect.field(cond, k));
    }
    
    return jni_update(table, keys, values, cond_keys, cond_values);
  }

  public static function delete(table:String, cond:Dynamic):Int
  {
    var cond_keys:Array<String> = [];
    var cond_values:Array<String> = [];
    
    for (k in Reflect.fields(cond))
    {
      cond_keys.push(k);
      cond_values.push(''+Reflect.field(cond, k));
    }
    
    return jni_delete(table, cond_keys, cond_values);
  }
  
  public static function getList(table:String, cond:Dynamic):Array<Dynamic>
  {
    var cond_keys:Array<String> = [];
    var cond_values:Array<String> = [];
    
    for (k in Reflect.fields(cond))
    {
      cond_keys.push(k);
      cond_values.push(''+Reflect.field(cond, k));
    }
    
    var result = new Array<Dynamic>();
    var jni_result:Array<Array<String>> = jni_getList(table, cond_keys, cond_values);
    var cols = jni_result[0].length;

    var j = 0;
    while (j < jni_result[1].length)
    {
      var row:Dynamic = {};
      for (i in 0...cols)
      {
        var val:Dynamic = jni_result[1][j + i];
        
        switch (jni_result[2][i])
        {
          case '1': val = Std.parseInt(val);
          case '2': val = Std.parseFloat(val);        
        }
        
        Reflect.setField(row, jni_result[0][i], val);
      }
      
      result.push(row);
      j += cols;
    }

    return result;
  }
  
  public static function count(table:String, ?cond:Dynamic):Int
  {
    var cond_keys:Array<String> = [];
    var cond_values:Array<String> = [];
    
    if (cond != null)
    {
      for (k in Reflect.fields(cond))
      {
        cond_keys.push(k);
        cond_values.push(''+Reflect.field(cond, k));
      }      
    }

    return jni_count(table, cond_keys, cond_values);
  }
  
#end
  
}