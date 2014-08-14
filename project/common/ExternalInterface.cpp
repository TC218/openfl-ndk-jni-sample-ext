#ifndef STATIC_LINK
#define IMPLEMENT_API
#endif

#if defined(HX_WINDOWS) || defined(HX_MACOS) || defined(HX_LINUX)
#define NEKO_COMPATIBLE
#endif


#include <hx/CFFI.h>
#include "Utils.h"
#include "DBHelper.h"




static value aptiekaextension_sample_method (value inputValue) {
	
	int returnValue = aptiekaextension::SampleMethod(val_int(inputValue));
	return alloc_int(returnValue);
	
}
DEFINE_PRIM (aptiekaextension_sample_method, 1);


static void aptiekaextension_init(void) 
{
  aptiekaextension::init();
}
DEFINE_PRIM (aptiekaextension_init, 0);



static value dbhelper_insert (value table, value keys, value values) 
{
	return dbhelper::insert(table, keys, values); 
}
DEFINE_PRIM (dbhelper_insert, 3);



extern "C" void aptiekaextension_main () 
{	
	val_int(0); // Fix Neko init
	
//  ALOG("aptiekaextension_main()");
    
}
DEFINE_ENTRY_POINT (aptiekaextension_main);



extern "C" int aptiekaextension_register_prims () { return 0; }



	#ifdef ANDROID

		extern "C"{
			JNIEXPORT void JNICALL Java_org_haxe_extension_AptiekaExtension_init(
				                   													JNIEnv * env ,
				                   													jobject obj
				                   													)
      {
//				ALOG("init called!");
			}
      
      
      JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
      {
        /*
          JNIEnv *env;
          ALOG("JNI_OnLoad called");
          if (vm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
              ALOG("Failed to get the environment using GetEnv()");
              return -1;
          }
          */
          
          aptiekaextension::JNI_OnLoad(vm);
          
          return JNI_VERSION_1_6;
      }

		}

	#endif