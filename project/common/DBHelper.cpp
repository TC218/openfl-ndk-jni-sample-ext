
#include "DBHelper.h"


namespace dbhelper 
{	
  JNIEnv *env;
  jclass cls;
  jclass string_cls;
  
  jmethodID j_insert;
  
  
  void init(JNIEnv *jni_env)
  {
    env = jni_env;
    cls = (jclass)env->NewGlobalRef(env->FindClass("org/haxe/extension/DBHelper"));    
    string_cls = (jclass)env->NewGlobalRef(env->FindClass("java/lang/String"));    
    
    j_insert = env->GetStaticMethodID(cls, "insert", "(Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)I");        
   

  }

  
	value insert(value table, value keys, value values)
  {
    int len = val_array_size(keys);
    
    jobjectArray j_keys = env->NewObjectArray(len, string_cls, NULL);
    jobjectArray j_values = env->NewObjectArray(len, string_cls, NULL);        

    for (int i=0; i<len; i++)
    {      
      jstring s = env->NewStringUTF(val_string(val_array_i(keys, i)));
      env->SetObjectArrayElement(j_keys, i, s );
      env->DeleteLocalRef(s);
      
      s = env->NewStringUTF(val_string(val_array_i(values, i)));
      env->SetObjectArrayElement(j_values, i, s );
      env->DeleteLocalRef(s);
    }
    
    jstring j_table = env->NewStringUTF(val_string(table));
    
    value result = alloc_int(env->CallStaticIntMethod(cls, j_insert, j_table, j_keys, j_values));
    
    env->DeleteLocalRef(j_keys);    
    env->DeleteLocalRef(j_values);    
    env->DeleteLocalRef(j_table);    
    
    return result;
	}
	
}