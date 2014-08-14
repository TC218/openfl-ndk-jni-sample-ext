#include "Utils.h"
#include "DBHelper.h"


namespace aptiekaextension 
{
	JavaVM *java_vm;
  JNIEnv *jni_ndk_env;
  
  
  
  void JNI_OnLoad(JavaVM *vm)
  {
    java_vm = vm;
    ALOG("JNI_ONLOAD");    
  }
	
  void init()
  {
    ALOG("INIT()");

    int status = java_vm->AttachCurrentThread(&jni_ndk_env, NULL);
    
    dbhelper::init(jni_ndk_env);
    
//    java_vm->DetachCurrentThread();
  }
  
	int SampleMethod(int inputValue) {
		
		return inputValue * 200;
		
	}
	
	
}