#ifndef DBHELPER_H
#define DBHELPER_H

#include "Utils.h"

#include <jni.h>
#include <hx/CFFI.h>



namespace dbhelper 
{
  void init(JNIEnv *env);  
	value insert(value table, value keys, value values);
	
}


#endif